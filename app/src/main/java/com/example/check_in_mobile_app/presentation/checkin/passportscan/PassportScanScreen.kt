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

@Composable
fun PassportScanScreen(
    onBack: () -> Unit = {},
    onContinue: () -> Unit = {},
    viewModel: PassportScanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PassportScanScreenContent(
        capturedBitmap = uiState.capturedBitmap,
        onBack = onBack,
        onContinue = onContinue,
        onPassportCaptured = viewModel::onPassportCaptured
    )
}

@Composable
fun PassportScanScreenContent(
    capturedBitmap: Bitmap?,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onPassportCaptured: (Bitmap?) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

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
    
    val mainExecutor = remember { androidx.core.content.ContextCompat.getMainExecutor(context) }

    fun capturePhoto() {
        if (!hasCameraPermission) {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
            return
        }
        cameraController.takePicture(
            mainExecutor,
            object : androidx.camera.core.ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: androidx.camera.core.ImageProxy) {
                    val bitmap = image.toBitmap()
                    onPassportCaptured(bitmap)
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
        topBar = { PassportScanTopBar(onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Camera viewfinder — shows captured image when available
            CameraViewfinder(
                capturedBitmap = capturedBitmap,
                cameraController = cameraController,
                hasCameraPermission = hasCameraPermission
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                AlignmentGuideCard()

                ScanPassportButton(onClick = { capturePhoto() })

                UploadFromLibraryButton(onClick = { galleryLauncher.launch("image/*") })

                // Step 3 — display result via capturedBitmap passed to CameraViewfinder above
                PassportPreviewCard(capturedBitmap = capturedBitmap)

                Spacer(modifier = Modifier.height(32.dp))

                ScanContinueButton(
                    isPending = capturedBitmap == null,
                    onClick = onContinue
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PassportScanScreenPreview() {
    PassportScanScreenContent(
        capturedBitmap = null,
        onBack = {},
        onContinue = {},
        onPassportCaptured = {}
    )
}
