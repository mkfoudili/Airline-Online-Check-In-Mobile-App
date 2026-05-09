package com.example.check_in_mobile_app.presentation.main.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import com.example.domain.model.Booking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OnlineHomeScreen(
    uiState: HomeUiState,
    onBookingReferenceChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onCheckInClick: () -> Unit = {},
    onFindFlightClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNavigateToFlightDetails: (String) -> Unit = {},
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Spacer(modifier = Modifier.height(24.dp))

    // Header
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (uiState.userName.isNotBlank())
                stringResource(R.string.home_greeting, uiState.userName)
            else
                stringResource(R.string.home_greeting_default),
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

    // Active flight card
    ActiveFlightCard(
        uiState = uiState,
        onCheckInClick = onCheckInClick
    )

    Spacer(modifier = Modifier.height(28.dp))

    // Section titre recherche
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

    // Formulaire de recherche
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
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
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
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboard?.hide()
                    onFindFlightClick()
                }
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        PrimaryButton(
            text = stringResource(R.string.home_find_flight),
            onClick = {
                keyboard?.hide()
                onFindFlightClick()
            },
            enabled = !uiState.isSearching
        )
    }

    AnimatedVisibility(visible = uiState.isSearching) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = NavyBlue,
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.home_search_loading),
                fontSize = 13.sp,
                color = CoolGray
            )
        }
    }

    AnimatedVisibility(
        visible = uiState.searchError != null && !uiState.isSearching,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        uiState.searchError?.let { error ->
            SearchErrorCard(error = error)
        }
    }

    AnimatedVisibility(
        visible = uiState.searchResult != null && !uiState.isSearching,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        uiState.searchResult?.let { booking ->
            Spacer(modifier = Modifier.height(20.dp))
            SearchResultPreview(
                booking = booking,
                onViewDetails = { onNavigateToFlightDetails(booking.bookingRef) }
            )
        }
    }
}

// Carte d'erreur

@Composable
private fun SearchErrorCard(error: SearchError) {
    val (icon, message) = when (error) {
        is SearchError.NotFound -> Pair(
            R.drawable.info,
            stringResource(R.string.home_search_error_not_found)
        )
        is SearchError.NetworkError -> Pair(
            R.drawable.wifi_off2,
            stringResource(R.string.home_search_error_network)
        )
        is SearchError.EmptyFields -> Pair(
            R.drawable.info,
            stringResource(R.string.home_search_error_empty_fields)
        )
        is SearchError.Unknown -> Pair(
            R.drawable.info,
            stringResource(R.string.home_search_error_generic)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ErrorRed.copy(alpha = 0.08f))
            .border(1.dp, ErrorRed.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = ErrorRed,
            modifier = Modifier.size(18.dp).offset(y = 2.dp)
        )
        Text(
            text = message,
            fontSize = 13.sp,
            color = ErrorRed,
            lineHeight = 18.sp
        )
    }
}

// Preview résultat

@Composable
private fun SearchResultPreview(
    booking: Booking,
    onViewDetails: () -> Unit
) {
    val flight = booking.flight
    val dateFmt = SimpleDateFormat("dd MMM • HH:mm", Locale.getDefault())
    val depDate = dateFmt.format(Date(flight.departureTime))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, NavyBlue.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Titre + badge statut
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home_search_result_title),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = CoolGray
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(NavyBlue.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = booking.pnr,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Route
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = flight.origin,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkText
                )
                Text(
                    text = flight.originCity.ifBlank { flight.origin },
                    fontSize = 12.sp,
                    color = CoolGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.plane),
                contentDescription = null,
                tint = NavyBlue.copy(alpha = 0.5f),
                modifier = Modifier.size(22.dp).weight(1f).wrapContentWidth()
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = flight.destination,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkText
                )
                Text(
                    text = flight.destinationCity.ifBlank { flight.destination },
                    fontSize = 12.sp,
                    color = CoolGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = DividerColor)
        Spacer(modifier = Modifier.height(12.dp))

        // Détails
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoChip(
                label = stringResource(R.string.departure_label),
                value = depDate
            )
            if (booking.passengers.isNotEmpty()) {
                InfoChip(
                    label = stringResource(R.string.passenger_label),
                    value = "${booking.passengers.first().firstName} ${booking.passengers.first().lastName}"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton vers FlightDetails
        Button(
            onClick = onViewDetails,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NavyBlue,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(vertical = 13.dp)
        ) {
            Text(
                text = stringResource(R.string.home_search_view_details),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            color = CoolGray,
            letterSpacing = 0.6.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            color = DarkText,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}