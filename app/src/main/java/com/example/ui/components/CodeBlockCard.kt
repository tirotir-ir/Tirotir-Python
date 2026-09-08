package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CodeBg
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun CodeBlockCard(
    code: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    onRunInLab: ((String) -> Unit)? = null
) {
  val clipboardManager = LocalClipboardManager.current
  val lines = code.lines()

  Column(
      modifier = modifier
          .fillMaxWidth()
          .background(CodeBg, RoundedCornerShape(14.dp))
          .testTag("code_block_card")
  ) {
    // Header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131A29))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
          text = title ?: "Python Code",
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          color = PythonYellow,
          fontWeight = FontWeight.SemiBold
      )

      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = { clipboardManager.setText(AnnotatedString(code)) },
            modifier = Modifier.size(32.dp)
        ) {
          Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy code",
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(16.dp)
          )
        }

        if (onRunInLab != null) {
          ElevatedButton(
              onClick = { onRunInLab(code) },
              colors = ButtonDefaults.elevatedButtonColors(
                  containerColor = PythonBlue,
                  contentColor = Color.White
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.padding(start = 4.dp).testTag("run_in_lab_btn")
          ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Run in Lab",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // Code lines
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .horizontalScroll(rememberScrollState())
    ) {
      Column(
          modifier = Modifier.padding(end = 12.dp),
          horizontalAlignment = Alignment.End
      ) {
        for (i in 1..lines.size) {
          Text(
              text = "$i",
              color = Color(0xFF475569),
              fontFamily = FontFamily.Monospace,
              fontSize = 12.sp,
              lineHeight = 18.sp
          )
        }
      }

      Text(
          text = code,
          color = Color(0xFFE2E8F0),
          fontFamily = FontFamily.Monospace,
          fontSize = 12.sp,
          lineHeight = 18.sp
      )
    }
  }
}
