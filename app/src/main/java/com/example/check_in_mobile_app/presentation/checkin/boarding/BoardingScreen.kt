package com.example.check_in_mobile_app.presentation.checkin.boarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.BaseApplication
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.*
import com.example.check_in_mobile_app.presentation.components.OfflineBanner

@Composable
fun BoardingScreen(
    viewModel: BoardingViewModel = run {
        val context = LocalContext.current
        val app = context.applicationContext as BaseApplication
        viewModel(
            factory = BoardingViewModelFactory(
                boardingPassRepository = app.boardingPassRepository,
                generateQRCodeUseCase = app.generateQRCodeUseCase,
                generatePdfUseCase = app.generatePdfUseCase
            )
        )
    },
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.showDownloadSuccess) {
        if (uiState.showDownloadSuccess) {
            snackbarHostState.showSnackbar("Boarding pass saved to Downloads")
            viewModel.onDismissDownloadSuccess()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar("Error: $it")
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { BoardingTopBar(onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            OfflineBanner(
                iconId = R.drawable.wifi_off,
                iconDescription = "Info icon",
                title = "Available Offline",
                description = "This pass is saved to your local storage.",
                isVerifiedBadge = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            BoardingPassCard(uiState = uiState, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onDownloadPdf(context) },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = "Download icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.isLoading) "Generating PDF..." else "Download PDF",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            BoardingFooter()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Top app bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoardingTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Boarding Pass",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "Back icon",
                    tint = Color(0xFF2A3343)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
    )
    HorizontalDivider(color = BorderLight, thickness = 1.dp)
}

// Boarding pass card
@Composable
private fun BoardingPassCard(uiState: BoardingUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyBlue)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.diamond),
                        contentDescription = "Diamond icon",
                        tint = Color.White
                    )
                    Text(
                        text = "SKYWIRE AIRLINES",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = "FLIGHT ${uiState.flightNumber}",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 0.3.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = uiState.departureCode,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue,
                        letterSpacing = (-1).sp,
                        lineHeight = 42.sp
                    )
                    Text(
                        text = uiState.departureCity.uppercase(),
                        fontSize = 11.sp,
                        color = SubtleText,
                        letterSpacing = 0.5.sp
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Canvas(modifier = Modifier.weight(1f).height(8.dp)) {
                            drawLine(
                                color = Color(0xFFCBD5E1),
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.plane2),
                            contentDescription = "Plane icon"
                        )
                        Canvas(modifier = Modifier.weight(1f).height(8.dp)) {
                            drawLine(
                                color = Color(0xFFCBD5E1),
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "3H 55M",
                        fontSize = 11.sp,
                        color = SubtleText,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = uiState.arrivalCode,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue,
                        letterSpacing = (-1).sp,
                        lineHeight = 42.sp,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = uiState.arrivalCity.uppercase(),
                        fontSize = 11.sp,
                        color = SubtleText,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(20.dp, 0.dp)
        ) {
            drawLine(
                color = Color(0xFFCBD5E1),
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = 5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LabelValueCell(label = "GATE", value = uiState.gate)
                LabelValueCell(label = "SEAT", value = uiState.seat, align = TextAlign.End)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LabelValueCell(label = "BOARDING", value = uiState.boardingTime)
                LabelValueCell(label = "ZONE", value = "2", align = TextAlign.End)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(192.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, QrBorder, RoundedCornerShape(16.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    // QR Code Placeholder (drawable resource)
                    Image(
                        painter = painterResource(id = R.drawable.barcode_qr),
                        contentDescription = "QR Code - ${uiState.qrCodeData}"
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }

        HorizontalDivider(color = BorderLight, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC))
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PASSENGER",
                    fontSize = 12.sp,
                    color = SubtleText,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = uiState.passengerName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "ECONOMY PLUS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2A3343),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun LabelValueCell(
    label: String,
    value: String,
    align: TextAlign = TextAlign.Start
) {
    Column(horizontalAlignment = if (align == TextAlign.End) Alignment.End else Alignment.Start) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = SubtleText,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp,
            textAlign = align
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = align
        )
    }
}

// Footer
@Composable
private fun BoardingFooter() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceGray)
            .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.clock),
            contentDescription = "Clock icon",
            modifier = Modifier.padding(0.dp, 5.dp)
        )
        Column {
            Text(
                text = "Boarding starts in 45m",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Head to Gate A12. Security wait time is currently approximately 15 minutes.",
                fontSize = 11.sp,
                color = SubtleText,
                lineHeight = 16.sp
            )
        }
    }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BoardingScreenPreview() {
    BoardingScreen()
}