package com.example.check_in_mobile_app.presentation.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.authforms.RegisterForm
import com.example.check_in_mobile_app.ui.theme.*
import com.example.check_in_mobile_app.presentation.utils.toUserFriendlyMessage
import com.example.data.remote.GOOGLE_WEB_CLIENT_ID
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess){
            viewModel.registerFcmTokenAfterAuth()
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = androidx.compose.ui.res.stringResource(R.string.auth_create_account),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = ErrorRed,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = NavyBlue,
                    trackColor = LightGray
                )
            }

            RegisterForm(
                modifier = Modifier.weight(1f),
                onCreateAccountClick = { name, email, phone, pass ->
                    viewModel.register(name, email, phone, pass)
                },
                onGoogleSignUpClick = {
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(GOOGLE_WEB_CLIENT_ID)
                        .setAutoSelectEnabled(false)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    scope.launch {
                        try {
                            val result = credentialManager.getCredential(
                                context = context,
                                request = request
                            )
                            val credential = result.credential
                            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                try {
                                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                    viewModel.signInWithGoogle(googleIdTokenCredential.idToken)
                                } catch (e: Exception) {
                                    viewModel.setError("Failed to parse Google credential")
                                }
                            } else {
                                viewModel.setError("Authentication method not supported.")
                            }
                        } catch (e: GetCredentialException) {
                            viewModel.setError("No Google account selected or sign-in was canceled.")
                        } catch (e: Exception) {
                            viewModel.setError(e.toUserFriendlyMessage())
                        }
                    }
                },
                onSignInClick = onNavigateToLogin
            )
        }
    }
}