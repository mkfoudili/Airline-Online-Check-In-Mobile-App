package com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails

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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    text = "DOCUMENT DETAILS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            // ── Rows ─────────────────────────────────────────────────────────
            InfoRow(
                iconRes = R.drawable.checking_review_passport,
                iconBgColor = LightGray,
                label = "PASSPORT NUMBER",
                initialValue = passenger.passportNumber ?: "—"
            )
            HorizontalDivider(color = Color(0xFFF1F2F4), thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checkin_review_globe,
                iconBgColor =  LightGray,
                label = "NATIONALITY",
                initialValue = passenger.nationality ?: "—"
            )
            HorizontalDivider(color = Color(0xFFF1F2F4), thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checkin_review_calendar,
                iconBgColor =  LightGray,
                label = "DATE OF BIRTH",
                initialValue = passenger.dateOfBirth ?: "—"
            )
            HorizontalDivider(color = Color(0xFFF1F2F4), thickness = 1.dp)

            InfoRow(
                iconRes = R.drawable.checking_review_warning,
                iconBgColor =  LightGray,
                label = "EXPIRY DATE",
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
    initialValue: String,
    onValueSaved: (String) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    var textValue by remember(initialValue) { mutableStateOf(initialValue) }

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
            if (isEditing) {
                BasicTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    singleLine = true
                )
            } else {
                Text(
                    text = textValue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))


        IconButton(
            onClick = {
                if (isEditing) {
                    onValueSaved(textValue)
                }
                isEditing = !isEditing
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Check else Icons.Outlined.Edit,
                contentDescription = if (isEditing) "Save" else "Edit",
                tint = if (isEditing) NavyBlue else CoolGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PassengerInfoCardPreview() {
    val previewPassenger = com.example.data.repository.CheckInRepositoryImpl()
        .getPassengerForReview()
    Box(modifier = Modifier.padding(16.dp)) {
        PassengerInfoCard(passenger = previewPassenger)
    }
}
