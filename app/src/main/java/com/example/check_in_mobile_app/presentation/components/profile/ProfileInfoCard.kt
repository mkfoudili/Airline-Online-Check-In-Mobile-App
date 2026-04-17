package com.example.check_in_mobile_app.presentation.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.check_in_mobile_app.ui.theme.BorderLight

@Composable
fun ProfileInfoCard(
    email: String,
    phoneNumber: String,
    onEditEmailClick: () -> Unit,
    onEditPhoneClick: () -> Unit,
    onEditPasswordClick: () -> Unit,
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
            label = "Email Address",
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
            label = "Phone Number",
            value = phoneNumber,
            onClick = onEditPhoneClick
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = BorderLight
        )

        ProfileInfoRow(
            icon = Icons.Outlined.Lock,
            label = "Password",
            value = "••••••••••••",
            onClick = onEditPasswordClick
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
            onEditEmailClick = {},
            onEditPhoneClick = {},
            onEditPasswordClick = {}
        )
    }
}
