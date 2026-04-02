package com.example.check_in_mobile_app.presentation.components.booking

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object BookingIcons {
    val Plane: ImageVector
        get() = ImageVector.Builder(
            name = "Plane",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(21f, 16f)
                lineTo(21f, 14f)
                lineToRelative(-8f, -5f)
                lineTo(13f, 3.5f)
                curveToRelative(0f, -0.83f, -0.67f, -1.5f, -1.5f, -1.5f)
                curveToRelative(-0.83f, 0f, -1.5f, 0.67f, -1.5f, 1.5f)
                lineTo(10f, 9f)
                lineToRelative(-8f, 5f)
                lineToRelative(0f, 2f)
                lineToRelative(8f, -2.5f)
                lineTo(10f, 19f)
                lineToRelative(-2f, 1.5f)
                lineTo(8f, 22f)
                lineToRelative(3.5f, -1f)
                lineToRelative(3.5f, 1f)
                lineToRelative(0f, -1.5f)
                lineTo(13f, 19f)
                lineToRelative(0f, -5.5f)
                lineTo(21f, 16f)
                close()
            }
        }.build()

    val Calendar: ImageVector
        get() = ImageVector.Builder(
            name = "Calendar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(20f, 3f)
                horizontalLineToRelative(-1f)
                lineTo(19f, 1f)
                horizontalLineToRelative(-2f)
                verticalLineToRelative(2f)
                lineTo(7f, 3f)
                lineTo(7f, 1f)
                lineTo(5f, 1f)
                verticalLineToRelative(2f)
                lineTo(4f, 3f)
                curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                verticalLineToRelative(16f)
                curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
                horizontalLineToRelative(16f)
                curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
                lineTo(22f, 5f)
                curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
                close()
                moveTo(20f, 21f)
                lineTo(4f, 21f)
                lineTo(4f, 8f)
                horizontalLineToRelative(16f)
                verticalLineToRelative(13f)
                close()
            }
        }.build()

    val Clock: ImageVector
        get() = ImageVector.Builder(
            name = "Clock",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(11.99f, 2f)
                curveTo(6.47f, 2f, 2f, 6.48f, 2f, 12f)
                reflectiveCurveToRelative(4.47f, 10f, 9.99f, 10f)
                curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
                reflectiveCurveTo(17.52f, 2f, 11.99f, 2f)
                close()
                moveTo(12f, 20f)
                curveToRelative(-4.42f, 0f, -8f, -3.58f, -8f, -8f)
                reflectiveCurveToRelative(3.58f, -8f, 8f, -8f)
                reflectiveCurveToRelative(8f, 3.58f, 8f, 8f)
                reflectiveCurveToRelative(-3.58f, 8f, -8f, 8f)
                close()
                moveTo(12.5f, 7f)
                lineTo(11f, 7f)
                verticalLineToRelative(6f)
                lineToRelative(5.25f, 3.15f)
                lineToRelative(0.75f, -1.23f)
                lineToRelative(-4.5f, -2.67f)
                close()
            }
        }.build()
}
