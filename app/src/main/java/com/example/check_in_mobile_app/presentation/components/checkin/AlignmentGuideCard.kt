package com.example.check_in_mobile_app.presentation.components.checkin

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
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.SubtleText

private val EncryptionGreen = Color(0xFF00C48C)

@Composable
fun AlignmentGuideCard(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // Guide card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Info icon — bordered circle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, BorderLight, RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info_circle),
                    contentDescription = "Info",
                    tint = NavyBlue,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Alignment Guide",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ensure your passport photo page is fully visible within the frame. Avoid glare from overhead lights.",
                    fontSize = 13.sp,
                    color = SubtleText,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Green encryption row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.shield_check),
                contentDescription = "Encrypted",
                tint = EncryptionGreen,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Your biometric data is encrypted and never stored.",
                fontSize = 13.sp,
                color = EncryptionGreen,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
