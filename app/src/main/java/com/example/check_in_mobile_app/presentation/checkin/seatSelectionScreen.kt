package com.example.check_in_mobile_app.presentation.checkin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.presentation.components.checkin.ProgressBar
import com.example.check_in_mobile_app.presentation.components.checkin.SeatGrid
import com.example.check_in_mobile_app.presentation.components.checkin.SeatLegend
import com.example.check_in_mobile_app.presentation.components.checkin.SeatModel

import androidx.compose.foundation.layout.Row

import androidx.compose.runtime.getValue

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.check_in_mobile_app.presentation.components.checkin.CheckInTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelection(
    onNavigateBack: () -> Unit = {},
    onContinue: () -> Unit = {}
) {

    var selectedSeat by remember { mutableStateOf<SeatModel?>(null) }

    Scaffold(
        topBar = {
            CheckInTopBar(
                onBack = onNavigateBack,
                currentStep = 3,
                title = "Step 3: Choose Seat"
            )
        },
    ) { paddingValues ->
        Column(

            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color(0xFFF9FAFA))
        ) {


            SeatLegend()


            SeatGrid(
                modifier = Modifier.weight(1f).
                background(color = Color(0xFFF9FAFA)),
                selectedSeatId = selectedSeat?.seatId,
                onSeatSelected = { seat ->
                    selectedSeat = seat
                }
            )

            selectedSeat?.let { seat ->
                SelectedSeatBottomBar(
                    seat = seat,
                    onConfirm = onContinue
                )
            }
        }
    }
}

@Composable
fun SelectedSeatBottomBar(
    seat: SeatModel,
    onConfirm: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            text = "Seat ${seat.seatNumber}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NavyBlue
        )

        // "Standard Passenger Seat" or "Premium Seat"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (seat.isPremium)
                        R.drawable.zap
                    else
                        R.drawable.users
                ),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = if (seat.isPremium) "Premium Seat" else "Standard Passenger Seat",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm button
        OutlinedButton(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, NavyBlue)
        ) {
            Text(
                text = "Confirm Seat",
                fontSize = 15.sp,
                color = NavyBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}