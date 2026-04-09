package com.example.check_in_mobile_app.presentation.components.baggage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.*

@Composable
fun BaggageRulesCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceGray, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = NavyBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "BAGGAGE RULES & LIMITS",
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        RuleItem(text = "Standard checked baggage weight limit is 23kg (50lbs).", highlight = "23kg (50lbs)")
        Spacer(modifier = Modifier.height(8.dp))
        RuleItem(text = "Dimensions must not exceed 158cm (total L+W+H).", highlight = "158cm")
    }
}

@Composable
private fun RuleItem(text: String, highlight: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "• ", color = DarkText, fontSize = 14.sp)
        Text(
            text = text,
            style = Typography.bodyLarge.copy(
                color = Slate500,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        )
    }
}
