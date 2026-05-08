package com.example.domain.usecase.booking

import com.example.domain.model.Booking
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.BookingRepository
import javax.inject.Inject

class GetUpcomingBookingsUseCase @Inject constructor(
    private val repository: BookingRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(uid: String? = null): Result<List<Booking>> {
        val finalUid = uid ?: authRepository.getCurrentUserId() ?: return Result.failure(Exception("Not logged in"))
        return repository.getUpcomingBookings(finalUid)
    }
}
