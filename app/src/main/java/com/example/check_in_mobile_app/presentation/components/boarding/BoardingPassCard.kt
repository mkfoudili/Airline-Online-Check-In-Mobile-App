package com.example.check_in_mobile_app.presentation.components.boarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.checkin.boarding.BoardingUiState
import com.example.check_in_mobile_app.ui.theme.BorderColor
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.QrBorder
import com.example.check_in_mobile_app.ui.theme.SubtleText

@Composable
fun BoardingPassCard(uiState: BoardingUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(NavyBlue)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(painterResource(R.drawable.diamond), null, tint = Color.White)
                    Text(
                        text = stringResource(R.string.boarding_airline),
                        fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White,
                        letterSpacing = 0.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                Text("FLIGHT ${uiState.flightNumber}", fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f), maxLines = 1)
            }
        }

        Box(modifier = Modifier.fillMaxWidth().background(Color.White)
            .padding(horizontal = 20.dp, vertical = 24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(uiState.departureCode, fontSize = 40.sp, fontWeight = FontWeight.Bold,
                        color = NavyBlue, lineHeight = 42.sp, maxLines = 1)
                    Text(uiState.departureCity.uppercase(), fontSize = 11.sp, color = SubtleText,
                        letterSpacing = 0.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Canvas(Modifier.weight(1f).height(8.dp)) {
                            drawLine(Color(0xFFCBD5E1), Offset(0f, size.height / 2),
                                Offset(size.width, size.height / 2), 5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)))
                        }
                        Icon(painterResource(R.drawable.plane2), null)
                        Canvas(Modifier.weight(1f).height(8.dp)) {
                            drawLine(Color(0xFFCBD5E1), Offset(0f, size.height / 2),
                                Offset(size.width, size.height / 2), 5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)))
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(stringResource(R.string.boarding_duration), fontSize = 11.sp,
                        color = SubtleText, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(uiState.arrivalCode, fontSize = 40.sp, fontWeight = FontWeight.Bold,
                        color = NavyBlue, lineHeight = 42.sp, textAlign = TextAlign.End, maxLines = 1)
                    Text(uiState.arrivalCity.uppercase(), fontSize = 11.sp, color = SubtleText,
                        letterSpacing = 0.5.sp, textAlign = TextAlign.End, maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }
            }
        }

        Canvas(Modifier.fillMaxWidth().height(8.dp).padding(20.dp, 0.dp)) {
            drawLine(Color(0xFFCBD5E1), Offset(0f, size.height / 2),
                Offset(size.width, size.height / 2), 5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)))
        }

        Column(modifier = Modifier.fillMaxWidth().background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp)) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LabelValueCell(stringResource(R.string.boarding_gate_label), uiState.gate)
                LabelValueCell(stringResource(R.string.boarding_seat_label), uiState.seat, TextAlign.End)
            }
            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LabelValueCell(stringResource(R.string.boarding_boarding_label), uiState.boardingTime)
                LabelValueCell(stringResource(R.string.boarding_zone_label),
                    stringResource(R.string.boarding_zone_value), TextAlign.End)
            }
            Spacer(Modifier.height(24.dp))

            QrCodeBox(bitmap = uiState.qrBitmap)

            Spacer(Modifier.height(50.dp))
        }

        HorizontalDivider(color = BorderLight, thickness = 1.dp)

        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFF8FAFC))
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.boarding_passenger_label), fontSize = 12.sp,
                    color = SubtleText, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp, maxLines = 1)
                Spacer(Modifier.height(2.dp))
                Text(uiState.passengerName, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    color = DarkText, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)) {
                Text(stringResource(R.string.boarding_economy_plus), fontSize = 12.sp,
                    fontWeight = FontWeight.Bold, color = Color(0xFF2A3343),
                    letterSpacing = 0.5.sp, maxLines = 1)
            }
        }
    }
}

@Composable
private fun QrCodeBox(bitmap: ImageBitmap?) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier.size(192.dp).clip(RoundedCornerShape(16.dp))
                .border(1.dp, QrBorder, RoundedCornerShape(16.dp)).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(bitmap = bitmap, contentDescription = null, modifier = Modifier.size(160.dp))
            } else {
                Image(painterResource(R.drawable.barcode_qr), contentDescription = null)
            }
        }
    }
}

@Composable
private fun LabelValueCell(label: String, value: String, align: TextAlign = TextAlign.Start) {
    Column(horizontalAlignment = if (align == TextAlign.End) Alignment.End else Alignment.Start) {
        Text(label, fontSize = 10.sp, color = SubtleText, fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp, textAlign = align, maxLines = 1)
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = DarkText,
            textAlign = align, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}