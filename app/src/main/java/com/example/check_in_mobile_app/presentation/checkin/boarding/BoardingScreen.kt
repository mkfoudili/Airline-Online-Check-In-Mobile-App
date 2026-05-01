package com.example.check_in_mobile_app.presentation.checkin.boarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.BaseApplication
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.OfflineBanner
import com.example.check_in_mobile_app.presentation.components.boarding.BoardingPassCard
import com.example.check_in_mobile_app.presentation.components.boarding.BoardingTopBar
import com.example.check_in_mobile_app.presentation.components.boarding.BoardingFooter
import com.example.check_in_mobile_app.presentation.components.boarding.ShimmerBox
import com.example.check_in_mobile_app.presentation.components.boarding.BoardingPassSkeleton

import com.example.check_in_mobile_app.ui.theme.*


@Composable
fun BoardingScreen(
    viewModel: BoardingViewModel = run {
        val context = LocalContext.current
        val app = context.applicationContext as BaseApplication
        viewModel(
            factory = BoardingViewModelFactory(
                boardingPassRepository = app.boardingPassRepository,
                generateQRCodeUseCase = app.generateQRCodeUseCase,
                generateQRCodeBitmapUseCase = app.generateQRCodeBitmapUseCase,
                generatePdfUseCase = app.generatePdfUseCase
            )
        )
    },
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val savedMsg = stringResource(R.string.boarding_saved_downloads)
    val errorMsg = stringResource(R.string.boarding_error_prefix, uiState.errorMessage ?: "")

    LaunchedEffect(uiState.showDownloadSuccess) {
        if (uiState.showDownloadSuccess) {
            snackbarHostState.showSnackbar(savedMsg)
            viewModel.onDismissDownloadSuccess()
        }
    }
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) snackbarHostState.showSnackbar(errorMsg)
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { BoardingTopBar(onBack = onBack) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ShimmerBox(
                        Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    BoardingPassSkeleton()
                    ShimmerBox(
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                }
            }

            AnimatedVisibility(
                visible = !uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    OfflineBanner(
                        iconId = R.drawable.wifi_off,
                        iconDescription = "wifi-off",
                        title = stringResource(R.string.boarding_available_offline),
                        description = stringResource(R.string.boarding_offline_desc),
                        isVerifiedBadge = true
                    )
                    Spacer(Modifier.height(20.dp))
                    BoardingPassCard(
                        uiState = uiState,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.onDownloadPdf(context) },
                        enabled = !uiState.isDownloadingPdf,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyBlue,
                            disabledContainerColor = NavyBlue)
                    ) {
                        if (uiState.isDownloadingPdf) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 4.dp
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.download),
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.boarding_download_pdf),
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    BoardingFooter()
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BoardingScreenPreview() {
    BoardingScreen()
}