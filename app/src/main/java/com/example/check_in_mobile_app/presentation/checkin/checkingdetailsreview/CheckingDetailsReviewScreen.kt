package com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.checkin.CheckInTopBar
import com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails.PassengerAvatarRow
import com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails.PassengerInfoCard
import com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails.WarningBanner
import com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails.ConfirmCheckbox
import com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails.ReviewContinueButton
import com.example.check_in_mobile_app.ui.theme.ErrorRed
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.data.repository.CheckInRepositoryImpl
import com.example.domain.model.Passenger

@Composable
fun CheckingDetailsReviewScreen(
    onBack: () -> Unit = {},
    onContinue: () -> Unit = {},
    viewModel: CheckingDetailsReviewViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CheckingDetailsReviewScreenContent(
        passenger = uiState.passenger,
        isConfirmed = uiState.isConfirmed,
        onBack = onBack,
        onContinue = onContinue,
        onConfirmedChanged = viewModel::onConfirmedChanged
    )
}

@Composable
fun CheckingDetailsReviewScreenContent(
    passenger: Passenger,
    isConfirmed: Boolean,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onConfirmedChanged: (Boolean) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CheckInTopBar(
                onBack = onBack,
                currentStep = 2,
                title = "Step 2: Review"
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))


            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Verify Information",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue
                )
                Text(
                    text = "We've extracted these details from your passport scan. " +
                            "Please ensure everything is accurate before proceeding to seat selection.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 20.sp
                )
            }


            PassengerAvatarRow(passenger = passenger)


            PassengerInfoCard(passenger = passenger)


            WarningBanner()


            ConfirmCheckbox(
                isConfirmed = isConfirmed,
                onConfirmedChanged = onConfirmedChanged
            )


            ReviewContinueButton(
                enabled = isConfirmed,
                onClick = onContinue
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckingDetailsReviewScreenPreview() {
    CheckingDetailsReviewScreenContent(
        passenger = CheckInRepositoryImpl().getPassengerForReview(),
        isConfirmed = false,
        onBack = {},
        onContinue = {},
        onConfirmedChanged = {}
    )
}
