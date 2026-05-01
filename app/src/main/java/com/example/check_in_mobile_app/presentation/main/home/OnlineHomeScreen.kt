package com.example.check_in_mobile_app.presentation.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.BookingInputField
import com.example.check_in_mobile_app.presentation.components.PrimaryButton
import com.example.check_in_mobile_app.presentation.components.home.ActiveFlightCard
import com.example.check_in_mobile_app.ui.theme.*

@Composable
fun OnlineHomeScreen(
    uiState: HomeUiState,
    onBookingReferenceChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onCheckInClick: () -> Unit = {},
    onFindFlightClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
) {
    Spacer(modifier = Modifier.height(24.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.home_greeting, uiState.userName),
            fontSize = 25.sp,
            color = DarkText,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(LightGray)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = MediumGray,
                modifier = Modifier.size(22.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
    Box(
        modifier = Modifier
            .requiredWidth(screenWidth)
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
        text = stringResource(R.string.home_retrieve_booking),
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = NavyBlue,
        letterSpacing = (-0.2).sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
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
            label = stringResource(R.string.home_booking_reference),
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
            label = stringResource(R.string.home_last_name),
            value = uiState.lastName,
            placeholder = stringResource(R.string.home_last_name_hint),
            onValueChange = onLastNameChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        PrimaryButton(
            text = stringResource(R.string.home_find_flight),
            onClick = onFindFlightClick
        )
    }
}