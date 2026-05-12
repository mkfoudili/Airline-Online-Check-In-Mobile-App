package com.example.check_in_mobile_app.presentation.checkin.passportscan

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.presentation.components.checkin.*
import com.example.check_in_mobile_app.presentation.components.checkin.CheckInTopBar
import com.example.check_in_mobile_app.presentation.components.checkin.checkingpassportscan.CameraViewfinder
import com.example.check_in_mobile_app.presentation.components.checkin.checkingpassportscan.PassportPreviewCard
import com.example.check_in_mobile_app.presentation.components.checkin.checkingpassportscan.ScanContinueButton
import com.example.check_in_mobile_app.presentation.components.checkin.checkingpassportscan.ScanPassportButton
import androidx.compose.ui.res.stringResource
import com.example.check_in_mobile_app.R

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.check_in_mobile_app.presentation.checkin.CheckInSessionViewModel
import com.example.check_in_mobile_app.presentation.checkin.OcrStatus
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.NavyBlue

@Composable
fun PassportScanScreen(
    onBack: () -> Unit = {},
    onContinue: () -> Unit = {},
    viewModel: PassportScanViewModel = viewModel(),
    sessionViewModel: CheckInSessionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sessionState by sessionViewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    LaunchedEffect(sessionState.ocrStatus) {
        if (sessionState.ocrStatus == OcrStatus.SUCCESS) {
            sessionViewModel.resetOcrStatus()
            onContinue()
        } else if (sessionState.ocrStatus == OcrStatus.ERROR) {
            sessionState.errorMessage?.let { 
                snackbarHostState.showSnackbar(it)
                sessionViewModel.clearError()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PassportScanScreenContent(
            capturedBitmap = uiState.capturedBitmap,
            onBack = onBack,
            onContinue = {
                // If not success/loading, manual trigger (just in case)
                if (sessionState.ocrStatus == OcrStatus.IDLE || sessionState.ocrStatus == OcrStatus.ERROR) {
                    uiState.capturedBitmap?.let { sessionViewModel.startOcrAndVerify(it) }
                }
            },
            onPassportCaptured = { bitmap ->
                viewModel.onPassportCaptured(bitmap)
            },
            isLoading = sessionState.ocrStatus == OcrStatus.SCANNING || sessionState.ocrStatus == OcrStatus.VERIFYING
        )

        // Error snackbar host
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp)
        )

        // Loading Overlay
        if (sessionState.ocrStatus == OcrStatus.SCANNING || sessionState.ocrStatus == OcrStatus.VERIFYING) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.6f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = if (sessionState.ocrStatus == OcrStatus.SCANNING) 
                            "Reading passport data..." else "Verifying with airline records...",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 40.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PassportScanScreenContent(
    capturedBitmap: Bitmap?,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onPassportCaptured: (Bitmap?) -> Unit,
    isLoading: Boolean = false
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val screenHeight = androidx.compose.ui.platform.LocalConfiguration.current.screenHeightDp.dp

    var hasCameraPermission by remember {
        mutableStateOf(
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    val cameraController = remember {
        androidx.camera.view.LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
        }
    }

    val mainExecutor = remember {
        androidx.core.content.ContextCompat.getMainExecutor(context)
    }

    fun capturePhoto() {
        if (!hasCameraPermission) {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
            return
        }

        cameraController.takePicture(
            mainExecutor,
            object : androidx.camera.core.ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: androidx.camera.core.ImageProxy) {

                    val rawBitmap = image.toBitmap()


                    val rotationDegrees = image.imageInfo.rotationDegrees.toFloat()


                    val matrix = android.graphics.Matrix().apply {
                        postRotate(rotationDegrees)
                    }


                    val rotatedBitmap = Bitmap.createBitmap(
                        rawBitmap,
                        0,
                        0,
                        rawBitmap.width,
                        rawBitmap.height,
                        matrix,
                        true
                    )


                    onPassportCaptured(rotatedBitmap)


                    image.close()
                }

                override fun onError(exception: androidx.camera.core.ImageCaptureException) {

                }
            }
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            @Suppress("DEPRECATION")
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            onPassportCaptured(bitmap)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { CheckInTopBar(onBack = onBack, currentStep = 1, title = stringResource(R.string.checkin_step1_title)) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            CameraViewfinder(
                capturedBitmap = capturedBitmap,
                cameraController = cameraController,
                hasCameraPermission = hasCameraPermission,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.5f)
            )


            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Spacer(modifier = Modifier.height(20.dp))



                ScanPassportButton(
                    onClick = { if (!isLoading) capturePhoto() }
                )

                UploadFromLibraryButton(
                    onClick = { if (!isLoading) galleryLauncher.launch("image/*") }
                )

                PassportPreviewCard(
                    capturedBitmap = capturedBitmap,
                    onDelete = { if (!isLoading) onPassportCaptured(null) }
                )

                ScanContinueButton(
                    isPending = capturedBitmap == null || isLoading,
                    onClick = onContinue
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


