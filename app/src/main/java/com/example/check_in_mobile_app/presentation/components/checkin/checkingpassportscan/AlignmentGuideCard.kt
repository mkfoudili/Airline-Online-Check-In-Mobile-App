package com.example.check_in_mobile_app.presentation.components.checkin.checkingpassportscan

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.LocalAppColors

private val EncryptionGreen = Color(0xFF00C48C)

@Composable
fun AlignmentGuideCard(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // Guide card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Info icon — bordered circle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info_circle),
                    contentDescription = "Info",
                    tint = LocalAppColors.current.textAccent,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Alignment Guide",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Place passport about 8cm away from phone to ensure the photo page is fully visible within the frame and free of glare or shadows.",
                    fontSize = 13.sp,
                    color = LocalAppColors.current.textSubtle,
                    lineHeight = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

    }
}
