package com.example.domain.usecase.boarding

import com.example.domain.model.BoardingPass
import com.example.domain.repository.BoardingPassRepository
import javax.inject.Inject

/**
 * Saves a boarding pass locally (Room cache) after server generation.
 * Called from the presentation layer once the server confirms the boarding pass.
 */
class SaveBoardingPassUseCase @Inject constructor(
    private val boardingPassRepository: BoardingPassRepository
) {
    suspend operator fun invoke(boardingPass: BoardingPass) {
        boardingPassRepository.saveBoardingPassLocally(boardingPass)
    }
}