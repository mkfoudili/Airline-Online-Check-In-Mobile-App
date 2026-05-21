package com.example.check_in_mobile_app.presentation.checkin.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.flightdetails.FlightInfoCard
import com.example.check_in_mobile_app.presentation.components.flightdetails.PassengerCard
import com.example.check_in_mobile_app.ui.theme.*
import com.example.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    passengerId: String,
    viewModel: ConfirmationViewModel = hiltViewModel(),
    onNavigateToHomeScreen: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val savedMsg = stringResource(R.string.boarding_saved_downloads)

    LaunchedEffect(passengerId) {
        viewModel.generateBoardingPass(passengerId)
    }

    LaunchedEffect(uiState.showDownloadSuccess) {
        if (uiState.showDownloadSuccess) {
            snackbarHostState.showSnackbar(savedMsg)
            viewModel.onDismissSuccess()
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = stringResource(R.string.confirmation_title),
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = NavyBlue,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DividerColor))
            Spacer(modifier = Modifier.height(24.dp))

            when {
                uiState.isGenerating -> {
                    Box(
                        modifier         = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NavyBlue)
                    }
                }

                uiState.errorMessage != null && uiState.boardingPass == null -> {
                    Column(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(32.dp))
                        Text(
                            text      = uiState.errorMessage
                                ?: stringResource(R.string.confirmation_error_generic),
                            color     = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier  = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.generateBoardingPass(passengerId) },
                            colors  = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                        ) {
                            Text(
                                text  = stringResource(R.string.confirmation_retry),
                                color = Color.White
                            )
                        }
                    }
                }

                uiState.boardingPass != null -> {
                    val bp = uiState.boardingPass!!

                    val firstName = bp.passengerName.split(" ").firstOrNull().orEmpty()
                    val lastName  = bp.passengerName.split(" ").drop(1)
                        .joinToString(" ").ifBlank { bp.passengerName }

                    val displayBooking = Booking(
                        bookingId  = bp.passId,
                        pnr        = bp.bookingReference,
                        lastName   = lastName,
                        status     = CheckInStatus.CHECKED_IN,
                        gate       = bp.gate.orEmpty(),
                        bookingRef = bp.bookingReference,
                        passengers = listOf(
                            Passenger(
                                passengerId    = bp.passengerId,
                                uid            = null,
                                firstName      = firstName,
                                lastName       = lastName,
                                passportNumber = null,
                                nationality    = null,
                                dateOfBirth    = null,
                                expiryDate     = null,
                                seatNumber     = bp.seatNumber,
                                checkinStatus  = "CHECKED_IN"
                            )
                        ),
                        flight = Flight(
                            flightId        = bp.flightId,
                            flightNumber    = bp.flightNumber,
                            origin          = bp.origin,
                            destination     = bp.destination,
                            departureTime   = bp.departureTime ?: 0L,
                            arrivalTime     = bp.arrivalTime ?: 0L,
                            aircraftType    = "",
                            status          = "Scheduled",
                            originCity      = bp.originCity,
                            destinationCity = bp.destinationCity,
                            checkInOpensTime = "",
                            boardingTime     = bp.boardingTime.orEmpty(),
                            gate             = bp.gate.orEmpty(),
                            terminal         = bp.terminal.orEmpty()
                        )
                    )

                    SuccessHeader()
                    FlightInfoCard(booking = displayBooking)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text       = stringResource(R.string.confirmation_passenger_details),
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = CoolGray,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.padding(vertical = 16.dp)
                    )

                    PassengerCard(
                        booking  = displayBooking,
                        infoText = stringResource(
                            R.string.confirmation_frequent_flyer,
                            bp.passengerId
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoCard(
                            modifier  = Modifier
                                .weight(1f)
                                .border(1.dp, NavyBlue, RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(91.dp),
                            title     = stringResource(R.string.confirmation_seat_label),
                            value     = bp.seatNumber.orEmpty().ifBlank { "N/A" },
                            iconResId = com.example.data.R.drawable.briefcase
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        InfoCard(
                            modifier  = Modifier
                                .weight(1f)
                                .border(1.dp, NavyBlue, RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .height(91.dp),
                            title     = stringResource(R.string.confirmation_baggage_label),
                            value     = "01",
                            iconResId = com.example.data.R.drawable.briefcase
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Download PDF
                    Button(
                        onClick  = { viewModel.onDownloadPdf(context) },
                        enabled  = !uiState.isDownloadingPdf,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .border(1.dp, NavyBlue, RoundedCornerShape(14.dp)),
                        shape  = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor         = Color.White,
                            disabledContainerColor = Color.White
                        )
                    ) {
                        if (uiState.isDownloadingPdf) {
                            CircularProgressIndicator(
                                modifier    = Modifier.size(20.dp),
                                color       = NavyBlue,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                painter            = painterResource(R.drawable.download),
                                contentDescription = null,
                                tint               = NavyBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text       = stringResource(R.string.confirmation_download_pdf),
                                color      = NavyBlue,
                                fontSize   = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines   = 1,
                                overflow   = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick  = onNavigateToHomeScreen,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F5FB))
                    ) {
                        Text(
                            text       = stringResource(R.string.confirmation_back_dashboard),
                            color      = NavyBlue,
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SuccessHeader() {
    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier         = Modifier
                .size(80.dp)
                .background(Color.White, CircleShape)
                .border(2.dp, Color(0x332EE8AA), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter            = painterResource(R.drawable.circle_check2),
                contentDescription = null,
                tint               = ConfirmationGreen,
                modifier           = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text       = stringResource(R.string.confirmation_success_title),
            fontSize   = 26.sp,
            color      = ConfirmationGreen,
            fontWeight = FontWeight.ExtraBold,
            textAlign  = TextAlign.Center,
            maxLines   = 2,
            overflow   = TextOverflow.Ellipsis
        )
        Text(
            text       = stringResource(R.string.confirmation_success_desc),
            fontSize   = 16.sp,
            color      = InfoGray,
            lineHeight = 22.sp,
            modifier   = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
            textAlign  = TextAlign.Center
        )
    }
}

@Composable
private fun InfoCard(modifier: Modifier, title: String, value: String, iconResId: Int) {
    Column(
        modifier            = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter            = painterResource(iconResId),
            contentDescription = null,
            tint               = InfoGray
        )
        Text(
            text       = title,
            fontSize   = 10.sp,
            color      = InfoGray,
            fontWeight = FontWeight.Light,
            maxLines   = 1,
            overflow   = TextOverflow.Ellipsis
        )
        Text(
            text       = value,
            fontSize   = 18.sp,
            color      = NavyBlue,
            fontWeight = FontWeight.ExtraBold,
            maxLines   = 1,
            overflow   = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewConfirmationScreen() {
    ConfirmationScreen(passengerId = "preview-passenger")
}