package com.example.check_in_mobile_app.presentation.components.checkin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.LightGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassportScanTopBar(
    onBack: () -> Unit,
    currentStep: Int = 1
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Step 1: Passport",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = "Back",
                        tint = NavyBlue
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // Step progress bar — 5 segments
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (index < currentStep) NavyBlue
                            else BorderLight
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
    }
}