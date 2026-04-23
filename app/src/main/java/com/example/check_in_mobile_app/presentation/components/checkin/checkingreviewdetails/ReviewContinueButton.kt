package com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails

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
import com.example.check_in_mobile_app.R
import androidx.compose.ui.res.stringResource
import com.example.check_in_mobile_app.ui.theme.NavyBlue

@Composable
fun ReviewContinueButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NavyBlue,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFE2E8F0),
            disabledContentColor = Color(0xFF9CA3AF)
        )
    ) {
        Text(
            text = stringResource(R.string.review_continue_to_seat),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
