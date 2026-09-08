package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.MoreSubDestination
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun CertificateScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val userProgress by viewModel.userProgress.collectAsState()
  val completedWeeks = userProgress.completedWeeksCsv.split(",").filter { it.isNotBlank() }.toSet()
  var studentNameInput by remember(userProgress.studentName) { mutableStateOf(userProgress.studentName) }

  Column(
      modifier = modifier
          .fillMaxSize()
          .testTag("certificate_screen")
  ) {
    // Top Bar
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
      Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = { viewModel.navigateToMoreSub(MoreSubDestination.MENU) }) {
          Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column {
          Text(
              text = "Certificate of Completion",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
          )
          Text(
              text = "Official tirOtir AI Academy Credential",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Student Name Editor
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Recipient Full Name",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                  value = studentNameInput,
                  onValueChange = {
                    studentNameInput = it
                    viewModel.updateStudentName(it)
                  },
                  label = { Text("Enter your name for the diploma") },
                  modifier = Modifier.weight(1f),
                  singleLine = true
              )
            }
          }
        }
      }

      // Certificate View Card
      item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("official_certificate_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(2.dp, PythonYellow.copy(alpha = 0.6f))
        ) {
          Box(modifier = Modifier.fillMaxWidth()) {
            // Ornate border inside
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(BorderStroke(1.dp, PythonYellow.copy(alpha = 0.3f)), RoundedCornerShape(14.dp))
                    .padding(18.dp)
            ) {
              Column(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalAlignment = Alignment.CenterHorizontally
              ) {
                // Academy Emblem
                Surface(
                    shape = CircleShape,
                    color = PythonBlue.copy(alpha = 0.2f),
                    border = BorderStroke(2.dp, PythonYellow),
                    modifier = Modifier.size(54.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = PythonYellow,
                        modifier = Modifier.size(32.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "tirOtir AI Academy",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Andisheh Pardazan Anzan Institute",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "Technical & Vocational Training Organization (TVTO)",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(
                    color = PythonYellow.copy(alpha = 0.4f),
                    modifier = Modifier.width(160.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "CERTIFICATE OF COMPLETION",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PythonYellow,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This credential is proudly awarded to",
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFCBD5E1)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userProgress.studentName.ifBlank { "Python Scholar" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "for demonstrating proficiency and excellence in completing the 16-Week Curriculum of",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Python Programming Mastery & Computational Foundations",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Credentials Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                  Column {
                    Text(
                        text = "VERIFICATION ID",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "TT-TVTO-2026-9842X",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = Color(0xFFCBD5E1)
                    )
                  }

                  // Gold Seal
                  Surface(
                      shape = CircleShape,
                      color = PythonYellow,
                      modifier = Modifier.size(46.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(
                          imageVector = Icons.Default.Verified,
                          contentDescription = "Verified Seal",
                          tint = Color(0xFF0F172A),
                          modifier = Modifier.size(26.dp)
                      )
                    }
                  }

                  Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "ACCREDITATION",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "TVTO Standard",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Achievement Stats Breakdown
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Academic Milestones Breakdown",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Completed Weeks:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${completedWeeks.size} / 16", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PythonYellow)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Accumulated Quiz Score:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${userProgress.quizScore} pts", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF10B981))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Interactive Lab Executions:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${userProgress.labRunsCount} runs", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF38BDF8))
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}
