package com.example.check_in_mobile_app.presentation.welcome

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    var activeDot by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(500L)
            activeDot = (activeDot + 1) % 3
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBlue),
        contentAlignment = Alignment.Center
    ) {

        // ── Center content ─────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            // Logo icon in rounded square
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App logo",
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App name
            Text(
                text = "Airline Check-In",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // FLIGHT label
            Text(
                text = "FLIGHT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tagline
            Text(
                text = "DIGITAL BOARDING SYSTEM",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )
        }

        // ── Bottom content ─────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {


            Text(
                text = "Synchronizing flight data...",
                fontSize = 13.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.5f),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val size by animateDpAsState(
                        targetValue = if (index == activeDot) 10.dp else 8.dp,
                        animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic),
                        label = "dot_size_$index"
                    )


                    val alpha by animateFloatAsState(
                        targetValue = if (index == activeDot) 1f else 0.4f,
                        animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic),
                        label = "dot_alpha_$index"
                    )

                    Box(
                        modifier = Modifier
                            .size(size)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = alpha))
                    )
                }
            }
        }
    }
}