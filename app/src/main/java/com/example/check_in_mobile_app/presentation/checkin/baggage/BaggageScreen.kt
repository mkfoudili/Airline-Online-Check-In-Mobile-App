package com.example.check_in_mobile_app.presentation.checkin.baggage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.PrimaryButton
import com.example.check_in_mobile_app.presentation.components.baggage.BaggageCard
import com.example.check_in_mobile_app.presentation.components.baggage.BaggageNoteCard
import com.example.check_in_mobile_app.presentation.components.baggage.BaggageRulesCard
import com.example.check_in_mobile_app.presentation.components.checkin.CheckInTopBar
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Slate500
import com.example.check_in_mobile_app.ui.theme.Typography

@Composable
fun BaggageScreen(
    viewModel: BaggageViewModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    BaggageContent(
        uiState = uiState,
        onCheckedBaggageChange = viewModel::onCheckedBaggageChange,
        onSpecialEquipmentChange = viewModel::onSpecialEquipmentChange,
        onBackClick = onBackClick,
        onContinueClick = { viewModel.onContinueClick(onContinueClick) }
    )
}

@Composable
fun BaggageContent(
    uiState: BaggageUiState,
    onCheckedBaggageChange: (Int) -> Unit,
    onSpecialEquipmentChange: (Int) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.White,
        topBar = {
            CheckInTopBar(
                onBack = onBackClick,
                currentStep = 4,
                title = stringResource(R.string.checkin_baggage_step_title)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.checkin_baggage_declaration),
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.checkin_baggage_description),
                style = Typography.bodyLarge.copy(
                    color = Slate500,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            BaggageCard(
                iconResId = R.drawable.suitcase,
                title = stringResource(R.string.checkin_checked_baggage),
                description = stringResource(R.string.checkin_checked_baggage_desc),
                quantity = uiState.checkedBaggageCount,
                onIncrement = { onCheckedBaggageChange(uiState.checkedBaggageCount + 1) },
                onDecrement = { onCheckedBaggageChange((uiState.checkedBaggageCount - 1).coerceAtLeast(0)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BaggageCard(
                iconResId = R.drawable.suitcase,
                title = stringResource(R.string.checkin_special_equipment),
                description = stringResource(R.string.checkin_special_equipment_desc),
                quantity = uiState.specialEquipmentCount,
                onIncrement = { onSpecialEquipmentChange(uiState.specialEquipmentCount + 1) },
                onDecrement = { onSpecialEquipmentChange((uiState.specialEquipmentCount - 1).coerceAtLeast(0)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            BaggageRulesCard()

            Spacer(modifier = Modifier.height(24.dp))

            BaggageNoteCard()

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = stringResource(R.string.checkin_continue_to_step_5),
                onClick = onContinueClick,
                containerColor = NavyBlue,
                contentColor = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaggageScreenPreview() {
    CheckInMobileAppTheme {
        BaggageContent(
            uiState = BaggageUiState(
                checkedBaggageCount = 1,
                specialEquipmentCount = 0
            ),
            onCheckedBaggageChange = {},
            onSpecialEquipmentChange = {},
            onBackClick = {},
            onContinueClick = {}
        )
    }
}