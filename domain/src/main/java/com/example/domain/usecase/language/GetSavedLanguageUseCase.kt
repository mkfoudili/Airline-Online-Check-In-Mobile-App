package com.example.domain.usecase.language

import com.example.domain.preferences.LanguageRepository
import kotlinx.coroutines.flow.Flow

class GetSavedLanguageUseCase(
    private val repo: LanguageRepository
) {
    operator fun invoke() = repo.appLanguageFlow
}