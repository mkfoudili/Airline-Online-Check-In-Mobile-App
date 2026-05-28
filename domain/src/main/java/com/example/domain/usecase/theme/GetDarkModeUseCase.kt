package com.example.domain.usecase.theme

import com.example.domain.preferences.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkModeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(): Flow<Boolean> = themeRepository.isDarkModeFlow
}
