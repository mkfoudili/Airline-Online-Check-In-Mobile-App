package com.example.check_in_mobile_app.presentation.components.authforms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.PrimaryButton
import com.example.check_in_mobile_app.presentation.components.LanguageSwitcherLinks
import com.example.check_in_mobile_app.ui.theme.*

@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    onCreateAccountClick: (
        fullName: String,
        email: String,
        phone: String,
        password: String
    ) -> Unit = { _, _, _, _ -> },
    onGoogleSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {}
) {
    var fullName        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible        by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val passwordMismatch = confirmPassword.isNotEmpty() && password != confirmPassword
    val passwordTooShort = password.isNotEmpty() && password.length < 8

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        // ── Full Name ──────────────────────────────────────────────────
        FieldLabel(androidx.compose.ui.res.stringResource(R.string.common_full_name))
        AuthTextField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = androidx.compose.ui.res.stringResource(R.string.profile_full_name_placeholder),
            leadingIconRes = R.drawable.profile,
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Email Address ──────────────────────────────────────────────
        FieldLabel(androidx.compose.ui.res.stringResource(R.string.common_email_address))
        AuthTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = androidx.compose.ui.res.stringResource(R.string.profile_email_placeholder),
            leadingIconRes = R.drawable.mail,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Phone Number ───────────────────────────────────────────────
        FieldLabel(androidx.compose.ui.res.stringResource(R.string.common_phone_number))
        AuthTextField(
            value = phone,
            onValueChange = { phone = it },
            placeholder = androidx.compose.ui.res.stringResource(R.string.profile_phone_placeholder),
            leadingIconRes = R.drawable.phone,
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Password ───────────────────────────────────────────────────
        FieldLabel(androidx.compose.ui.res.stringResource(R.string.common_password))
        AuthTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = androidx.compose.ui.res.stringResource(R.string.profile_new_password_placeholder),
            leadingIconRes = R.drawable.lock,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            passwordVisible = passwordVisible,
            onTogglePassword = { passwordVisible = !passwordVisible },
            isError = passwordTooShort
        )
        if (passwordTooShort) {
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_password_hint),
                color = ErrorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        } else {
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_password_hint),
                color = MediumGray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Confirm Password ───────────────────────────────────────────
        FieldLabel(androidx.compose.ui.res.stringResource(R.string.profile_confirm_password_label))
        AuthTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = androidx.compose.ui.res.stringResource(R.string.profile_confirm_password_placeholder),
            leadingIconRes = R.drawable.lock,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            passwordVisible = confirmPasswordVisible,
            onTogglePassword = { confirmPasswordVisible = !confirmPasswordVisible },
            isError = passwordMismatch
        )
        if (passwordMismatch) {
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_password_mismatch),
                color = ErrorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Create Account Button ──────────────────────────────────────
        PrimaryButton(
            text = androidx.compose.ui.res.stringResource(R.string.auth_create_account),
            onClick = {
                if (!passwordTooShort && !passwordMismatch) {
                    onCreateAccountClick(fullName, email, phone, password)
                }
            },
            enabled = fullName.isNotBlank()
                    && email.isNotBlank()
                    && phone.isNotBlank()
                    && password.isNotBlank()
                    && confirmPassword.isNotBlank()
                    && !passwordTooShort
                    && !passwordMismatch
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── OR Divider ─────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
            Text(
                text = "  " + androidx.compose.ui.res.stringResource(R.string.common_or) + "  ",
                color = MediumGray,
                fontSize = 13.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Continue with Google ───────────────────────────────────────
        OutlinedButton(
            onClick = onGoogleSignUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, BorderColor),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_continue_with_google),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Already have an account? Sign In ───────────────────────────
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_already_have_account) + " ",
                fontSize = 14.sp,
                color = MediumGray,
                textAlign = TextAlign.Center
            )
            TextButton(
                onClick = onSignInClick,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.defaultMinSize(minHeight = 1.dp)
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(R.string.auth_sign_in),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue
                )
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        LanguageSwitcherLinks()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ── Reusable field label ───────────────────────────────────────────────────
@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = DarkText,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

// ── Reusable text field ────────────────────────────────────────────────────
@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIconRes: Int,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = MediumGray, fontSize = 14.sp)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIconRes),
                contentDescription = null,
                tint = MediumGray,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = if (isPassword && onTogglePassword != null) {
            {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.seen else R.drawable.unseen
                        ),
                        contentDescription = if (passwordVisible) {
                            androidx.compose.ui.res.stringResource(R.string.common_hide_password)
                        } else {
                            androidx.compose.ui.res.stringResource(R.string.common_show_password)
                        },
                        tint = MediumGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color.Black,
            errorTextColor = Color.Black,
            unfocusedBorderColor = BorderColor,
            focusedBorderColor = NavyBlue,
            errorBorderColor = ErrorRed,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            errorContainerColor = Color.White,
            cursorColor = NavyBlue
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    )
}