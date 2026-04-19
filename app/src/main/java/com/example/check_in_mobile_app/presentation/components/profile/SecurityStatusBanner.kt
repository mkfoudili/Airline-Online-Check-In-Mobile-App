package com.example.check_in_mobile_app.presentation.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.check_in_mobile_app.ui.theme.ActiveGreen
import com.example.check_in_mobile_app.ui.theme.SurfaceGray

@Composable
fun SecurityStatusBanner(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E8F0).copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = ActiveGreen
                )
                // Small check overlay to simulate the "person with checkmark" icon
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(12.dp)
                        .offset(x = 2.dp, y = 2.dp)
                        .background(Color.White, CircleShape)
                        .padding(1.dp),
                    tint = ActiveGreen
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = stringResource(R.string.profile_security_level_high),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = ActiveGreen
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.profile_security_verified_desc),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = ActiveGreen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecurityStatusBannerPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        SecurityStatusBanner()
    }
}
