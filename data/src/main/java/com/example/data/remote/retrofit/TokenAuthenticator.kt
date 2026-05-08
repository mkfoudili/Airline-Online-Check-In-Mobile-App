package com.example.data.remote.retrofit

import com.example.data.security.SecureStorage
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val secureStorage: SecureStorage
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // This is called when the server returns a 401 Unauthorized.
        // You would typically call a refresh token endpoint here.
        // For now, we return null to signal that we can't authenticate the request further.
        return null
    }
}
