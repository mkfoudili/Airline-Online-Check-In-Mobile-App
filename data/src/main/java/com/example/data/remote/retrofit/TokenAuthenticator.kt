package com.example.data.remote.retrofit

import com.example.data.remote.URL
import com.example.data.remote.dto.RefreshRequest
import com.example.data.remote.dto.TokenResponse
import com.example.data.security.SecureStorage
import com.google.gson.Gson
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val secureStorage: SecureStorage
) : Authenticator {

    // Client séparé : pas d'AuthInterceptor, pas d'Authenticator → pas de boucle
    private val refreshClient = OkHttpClient()
    private val gson = Gson()

    override fun authenticate(route: Route?, response: Response): Request? {
        // Si on a déjà tenté un refresh sur cette requête, abandonner
        if (response.request.header("X-Retry-After-Refresh") != null) return null

        val refreshToken = secureStorage.getRefreshToken() ?: return null

        return try {
            val newTokens = callRefresh(refreshToken) ?: run {
                secureStorage.clearTokens()
                return null
            }

            secureStorage.saveTokens(newTokens.accessToken, newTokens.refreshToken ?: refreshToken)

            response.request.newBuilder()
                .header("Authorization", "Bearer ${newTokens.accessToken}")
                .header("X-Retry-After-Refresh", "true")
                .build()

        } catch (e: Exception) {
            secureStorage.clearTokens()
            null
        }
    }

    private fun callRefresh(refreshToken: String): TokenResponse? {
        val body = gson.toJson(RefreshRequest(refreshToken))
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("${URL}auth/refresh")
            .post(body)
            .build()

        val response = refreshClient.newCall(request).execute()

        if (!response.isSuccessful) return null

        return response.body?.string()?.let {
            gson.fromJson(it, TokenResponse::class.java)
        }
    }
}