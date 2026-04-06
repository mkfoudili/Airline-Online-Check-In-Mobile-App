package com.example.check_in_mobile_app.presentation.welcome

import android.R.color.white
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.PrimaryButton
import com.example.check_in_mobile_app.presentation.components.SecondaryButton
import com.example.check_in_mobile_app.ui.theme.*

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ── Navy hero card ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(NavyBlue)
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Flight logo",
                modifier = Modifier.height(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Title & subtitle ───────────────────────────────────────────
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Ready for your flight?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText111
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Skip the airport queues and manage your entire journey from your pocket. Fast, secure, and entirely digital.",
                fontSize = 14.sp,
                color = MediumGray,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── Feature cards ──────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeatureCard(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.express,
                title = "Express Check-In",
                subtitle = "Scan your passport in seconds."
            )
            FeatureCard(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.check,
                title = "Digital Safety",
                subtitle = "Encrypted travel documents."
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── Buttons ────────────────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            PrimaryButton(
                text = "Get Started  →",
                onClick = onGetStarted
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = "Sign In",
                onClick = onSignIn
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Terms of Service ───────────────────────────────────────────
        Text(
            text = buildAnnotatedString {
                append("By continuing, you agree to our ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = NavyBlue)) {
                    append("Terms of Service")
                }
            },
            fontSize = 12.sp,
            color = MediumGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ── Feature card ───────────────────────────────────────────────────────────
@Composable
private fun FeatureCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    title: String,
    subtitle: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(LightGray)
            .padding(14.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkText111
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = MediumGray,
            lineHeight = 16.sp
        )
    }
}