package com.example.check_in_mobile_app.presentation.components.baggage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.*

@Composable
fun BaggageNoteCard(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ErrorRed.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = ErrorRed,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Note: All baggage is subject to inspection. Restricted items include hazardous materials and certain lithium batteries.",
            style = Typography.bodyLarge.copy(
                color = ErrorRed,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontStyle = FontStyle.Italic
            )
        )
    }
}
