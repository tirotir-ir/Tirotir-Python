package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun AcademyHeaderBanner(
    modifier: Modifier = Modifier,
    onExploreSyllabus: () -> Unit = {}
) {
  Card(
      modifier = modifier
          .fillMaxWidth()
          .testTag("academy_header_banner"),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
      // Background Image
      Image(
          painter = painterResource(id = R.drawable.academy_hero_banner),
          contentDescription = "tirOtir AI Academy Headquarters & Tech Campus",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
      )

      // Gradient overlay for contrast and legibility
      Box(
          modifier = Modifier
              .fillMaxSize()
              .background(
                  Brush.verticalGradient(
                      colors = listOf(
                          Color.Black.copy(alpha = 0.35f),
                          Color(0xFF0F172A).copy(alpha = 0.92f)
                      )
                  )
              )
      )

      // Content inside banner
      Column(
          modifier = Modifier
              .fillMaxSize()
              .padding(20.dp),
          verticalArrangement = Arrangement.SpaceBetween
      ) {
        // Top badge row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
              shape = RoundedCornerShape(12.dp),
              color = PythonBlue.copy(alpha = 0.9f)
          ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                  imageVector = Icons.Default.School,
                  contentDescription = "TVTO Accreditation",
                  tint = PythonYellow,
                  modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                  text = "TVTO ACCREDITED",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  letterSpacing = 0.8.sp
              )
            }
          }

          Surface(
              shape = CircleShape,
              color = Color(0x33FFFFFF)
          ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                  imageVector = Icons.Default.Verified,
                  contentDescription = null,
                  tint = Color(0xFF38BDF8),
                  modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                  text = "16-WEEK DIPLOMA",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color.White
              )
            }
          }
        }

        // Bottom title & credentials
        Column {
          Text(
              text = "tirOtir AI Academy",
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              letterSpacing = (-0.5).sp
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
              text = "Andisheh Pardazan Anzan Institute • Technical & Vocational Training Organization",
              fontSize = 12.sp,
              color = Color(0xFFCBD5E1),
              fontWeight = FontWeight.Normal,
              lineHeight = 16.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(
              verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = PythonYellow
            ) {
              Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Python 3.12 Standard",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
              }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Interactive IDE & Visual Lab",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
          }
        }
      }
    }
  }
}
