package com.example.domain.usecase.profile

import android.content.Context
import android.net.Uri
import com.example.domain.model.Profile
import com.example.domain.repository.ProfileRepository
import javax.inject.Inject

class UploadProfilePhotoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        imageUri: Uri,
        context: Context
    ): Profile {
        return repository.uploadProfilePhoto(
            imageUri= imageUri,
            context= context
        )
    }
}