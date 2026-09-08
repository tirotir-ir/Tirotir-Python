package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CurriculumData
import com.example.data.QuizData
import com.example.data.QuizQuestion
import com.example.data.ScriptEntity
import com.example.data.UserProgressEntity
import com.example.engine.CodeTracer
import com.example.engine.PythonInterpreter
import com.example.engine.TraceStep
import com.example.engine.TurtleEngine
import com.example.engine.TurtleLine
import com.example.engine.TurtleState
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainDestination {
  HOME,
  SYLLABUS,
  IDE_LAB,
  PROJECTS,
  MORE
}

enum class MoreSubDestination {
  MENU,
  TRACER,
  TURTLE,
  QUIZZES,
  CERTIFICATE,
  SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val db = AppDatabase.getInstance(application)
  private val scriptDao = db.scriptDao()
  private val userProgressDao = db.userProgressDao()
  private val interpreter = PythonInterpreter()

  // Saved scripts from Room
  val savedScripts: StateFlow<List<ScriptEntity>> = scriptDao.getAllScripts()
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Navigation state
  private val _currentDestination = MutableStateFlow(MainDestination.HOME)
  val currentDestination: StateFlow<MainDestination> = _currentDestination.asStateFlow()

  private val _moreSubDestination = MutableStateFlow(MoreSubDestination.MENU)
  val moreSubDestination: StateFlow<MoreSubDestination> = _moreSubDestination.asStateFlow()

  // Theme mode
  private val _themeMode = MutableStateFlow(AppThemeMode.DARK)
  val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

  // User progress
  private val _userProgress = MutableStateFlow(UserProgressEntity())
  val userProgress: StateFlow<UserProgressEntity> = _userProgress.asStateFlow()

  // IDE State
  private val _ideCode = MutableStateFlow(
      """# tirOtir AI Academy - Interactive Lab
# Press 'Run Code' to execute
print("Hello, tirOtir AI Academy!")
a = 15
b = 25
total = a + b
print(f"Calculation: {a} + {b} = {total}")

for i in range(1, 4):
    print("Lesson step:", i)
"""
  )
  val ideCode: StateFlow<String> = _ideCode.asStateFlow()

  private val _terminalOutput = MutableStateFlow("")
  val terminalOutput: StateFlow<String> = _terminalOutput.asStateFlow()

  private val _isExecutionSuccess = MutableStateFlow(true)
  val isExecutionSuccess: StateFlow<Boolean> = _isExecutionSuccess.asStateFlow()

  private val _executionDurationMs = MutableStateFlow(0L)
  val executionDurationMs: StateFlow<Long> = _executionDurationMs.asStateFlow()

  private val _isExecuting = MutableStateFlow(false)
  val isExecuting: StateFlow<Boolean> = _isExecuting.asStateFlow()

  private val _inputPrompt = MutableStateFlow<String?>(null)
  val inputPrompt: StateFlow<String?> = _inputPrompt.asStateFlow()

  private var pendingInputCallback: ((String) -> Unit)? = null

  // Tracer State
  private val _tracerCode = MutableStateFlow(CodeTracer.presets[0].code)
  val tracerCode: StateFlow<String> = _tracerCode.asStateFlow()

  private val _traceSteps = MutableStateFlow<List<TraceStep>>(emptyList())
  val traceSteps: StateFlow<List<TraceStep>> = _traceSteps.asStateFlow()

  private val _currentStepIndex = MutableStateFlow(0)
  val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

  private val _isTracerPlaying = MutableStateFlow(false)
  val isTracerPlaying: StateFlow<Boolean> = _isTracerPlaying.asStateFlow()

  private val _tracerSpeedMs = MutableStateFlow(800L)
  val tracerSpeedMs: StateFlow<Long> = _tracerSpeedMs.asStateFlow()

  private var tracerJob: Job? = null

  // Turtle Canvas State
  private val _turtleCode = MutableStateFlow(TurtleEngine.templates[0].code)
  val turtleCode: StateFlow<String> = _turtleCode.asStateFlow()

  private val _turtleLines = MutableStateFlow<List<TurtleLine>>(emptyList())
  val turtleLines: StateFlow<List<TurtleLine>> = _turtleLines.asStateFlow()

  private val _turtleState = MutableStateFlow(TurtleState())
  val turtleState: StateFlow<TurtleState> = _turtleState.asStateFlow()

  // Quiz State
  private val _selectedAnswers = MutableStateFlow<Map<String, Int>>(emptyMap())
  val selectedAnswers: StateFlow<Map<String, Int>> = _selectedAnswers.asStateFlow()

  private val _submittedQuizzes = MutableStateFlow<Set<String>>(emptySet())
  val submittedQuizzes: StateFlow<Set<String>> = _submittedQuizzes.asStateFlow()

  init {
    loadUserProgress()
    runTurtle()
    loadTracer(CodeTracer.presets[0].code)
  }

  fun navigateTo(dest: MainDestination) {
    _currentDestination.value = dest
    if (dest == MainDestination.MORE) {
      _moreSubDestination.value = MoreSubDestination.MENU
    }
  }

  fun navigateToMoreSub(sub: MoreSubDestination) {
    _currentDestination.value = MainDestination.MORE
    _moreSubDestination.value = sub
  }

  fun setTheme(mode: AppThemeMode) {
    _themeMode.value = mode
  }

  fun updateIdeCode(newCode: String) {
    _ideCode.value = newCode
  }

  fun runIdeCode() {
    viewModelScope.launch {
      _isExecuting.value = true
      _terminalOutput.value = "Executing script in Python runtime..."
      val result = interpreter.execute(
          code = _ideCode.value,
          inputCallback = { prompt ->
            _inputPrompt.value = prompt
            kotlin.coroutines.suspendCoroutine { cont ->
              pendingInputCallback = { entered ->
                _inputPrompt.value = null
                cont.resumeWith(Result.success(entered))
              }
            }
          }
      )
      _terminalOutput.value = result.output
      _isExecutionSuccess.value = result.isSuccess
      _executionDurationMs.value = result.executionTimeMs
      _isExecuting.value = false

      // Update progress stats
      val curr = _userProgress.value
      val updated = curr.copy(labRunsCount = curr.labRunsCount + 1)
      _userProgress.value = updated
      userProgressDao.saveUserProgress(updated)
    }
  }

  fun submitInput(response: String) {
    pendingInputCallback?.invoke(response)
    pendingInputCallback = null
  }

  fun clearTerminal() {
    _terminalOutput.value = ""
  }

  fun loadCodeIntoIde(code: String) {
    _ideCode.value = code
    _currentDestination.value = MainDestination.IDE_LAB
  }

  fun saveCurrentScript(title: String) {
    viewModelScope.launch {
      val entity = ScriptEntity(title = title, code = _ideCode.value)
      scriptDao.insertScript(entity)
    }
  }

  fun deleteScript(id: Long) {
    viewModelScope.launch {
      scriptDao.deleteById(id)
    }
  }

  // --- TRACER METHODS ---
  fun loadTracer(code: String) {
    _tracerCode.value = code
    val steps = CodeTracer.trace(code)
    _traceSteps.value = steps
    _currentStepIndex.value = 0
    stopTracerAutoplay()
  }

  fun stepForwardTracer() {
    if (_currentStepIndex.value < _traceSteps.value.size - 1) {
      _currentStepIndex.value += 1
    } else {
      stopTracerAutoplay()
    }
  }

  fun resetTracer() {
    stopTracerAutoplay()
    _currentStepIndex.value = 0
  }

  fun togglePlayTracer() {
    if (_isTracerPlaying.value) {
      stopTracerAutoplay()
    } else {
      startTracerAutoplay()
    }
  }

  private fun startTracerAutoplay() {
    _isTracerPlaying.value = true
    tracerJob?.cancel()
    tracerJob = viewModelScope.launch {
      while (_isTracerPlaying.value) {
        if (_currentStepIndex.value < _traceSteps.value.size - 1) {
          _currentStepIndex.value += 1
          delay(_tracerSpeedMs.value)
        } else {
          _isTracerPlaying.value = false
          break
        }
      }
    }
  }

  private fun stopTracerAutoplay() {
    _isTracerPlaying.value = false
    tracerJob?.cancel()
    tracerJob = null
  }

  fun setTracerSpeed(speedMs: Long) {
    _tracerSpeedMs.value = speedMs
  }

  // --- TURTLE METHODS ---
  fun updateTurtleCode(newCode: String) {
    _turtleCode.value = newCode
  }

  fun runTurtle() {
    val (lines, state) = TurtleEngine.parseAndRun(_turtleCode.value)
    _turtleLines.value = lines
    _turtleState.value = state
  }

  fun resetTurtle() {
    _turtleLines.value = emptyList()
    _turtleState.value = TurtleState()
  }

  // --- QUIZ METHODS ---
  fun selectAnswer(questionId: String, optionIndex: Int) {
    val map = _selectedAnswers.value.toMutableMap()
    map[questionId] = optionIndex
    _selectedAnswers.value = map
  }

  fun submitQuiz(question: QuizQuestion) {
    val submitted = _submittedQuizzes.value.toMutableSet()
    submitted.add(question.id)
    _submittedQuizzes.value = submitted

    val selected = _selectedAnswers.value[question.id]
    if (selected == question.correctIndex) {
      val curr = _userProgress.value
      val updated = curr.copy(quizScore = curr.quizScore + 10)
      _userProgress.value = updated
      viewModelScope.launch {
        userProgressDao.saveUserProgress(updated)
      }
    }
  }

  // --- LESSON PROGRESS ---
  fun toggleLessonCompleted(weekNumber: Int) {
    val curr = _userProgress.value
    val list = curr.completedWeeksCsv.split(",").filter { it.isNotBlank() }.toMutableSet()
    val key = weekNumber.toString()
    if (list.contains(key)) {
      list.remove(key)
    } else {
      list.add(key)
    }
    val updated = curr.copy(completedWeeksCsv = list.joinToString(","))
    _userProgress.value = updated
    viewModelScope.launch {
      userProgressDao.saveUserProgress(updated)
    }
  }

  fun updateStudentName(name: String) {
    val curr = _userProgress.value
    val updated = curr.copy(studentName = name)
    _userProgress.value = updated
    viewModelScope.launch {
      userProgressDao.saveUserProgress(updated)
    }
  }

  fun resetAllProgress() {
    viewModelScope.launch {
      val blank = UserProgressEntity(
          id = 1,
          studentName = "Python Student",
          completedWeeksCsv = "",
          completedQuizzesCsv = "",
          quizScore = 0,
          labRunsCount = 0
      )
      _userProgress.value = blank
      userProgressDao.saveUserProgress(blank)
      _selectedAnswers.value = emptyMap()
      _submittedQuizzes.value = emptySet()
    }
  }

  private fun loadUserProgress() {
    viewModelScope.launch {
      val progress = userProgressDao.getUserProgressOnce()
      if (progress != null) {
        _userProgress.value = progress
      } else {
        val initial = UserProgressEntity()
        userProgressDao.saveUserProgress(initial)
        _userProgress.value = initial
      }
    }
  }
}
