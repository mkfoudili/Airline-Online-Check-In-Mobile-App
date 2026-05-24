package com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.CoolGray
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.LightGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.domain.model.Passenger


@Composable
fun PassengerInfoCard(passenger: Passenger) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // ── Header ──────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyBlue)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = stringResource(R.string.review_document_details),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface,
                    letterSpacing = 1.sp
                )
            }

            // ── Rows ─────────────────────────────────────────────────────────
            InfoRow(
                iconRes = R.drawable.checking_review_passport,
                iconBgColor = LightGray,
                label = stringResource(R.string.review_passport_number_label),
                initialValue = passenger.passportNumber ?: "—"
            )
            HorizontalDivider(color = Color(0xFFF1F2F4), thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checkin_review_globe,
                iconBgColor =  LightGray,
                label = stringResource(R.string.review_nationality_label),
                initialValue = passenger.nationality ?: "—"
            )
            HorizontalDivider(color = Color(0xFFF1F2F4), thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checkin_review_calendar,
                iconBgColor =  LightGray,
                label = stringResource(R.string.review_dob_label),
                initialValue = passenger.dateOfBirth ?: "—"
            )
            HorizontalDivider(color = Color(0xFFF1F2F4), thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checking_review_warning,
                iconBgColor =  LightGray,
                label = stringResource(R.string.review_expiry_label),
                initialValue = passenger.expiryDate ?: "—"
            )
        }
    }
}

@Composable
private fun InfoRow(
    iconRes: Int,
    iconBgColor: Color,
    label: String,
    initialValue: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coloured icon box
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF9CA3AF),
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = initialValue,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}


