package com.example.data

data class QuizQuestion(
    val id: String,
    val weekNumber: Int,
    val milestone: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val codeSnippet: String? = null
)

object QuizData {
  val questions: List<QuizQuestion> = listOf(
      QuizQuestion(
          id = "q1",
          weekNumber = 1,
          milestone = "Week 1: Fundamentals",
          question = "What function is used to display output to the standard console in Python?",
          options = listOf("echo()", "console.log()", "print()", "System.out.println()"),
          correctIndex = 2,
          explanation = "In Python, the built-in print() function evaluates expressions and writes them to the standard output stream."
      ),
      QuizQuestion(
          id = "q2",
          weekNumber = 2,
          milestone = "Week 2: Data Types",
          question = "What is the return type of the input() function in Python 3?",
          options = listOf("int", "str (string)", "dynamic", "None"),
          correctIndex = 1,
          explanation = "input() always returns a string (str). To use it as a number, you must cast it explicitly using int() or float()."
      ),
      QuizQuestion(
          id = "q3",
          weekNumber = 3,
          milestone = "Week 3: Operators",
          question = "What is the result of 17 // 4 in Python?",
          options = listOf("4.25", "4", "1", "16"),
          correctIndex = 1,
          explanation = "The // operator performs floor division, rounding down to the nearest integer (17 // 4 = 4)."
      ),
      QuizQuestion(
          id = "q4",
          weekNumber = 4,
          milestone = "Week 4: Conditionals",
          question = "Which keyword is used in Python for 'else if' conditions?",
          options = listOf("elseif", "else if", "elif", "case"),
          correctIndex = 2,
          explanation = "Python uses 'elif' to chain alternative conditional branches."
      ),
      QuizQuestion(
          id = "q5",
          weekNumber = 5,
          milestone = "Week 5: Loops",
          question = "What values does range(1, 6, 2) generate?",
          options = listOf("[1, 2, 3, 4, 5, 6]", "[1, 3, 5]", "[1, 3, 5, 7]", "[2, 4, 6]"),
          correctIndex = 1,
          explanation = "range(start=1, stop=6, step=2) begins at 1 and steps by 2 while less than 6, producing 1, 3, and 5."
      ),
      QuizQuestion(
          id = "q6",
          weekNumber = 6,
          milestone = "Week 6: Lists",
          question = "How do you add an element to the end of a list in Python?",
          options = listOf("list.push(item)", "list.add(item)", "list.append(item)", "list.insert_end(item)"),
          correctIndex = 2,
          explanation = "The .append() method appends an element directly to the end of an existing list."
      ),
      QuizQuestion(
          id = "q7",
          weekNumber = 7,
          milestone = "Week 7: Tuples & Sets",
          question = "What is the primary difference between a list and a tuple?",
          options = listOf(
              "Tuples cannot hold strings",
              "Tuples are immutable (cannot be changed once created)",
              "Lists cannot be sliced",
              "Tuples require curly braces"
          ),
          correctIndex = 1,
          explanation = "Tuples are immutable sequences, meaning their elements cannot be added, removed, or modified after instantiation."
      ),
      QuizQuestion(
          id = "q8",
          weekNumber = 8,
          milestone = "Week 8: Dictionaries",
          question = "How do you safely access a key in a dictionary without risking a KeyError if it is missing?",
          options = listOf("dict.try_get(key)", "dict.get(key, default)", "dict[key]?", "dict.find(key)"),
          correctIndex = 1,
          explanation = "The .get(key, default) method returns the value if the key exists, or a fallback default without raising KeyError."
      ),
      QuizQuestion(
          id = "q9",
          weekNumber = 9,
          milestone = "Week 9: Functions",
          question = "What keyword is used to define a function in Python?",
          options = listOf("function", "fn", "fun", "def"),
          correctIndex = 3,
          explanation = "Functions are declared using the 'def' keyword followed by the function identifier and parameters."
      ),
      QuizQuestion(
          id = "q10",
          weekNumber = 10,
          milestone = "Week 10: Lambdas",
          question = "What is a lambda function in Python?",
          options = listOf(
              "A multi-threaded background process",
              "An anonymous small function defined with the lambda keyword",
              "A variable that cannot be reassigned",
              "A class decorator"
          ),
          correctIndex = 1,
          explanation = "A lambda function is an inline, anonymous single-expression function (e.g., lambda x: x * 2)."
      ),
      QuizQuestion(
          id = "q11",
          weekNumber = 11,
          milestone = "Week 11: File I/O",
          question = "Which file mode appends new content to the end of a file without overwriting it?",
          options = listOf("'r'", "'w'", "'a'", "'x'"),
          correctIndex = 2,
          explanation = "Mode 'a' (append) positions the stream at the end of the file, preserving existing content."
      ),
      QuizQuestion(
          id = "q12",
          weekNumber = 12,
          milestone = "Week 12: Exceptions",
          question = "Which block always executes regardless of whether an exception was raised or handled?",
          options = listOf("except", "finally", "catch", "always"),
          correctIndex = 1,
          explanation = "The 'finally' block is guaranteed to execute whether an exception occurred or not, perfect for cleanup."
      ),
      QuizQuestion(
          id = "q13",
          weekNumber = 13,
          milestone = "Week 13: OOP",
          question = "What is the role of the __init__ method in a Python class?",
          options = listOf(
              "To destroy the object",
              "To initialize new instance attributes upon creation",
              "To inherit from parent classes",
              "To import external libraries"
          ),
          correctIndex = 1,
          explanation = "__init__ is the class constructor method invoked automatically when a new instance is instantiated."
      ),
      QuizQuestion(
          id = "q14",
          weekNumber = 14,
          milestone = "Week 14: Inheritance",
          question = "How does a child class invoke the constructor of its parent class?",
          options = listOf("parent.__init__()", "super().__init__()", "base.init()", "this.parent()"),
          correctIndex = 1,
          explanation = "super().__init__() delegates initialization to the parent superclass."
      ),
      QuizQuestion(
          id = "q15",
          weekNumber = 15,
          milestone = "Week 15: Modules",
          question = "In Python's turtle module, how many degrees must you turn right 5 times to draw a regular 5-pointed star?",
          options = listOf("72 degrees", "90 degrees", "144 degrees", "120 degrees"),
          correctIndex = 2,
          explanation = "A classic 5-pointed star requires turning 144 degrees (180 - 36) at each vertex point."
      ),
      QuizQuestion(
          id = "q16",
          weekNumber = 16,
          milestone = "Week 16: Packaging",
          question = "Which PyInstaller option bundles all dependencies into a single standalone executable file?",
          options = listOf("--single", "--onefile", "--bundle-all", "--compact"),
          correctIndex = 1,
          explanation = "The --onefile option packages the Python runtime and dependencies into a single portable binary."
      )
  )
}
