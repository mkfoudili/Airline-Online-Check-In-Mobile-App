package com.example.check_in_mobile_app.presentation.components.checkin

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.NavyBlue

import androidx.compose.ui.res.stringResource

@Composable
fun UploadFromLibraryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = NavyBlue
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.image_placeholder),
            contentDescription = "Gallery icon",
            tint = DarkText,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.upload_library_button),
            color = DarkText,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
