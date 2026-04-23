package com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.ErrorRed

import androidx.compose.ui.res.stringResource

@Composable
fun WarningBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ErrorRed.copy(alpha = 0.05f),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = R.drawable.info_circle),
            contentDescription = null,
            tint = ErrorRed,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Text(
            text = stringResource(R.string.review_warning_text),
            fontSize = 13.sp,
            color = Color(0xFFEF4444),
            lineHeight = 18.sp
        )
    }
}
