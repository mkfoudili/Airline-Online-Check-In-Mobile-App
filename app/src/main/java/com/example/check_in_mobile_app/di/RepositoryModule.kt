package com.example.check_in_mobile_app.di

import com.example.data.preferences.UserPreferencesRepository
import com.example.domain.preferences.LanguageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLanguageRepository(
        impl: UserPreferencesRepository
    ): LanguageRepository
}