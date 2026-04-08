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
                            contentDescription = "Back",
                            tint = NavyBlue
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Step 4: Baggage",
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
                text = "Baggage Declaration",
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    fontSize = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Select the number of bags you plan to check in for your flight.",
                style = Typography.bodyLarge.copy(
                    color = Slate500,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            BaggageCard(
                iconResId = R.drawable.suitcase,
                title = "Checked Baggage",
                description = "Large suitcases or bags stored in the aircraft hold.",
                quantity = uiState.checkedBaggageCount,
                onIncrement = { onCheckedBaggageChange(uiState.checkedBaggageCount + 1) },
                onDecrement = { onCheckedBaggageChange((uiState.checkedBaggageCount - 1).coerceAtLeast(0)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BaggageCard(
                iconResId = R.drawable.suitcase, // Using suitcase as placeholder for special equipment
                title = "Special Equipment",
                description = "Bicycles, skis, or oversized sports gear.",
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
                text = "Continue to Step 5",
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