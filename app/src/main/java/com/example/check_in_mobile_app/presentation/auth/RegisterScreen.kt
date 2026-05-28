package com.example.check_in_mobile_app.presentation.auth

import androidx.compose.material3.MaterialTheme

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.check_in_mobile_app.ui.theme.DarkText
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
        if (state.isSuccess) onRegisterSuccess()
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
        RegisterForm(
            modifier = Modifier.padding(paddingValues),
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
                        Log.d("AuthDebug", "Step 1: Building Google ID option")
                        val googleIdOption = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId(GOOGLE_WEB_CLIENT_ID)
                            .setAutoSelectEnabled(false)
                            .build()
                        Log.d("AuthDebug", "Step 2: Google ID option built successfully")

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()
                        Log.d("AuthDebug", "Step 3: Request built successfully")

                        val result = credentialManager.getCredential(
                            context = context,
                            request = request
                        )
                        Log.d("AuthDebug", "Step 4: Got credential result: ${result.credential.type}")

                        val credential = result.credential
                        if (credential is GoogleIdTokenCredential) {
                            Log.d("AuthDebug", "Step 5: Valid GoogleIdTokenCredential, sending to ViewModel")
                            viewModel.signInWithGoogle(credential.idToken)
                        } else {
                            Log.e("AuthDebug", "Step 5 FAILED: Unexpected credential type: ${credential.type}")
                        }

                    } catch (e: GetCredentialException) {
                        Log.e("AuthDebug", "FAILED at GetCredentialException")
                        Log.e("AuthDebug", "Type: ${e.type}")
                        Log.e("AuthDebug", "Message: ${e.message}")
                        Log.e("AuthDebug", "Cause: ${e.cause}")
                        viewModel.setError("Google Sign-Up failed: ${e.message ?: "No accounts found"}")
                    } catch (e: Exception) {
                        Log.e("AuthDebug", "FAILED at unexpected Exception")
                        Log.e("AuthDebug", "Message: ${e.message}")
                        Log.e("AuthDebug", "Cause: ${e.cause}")
                        viewModel.setError("An unexpected error occurred")
                    }
                }
            },
            onSignInClick = onNavigateToLogin
        )
    }
}