package com.example.check_in_mobile_app.presentation.components.booking

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import java.text.SimpleDateFormat
import java.util.*

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
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .clickable { showDatePicker = true }
            .padding(start = 12.dp, end = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "Date",
                tint = NavyBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(Modifier.weight(1f)) {
                if (!hasDate) {
                    Text("Select Date", fontSize = 14.sp, color = NavyBlue, fontWeight = FontWeight.Medium)
                } else {
                    Text(selectedDate!!, fontSize = 14.sp, color = Color.Black)
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
                        tint = MediumGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                // Show chevron when no date selected
                Icon(
                    painter = painterResource(id = R.drawable.chevron_down),
                    contentDescription = "Expand",
                    tint = MediumGray,
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
                        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        onDateSelected(sdf.format(Date(millis)))
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = NavyBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = NavyBlue)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = NavyBlue,
                    todayDateBorderColor = NavyBlue,
                    todayContentColor = NavyBlue,
                )
            )
        }
    }
}

