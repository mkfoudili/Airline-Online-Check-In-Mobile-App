package com.example.check_in_mobile_app.presentation.checkin.baggage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.example.check_in_mobile_app.ui.theme.*

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
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.chevron_left),
                            contentDescription = stringResource(R.string.common_back),
                            tint = NavyBlue
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.checkin_baggage_step_title),
                        style = Typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyBlue,
                            fontSize = 18.sp
                        )
                    )
                }
                // Progress Line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(LightGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f) // Assuming Step 4 of 5 or similar
                            .fillMaxHeight()
                            .background(NavyBlue)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                iconResId = R.drawable.suitcase, // Using suitcase as placeholder for special equipment
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
