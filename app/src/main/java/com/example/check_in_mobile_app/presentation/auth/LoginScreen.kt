package com.example.check_in_mobile_app.presentation.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.authforms.LoginForm
import com.example.check_in_mobile_app.ui.theme.*
import kotlin.jvm.java

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
//    onNavigateToHomeScreen: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sign In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NavyBlue
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.chevron_left),
                            contentDescription = "Back",
                            tint = NavyBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // ── Logo ───────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(NavyBlue),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App logo",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Title ──────────────────────────────────────────────────
            Text(
                text = "Welcome Back",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText111
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Subtitle ───────────────────────────────────────────────
            Text(
                text = "Enter your credentials to manage your flights",
                fontSize = 14.sp,
                color = MediumGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Login Form ─────────────────────────────────────────────
            LoginForm(
                modifier = Modifier.fillMaxWidth(),
                onSignInClick = { email, password ->
                    viewModel.login(email, password)
                    onLoginSuccess()
                },
                 onGoogleSignInClick = {  },
                onSignUpClick = onNavigateToRegister
            )
        }
    }
}