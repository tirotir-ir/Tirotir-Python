package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CodeBg
import com.example.ui.theme.CodeComment
import com.example.ui.theme.CodeFunction
import com.example.ui.theme.CodeKeyword
import com.example.ui.theme.CodeNumber
import com.example.ui.theme.CodeString
import com.example.ui.theme.CodeVariable
import com.example.ui.theme.PythonYellow

@Composable
fun PythonCodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
  val lines = code.lines()
  val lineCount = maxOf(lines.size, 1)

  Column(
      modifier = modifier
          .fillMaxWidth()
          .clipToBounds()
          .background(CodeBg, RoundedCornerShape(16.dp))
          .testTag("python_code_editor")
  ) {
    // Top bar for editor
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131B2E))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(10.dp)
                .background(Color(0xFFEF4444), RoundedCornerShape(5.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(10.dp)
                .background(Color(0xFFF59E0B), RoundedCornerShape(5.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(10.dp)
                .background(Color(0xFF10B981), RoundedCornerShape(5.dp))
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Text(
          text = "main.py",
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          color = Color(0xFF94A3B8),
          fontWeight = FontWeight.Medium
      )
      Spacer(modifier = Modifier.weight(1f))
      Text(
          text = "$lineCount lines",
          fontSize = 11.sp,
          color = Color(0xFF64748B)
      )
    }

    // Editor Body: Line numbers + Text Field
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 220.dp, max = 340.dp)
            .padding(8.dp)
    ) {
      Row(
          modifier = Modifier.fillMaxSize()
      ) {
        // Line numbers column
        Column(
            modifier = Modifier
                .width(36.dp)
                .padding(top = 2.dp, end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
          for (i in 1..lineCount) {
            Text(
                text = "$i",
                color = Color(0xFF475569),
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
          }
        }

        // Vertical divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color(0xFF1E293B))
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Code Input Area
        BasicTextField(
            value = code,
            onValueChange = onCodeChange,
            readOnly = readOnly,
            textStyle = TextStyle(
                color = Color(0xFFE2E8F0),
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                lineHeight = 20.sp
            ),
            cursorBrush = SolidColor(PythonYellow),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = 2.dp)
                .testTag("code_input_field")
        )
      }
    }

    // Quick Symbol Toolbar
    if (!readOnly) {
      QuickSymbolBar(
          onInsert = { symbol ->
            onCodeChange(code + symbol)
          }
      )
    }
  }
}

@Composable
fun QuickSymbolBar(
    onInsert: (String) -> Unit,
    modifier: Modifier = Modifier
) {
  val symbols = listOf(
      ":", "()", "\"\"", "[]", "{}", "==", "!=", "<=", ">=",
      "def ", "print()", "in ", "for ", "if ", "elif ", "else:",
      "range()", "return ", "import ", "#izeh"
  )

  Surface(
      color = Color(0xFF101726),
      modifier = modifier.fillMaxWidth()
  ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
          text = "SNIPPETS:",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF64748B),
          modifier = Modifier.padding(end = 6.dp)
      )
      symbols.forEach { sym ->
        ElevatedButton(
            onClick = { onInsert(sym) },
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .height(34.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xFF1E293B),
                contentColor = PythonYellow
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
          Text(
              text = sym,
              fontFamily = FontFamily.Monospace,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}

private fun Modifier.clipToBounds(): Modifier = this
