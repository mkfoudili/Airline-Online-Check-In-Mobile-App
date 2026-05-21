package com.example.data.remote.retrofit

import com.example.data.security.SecureStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val secureStorage: SecureStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        
        // Skip adding Authorization header for auth endpoints
        if (path.contains("/auth/login") || 
            path.contains("/auth/register") || 
            path.contains("/auth/google") ||
            path.contains("/auth/exists")) {
            return chain.proceed(request)
        }

        val token = secureStorage.getAuthToken()
        val newRequest = request.newBuilder()

        if (token != null) {
            newRequest.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(newRequest.build())
    }
}
