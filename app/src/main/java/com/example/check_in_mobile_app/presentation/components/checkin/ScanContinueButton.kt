package com.example.check_in_mobile_app.presentation.components.checkin

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Slate500
import com.example.check_in_mobile_app.ui.theme.SurfaceGray

@Composable
fun ScanContinueButton(
    isPending: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NavyBlue,
            contentColor = Color.White,
            disabledContainerColor = SurfaceGray,
            disabledContentColor = Color(0xFF91959C)
        ),
        enabled = !isPending
    ) {
        Text(
            text = "Continue",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
