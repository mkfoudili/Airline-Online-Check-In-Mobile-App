package com.example.check_in_mobile_app.presentation.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.SubtleText

@Composable
fun ProfileInfoCard(
    email: String,
    phoneNumber: String,
    language: String,
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onEditEmailClick: () -> Unit,
    onEditPhoneClick: () -> Unit,
    onEditPasswordClick: () -> Unit,
    onEditLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
    ) {
        ProfileInfoRow(
            icon = Icons.Outlined.Email,
            label = stringResource(R.string.profile_label_email),
            value = email,
            onClick = onEditEmailClick
        )
        
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = BorderLight
        )

        ProfileInfoRow(
            icon = Icons.Outlined.Phone,
            label = stringResource(R.string.profile_label_phone),
            value = phoneNumber,
            onClick = onEditPhoneClick
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = BorderLight
        )

        ProfileInfoRow(
            icon = Icons.Outlined.Language,
            label = stringResource(R.string.profile_label_language),
            value = when (language) {
                "English" -> stringResource(R.string.language_english)
                "French" -> stringResource(R.string.language_french)
                "Arabic" -> stringResource(R.string.language_arabic)
                else -> language
            },
            onClick = onEditLanguageClick
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = BorderLight
        )

        ProfileThemeRow(
            isDarkMode = isDarkMode,
            onThemeToggle = onThemeToggle
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = BorderLight
        )

        ProfileInfoRow(
            icon = Icons.Outlined.Lock,
            label = stringResource(R.string.profile_label_password),
            value = "••••••••••••",
            onClick = onEditPasswordClick
        )
    }
}

@Composable
private fun ProfileThemeRow(
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon in circle
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, BorderLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = NavyBlue
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Label and Value
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.profile_theme_label).uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SubtleText,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (isDarkMode) stringResource(R.string.theme_dark) else stringResource(R.string.theme_light),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
        }

        // Switch
        Switch(
            checked = isDarkMode,
            onCheckedChange = onThemeToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NavyBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BorderLight,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileInfoCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ProfileInfoCard(
            email = "mr_mekircha@esi.dz",
            phoneNumber = "+1 (555) 012-3456",
            language = "English",
            isDarkMode = false,
            onThemeToggle = {},
            onEditEmailClick = {},
            onEditPhoneClick = {},
            onEditPasswordClick = {},
            onEditLanguageClick = {}
        )
    }
}
