package com.example.check_in_mobile_app.presentation.components.checkin

enum class SeatState {
    AVAILABLE,
    SELECTED,
    BLOCKED
}

enum class SeatType {
    REGULAR,
    PREMIUM
}
data class SeatModel(
    val seatId: String,
    val flightId: String,
    val seatNumber: String,   // e.g. "7B", "12A"
    val seatClass: String,    // e.g. "ECONOMY", "BUSINESS"
    val isAvailable: Boolean,
    val isPremium: Boolean,
    val occupiedBy: String?   // null if not occupied
)