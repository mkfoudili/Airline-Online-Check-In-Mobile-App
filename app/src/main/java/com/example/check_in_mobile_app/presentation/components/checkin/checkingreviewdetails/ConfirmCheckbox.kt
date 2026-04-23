package com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import androidx.compose.ui.res.stringResource
import com.example.check_in_mobile_app.ui.theme.CoolGray
import com.example.check_in_mobile_app.ui.theme.LightGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue

@Composable
fun ConfirmCheckbox(
    isConfirmed: Boolean,
    onConfirmedChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isConfirmed)  NavyBlue else LightGray ,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = isConfirmed,
            onCheckedChange = onConfirmedChanged,
            colors = CheckboxDefaults.colors(
                checkedColor = NavyBlue,
                uncheckedColor = CoolGray
            ),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = stringResource(R.string.review_confirm_text),
            fontSize = 14.sp,
            color = Color.Black,
            lineHeight = 20.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
