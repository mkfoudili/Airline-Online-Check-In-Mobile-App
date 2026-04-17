package com.example.check_in_mobile_app.presentation.components.checkin

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.NavyBlue

private val CameraBackground = Color(0xFF1A1A2E)
private val FrameWhite = Color.White
private val OverlayDark = Color(0xCC000000)


@Composable
fun CameraViewfinder(
    capturedBitmap: Bitmap?,
    cameraController: androidx.camera.view.LifecycleCameraController? = null,
    hasCameraPermission: Boolean = false,
    modifier: Modifier = Modifier
) {
    val screenHeight = androidx.compose.ui.platform.LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * 0.5f)
            .clipToBounds()
            .background(CameraBackground),
        contentAlignment = Alignment.Center
    ) {
        // Captured image or dark background
        if (capturedBitmap != null) {
            Image(
                bitmap = capturedBitmap.asImageBitmap(),
                contentDescription = "Captured passport",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (hasCameraPermission && cameraController != null) {
            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    androidx.camera.view.PreviewView(ctx).apply {
                        scaleType = androidx.camera.view.PreviewView.ScaleType.FILL_CENTER
                        controller = cameraController
                    }
                }
            )
        }

        if (capturedBitmap == null) { //  Dashed alignment frame drawn on canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val frameW = size.width * 0.82f
                val frameH = size.height * 0.72f
                val left = (size.width - frameW) / 2f
                val top = (size.height - frameH) / 2f
                val cornerLen = 32.dp.toPx()
                val cornerRadius = 8.dp.toPx()
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 8f))

                // Dashed border rectangle
                drawRoundRect(
                    color = FrameWhite.copy(alpha = 0.7f),
                    topLeft = Offset(left, top),
                    size = Size(frameW, frameH),
                    cornerRadius = CornerRadius(cornerRadius),
                    style = Stroke(width = 2.dp.toPx(), pathEffect = dashEffect)
                )

                // Solid corner accents — top-left
                drawLine(
                    FrameWhite,
                    Offset(left, top + cornerLen),
                    Offset(left, top + cornerRadius),
                    strokeWidth = 4.dp.toPx()
                )
                drawLine(
                    FrameWhite,
                    Offset(left + cornerRadius, top),
                    Offset(left + cornerLen, top),
                    strokeWidth = 4.dp.toPx()
                )
                // top-right
                drawLine(
                    FrameWhite,
                    Offset(left + frameW, top + cornerLen),
                    Offset(left + frameW, top + cornerRadius),
                    strokeWidth = 4.dp.toPx()
                )
                drawLine(
                    FrameWhite,
                    Offset(left + frameW - cornerLen, top),
                    Offset(left + frameW - cornerRadius, top),
                    strokeWidth = 4.dp.toPx()
                )
                // bottom-left
                drawLine(
                    FrameWhite,
                    Offset(left, top + frameH - cornerLen),
                    Offset(left, top + frameH - cornerRadius),
                    strokeWidth = 4.dp.toPx()
                )
                drawLine(
                    FrameWhite,
                    Offset(left + cornerRadius, top + frameH),
                    Offset(left + cornerLen, top + frameH),
                    strokeWidth = 4.dp.toPx()
                )
                // bottom-right
                drawLine(
                    FrameWhite,
                    Offset(left + frameW, top + frameH - cornerLen),
                    Offset(left + frameW, top + frameH - cornerRadius),
                    strokeWidth = 4.dp.toPx()
                )
                drawLine(
                    FrameWhite,
                    Offset(left + frameW - cornerLen, top + frameH),
                    Offset(left + frameW - cornerRadius, top + frameH),
                    strokeWidth = 4.dp.toPx()
                )
            }
    }


        if (capturedBitmap == null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2A3343).copy(alpha=0.6f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (hasCameraPermission) "PLACE PASSPORT WITHIN FRAME" else "CAMERA PERMISSION REQUIRED",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // "Ready to Scan" pill at the top
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 14.dp)
                .border(1.dp, Color.White.copy(alpha=0.3f), RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(NavyBlue.copy(alpha = 0.6f))
                .padding(horizontal = 14.dp, vertical = 6.dp)

        ) {
            Text(
                text = if (capturedBitmap == null) "Ready to Scan" else "Image Captured",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


