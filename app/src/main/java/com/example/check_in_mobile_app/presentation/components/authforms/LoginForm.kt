package com.example.check_in_mobile_app.presentation.components.authforms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.PrimaryButton
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.BorderColor
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.SubtleText
import com.example.check_in_mobile_app.presentation.components.LanguageSwitcherLinks


@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    onSignInClick: (email: String, password: String) -> Unit = { _, _ -> },
    onGoogleSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    val primaryColor = NavyBlue
    val borderColor = BorderColor
    val labelColor = DarkText
    val hintColor = MediumGray
    val linkColor = NavyBlue
    val dividerTextColor = SubtleText

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        // ── Email Address ──────────────────────────────────────────────
        Text(
            text = androidx.compose.ui.res.stringResource(R.string.common_email_address),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = labelColor,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    text = "name@example.com",
                    color = hintColor,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mail),
                    contentDescription = "Email icon",
                    tint = hintColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                errorTextColor = Color.Black,
                unfocusedBorderColor = borderColor,
                focusedBorderColor = primaryColor,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = primaryColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Password ───────────────────────────────────────────────────
        Text(
            text = androidx.compose.ui.res.stringResource(R.string.common_password),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = labelColor,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    text = "••••••••",
                    color = hintColor,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "Password icon",
                    tint = hintColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.unseen else R.drawable.seen
                        ),
                        contentDescription = if (passwordVisible) {
                            androidx.compose.ui.res.stringResource(R.string.common_hide_password)
                        } else {
                            androidx.compose.ui.res.stringResource(R.string.common_show_password)
                        },
                        tint = hintColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                errorTextColor = Color.Black,
                unfocusedBorderColor = borderColor,
                focusedBorderColor = primaryColor,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = primaryColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Sign In Button ─────────────────────────────────────────────
        PrimaryButton(
            text = androidx.compose.ui.res.stringResource(R.string.auth_sign_in),
            onClick = { onSignInClick(email, password) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = borderColor
            )
            Text(
                text = "  " + androidx.compose.ui.res.stringResource(R.string.common_or) + "  ",
                color = dividerTextColor,
                fontSize = 13.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = borderColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onGoogleSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, borderColor),
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
                color = labelColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,  // Add this
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_dont_have_account) + " ",
                fontSize = 14.sp,
                color = dividerTextColor,
                textAlign = TextAlign.Center
            )
            TextButton(
                onClick = onSignUpClick,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.defaultMinSize(minHeight = 1.dp)
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(R.string.auth_sign_up),
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
