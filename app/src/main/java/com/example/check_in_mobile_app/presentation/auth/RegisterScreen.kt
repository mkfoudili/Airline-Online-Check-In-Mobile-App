package com.example.check_in_mobile_app.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.authforms.RegisterForm
import com.example.check_in_mobile_app.ui.theme.DarkText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onRegisterSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.chevron_left),
                            contentDescription = "Back",
                            tint = DarkText,
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
        RegisterForm(
            modifier = Modifier.padding(paddingValues),
            onCreateAccountClick = { name, email, phone, pass ->
                viewModel.register(name, email, phone, pass)
            },
            onGoogleSignUpClick = { /* TODO */ },
            onSignInClick = onNavigateToLogin
        )
    }
}