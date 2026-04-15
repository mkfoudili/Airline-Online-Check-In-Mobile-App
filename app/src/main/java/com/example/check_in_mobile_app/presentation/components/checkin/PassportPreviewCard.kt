package com.example.check_in_mobile_app.presentation.components.checkin

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.ActiveGreen
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.CheckedInText
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.ErrorRed
import com.example.check_in_mobile_app.ui.theme.LightGray
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.Slate500
import com.example.check_in_mobile_app.ui.theme.SurfaceGray
import com.example.check_in_mobile_app.ui.theme.lightTextGrey

fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 1.dp,
    dashWidth: Dp = 8.dp,
    dashGap: Dp = 4.dp,
    cornerRadius: Dp = 14.dp
): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            asFrameworkPaint().apply {
                isAntiAlias = true
                style = android.graphics.Paint.Style.STROKE
                this.strokeWidth = strokeWidth.toPx()
                this.color = android.graphics.Color.TRANSPARENT
                pathEffect = android.graphics.DashPathEffect(
                    floatArrayOf(dashWidth.toPx(), dashGap.toPx()), 0f
                )
                this.color = color.toArgb()
            }
        }
        val radius = cornerRadius.toPx()
        val halfStroke = strokeWidth.toPx() / 2
        canvas.drawRoundRect(
            left   = halfStroke,
            top    = halfStroke,
            right  = size.width  - halfStroke,
            bottom = size.height - halfStroke,
            radiusX = radius,
            radiusY = radius,
            paint   = paint
        )
    }
}


@Composable
fun PassportPreviewCard(
    capturedBitmap: Bitmap?,
    onDelete: () -> Unit, // 1. Added callback parameter
    modifier: Modifier = Modifier
) {
    val isPending = capturedBitmap == null

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isPending) SurfaceGray else Color(0xFFDCFCE7).copy(alpha=0.5f))
            .dashedBorder(
                color        = if (isPending) BorderLight else Color(0xFFDCFCE7),
                strokeWidth  = 1.dp,
                dashWidth    = 8.dp,
                dashGap      = 2.dp,
                cornerRadius = 14.dp
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (capturedBitmap != null) {
                Image(
                    bitmap = capturedBitmap.asImageBitmap(),
                    contentDescription = "Passport thumbnail",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.checking_review_passport),
                    contentDescription = "Passport",
                    tint = Slate500,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Text
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Passport Preview",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPending) DarkText else CheckedInText
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = if (isPending) "No image captured yet" else "Image captured",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = if (isPending) Color(0xFF94A3B8) else ActiveGreen
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isPending) BorderLight else Color(0xFFDCFCE7))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (isPending) "Pending" else "Ready",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPending) Slate500 else CheckedInText
                )
            }


            if (!isPending) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete captured passport",
                        tint = ErrorRed.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}