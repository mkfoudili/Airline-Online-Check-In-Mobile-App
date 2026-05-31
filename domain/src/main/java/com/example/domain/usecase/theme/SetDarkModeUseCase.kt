package com.example.domain.usecase.theme

import com.example.domain.preferences.ThemeRepository
import javax.inject.Inject

class SetDarkModeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(isDarkMode: Boolean) {
        themeRepository.setDarkMode(isDarkMode)
    }
}
