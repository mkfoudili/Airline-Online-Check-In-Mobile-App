package com.example.check_in_mobile_app.presentation.components.booking

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.LocalAppColors
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    selectedDate: String?,
    onDateSelected: (String) -> Unit,
    onClearDate: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val hasDate = !selectedDate.isNullOrEmpty()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .clickable { showDatePicker = true }
            .padding(start = 12.dp, end = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = stringResource(R.string.select_date),
                tint = LocalAppColors.current.textAccent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(Modifier.weight(1f)) {
                if (!hasDate) {
                    Text(
                        text = stringResource(R.string.select_date),
                        fontSize = 14.sp,
                        color = LocalAppColors.current.textAccent,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(selectedDate!!, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            if (hasDate) {
                // Show clear (X) button when a date is selected
                IconButton(
                    onClick = {
                        onClearDate()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Clear date",
                        tint = LocalAppColors.current.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                // Show chevron when no date selected
                Icon(
                    painter = painterResource(id = R.drawable.chevron_down),
                    contentDescription = "Expand",
                    tint = LocalAppColors.current.textSecondary,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                )
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Use system locale for correct month/day naming
                        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        onDateSelected(sdf.format(Date(millis)))
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.ok), color = LocalAppColors.current.textAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel), color = LocalAppColors.current.textAccent)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = NavyBlue,
                    todayDateBorderColor = LocalAppColors.current.textAccent,
                    todayContentColor = LocalAppColors.current.textAccent,
                )
            )
        }
    }
}


