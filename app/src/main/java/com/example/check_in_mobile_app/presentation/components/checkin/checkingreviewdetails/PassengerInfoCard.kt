package com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.LocalAppColors
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.domain.model.Passenger


@Composable
fun PassengerInfoCard(passenger: Passenger) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            // ── Rows ─────────────────────────────────────────────────────────
            InfoRow(
                iconRes = R.drawable.checking_review_passport,
                label = stringResource(R.string.review_passport_number_label),
                initialValue = passenger.passportNumber ?: "—"
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checkin_review_globe,
                label = stringResource(R.string.review_nationality_label),
                initialValue = passenger.nationality ?: "—"
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checkin_review_calendar,
                label = stringResource(R.string.review_dob_label),
                initialValue = passenger.dateOfBirth ?: "—"
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checking_review_warning,
                label = stringResource(R.string.review_expiry_label),
                initialValue = passenger.expiryDate ?: "—"
            )
        }
    }
}

@Composable
private fun InfoRow(
    iconRes: Int,
    label: String,
    initialValue: String
) {
    val appColors = LocalAppColors.current
    var isEditing by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(initialValue) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coloured icon box
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(appColors.iconBackground),
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
                color = appColors.textSubtle,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(3.dp))

            if (isEditing) {
                BasicTextField(
                    value = value,
                    onValueChange = { value = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = appColors.textPrimary
                    ),
                    cursorBrush = SolidColor(appColors.textAccent),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    appColors.textAccent.copy(alpha = 0.07f),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            innerTextField()
                        }
                    }
                )
            } else {
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = appColors.textPrimary
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Edit / Confirm button
        IconButton(
            onClick = { isEditing = !isEditing },
            modifier = Modifier.size(32.dp)
        ) {
            if (isEditing) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Confirm",
                    tint = appColors.textAccent,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = appColors.textSubtle,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}