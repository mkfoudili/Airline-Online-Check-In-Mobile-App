package com.example.check_in_mobile_app.presentation.home

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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.presentation.components.BookingInputField
import com.example.check_in_mobile_app.presentation.components.PrimaryButton
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.DividerColor
import com.example.check_in_mobile_app.ui.theme.LightGray
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.presentation.components.home.ActiveFlightCard

@Composable
fun OnlineHomeScreen(
    uiState : HomeUiState,
    onBookingReferenceChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onCheckInClick: () -> Unit = {},
    onFindFlightClick: () -> Unit = {},
    screenWidth : Dp = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
) {
    Spacer(modifier = Modifier.height(24.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hello, ${uiState.userName}",
            fontSize = 25.sp,
            color = DarkText,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        )

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Profile",
                tint = MediumGray,
                modifier = Modifier.size(22.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
    Box(
        modifier = Modifier
            .requiredWidth(screenWidth)
            .requiredWidth(screenWidth) // Extend beyond screen width for full bleed effect
            .height(1.dp)
            .background(DividerColor)
    )
    Spacer(modifier = Modifier.height(24.dp))

    ActiveFlightCard(
        destination = uiState.flightDestination,
        onCheckInClick = onCheckInClick,
        uiState = uiState
    )

    Spacer(modifier = Modifier.height(28.dp))

    Text(
        text = "Retrieve Booking",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = NavyBlue,
        letterSpacing = (-0.2).sp
    )

    Spacer(modifier = Modifier.height(18.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        BookingInputField(
            label = "BOOKING REFERENCE",
            value = uiState.bookingReference,
            placeholder = "e.g. AB1234",
            onValueChange = onBookingReferenceChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = MediumGray,
                    modifier = Modifier.size(18.dp)
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                keyboardType = KeyboardType.Text
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        BookingInputField(
            label = "LAST NAME",
            value = uiState.lastName,
            placeholder = "As written in passport",
            onValueChange = onLastNameChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        PrimaryButton(
            text = "Find My Flight",
            onClick = onFindFlightClick,
        )
    }
}