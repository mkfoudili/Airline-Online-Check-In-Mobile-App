package com.example.check_in_mobile_app.presentation.components.checkin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SeatGrid(
    seats: List<SeatModel> = generateFallbackSeats(),
    selectedSeatId: String? = null,
    onSeatSelected: (SeatModel) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val rows = seats.chunked(6)

    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        itemsIndexed(rows) { rowIndex, rowSeats ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // First 3 seats
                rowSeats.take(3).forEach { seat ->
                    val seatState = when {
                        !seat.isAvailable                   -> SeatState.BLOCKED
                        seat.seatId == selectedSeatId       -> SeatState.SELECTED
                        else                                -> SeatState.AVAILABLE
                    }
                    val seatType = if (seat.isPremium) SeatType.PREMIUM else SeatType.REGULAR
                    Seat(
                        label = seat.seatNumber,
                        seatState = seatState,
                        seatType = seatType,
                        onClick = {
                            if (seat.isAvailable) {
                                onSeatSelected(seat)
                            }
                        }
                    )
                }


                Text(
                    text = "${rowIndex + 1}",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )

                // Last 3 seats
                rowSeats.drop(3).forEach { seat ->
                    val seatState = when {
                        !seat.isAvailable                   -> SeatState.BLOCKED
                        seat.seatId == selectedSeatId       -> SeatState.SELECTED
                        else                                -> SeatState.AVAILABLE
                    }
                    val seatType = if (seat.isPremium) SeatType.PREMIUM else SeatType.REGULAR
                    Seat(
                        label = seat.seatNumber,
                        seatState = seatState,
                        seatType = seatType,
                        onClick = {
                            if (seat.isAvailable) {
                                onSeatSelected(seat)
                            }
                        }
                    )

                }

            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
//dummy data I will delete it after the backend is ready
fun generateFallbackSeats(flightId: String = "FL001"): List<SeatModel> {
    val rows = (1..30)
    val columns = listOf("A", "B", "C", "D", "E", "F")


    val occupiedSeats = setOf("3B", "5A", "7C", "10F", "15D", "20A", "22E")

    return rows.flatMap { row ->
        columns.map { col ->
            val seatNumber = "$row$col"

            SeatModel(
                seatId = seatNumber,
                flightId = flightId,
                seatNumber = seatNumber,
                seatClass = if (row <= 2) "BUSINESS" else "ECONOMY",
                isAvailable = !occupiedSeats.contains(seatNumber),
                isPremium = row <= 2,
                occupiedBy = if (occupiedSeats.contains(seatNumber)) "PASSENGER" else null
            )

        }
    }
}