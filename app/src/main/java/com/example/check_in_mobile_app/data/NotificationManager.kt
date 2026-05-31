package com.example.check_in_mobile_app.data

import com.example.data.security.SecureStorage
import com.example.domain.usecase.notification.getUnreadNotificationCountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManager @Inject constructor(
    private val getUnreadNotificationCountUseCase: getUnreadNotificationCountUseCase,
    private val secureStorage: SecureStorage
) {
    private val _hasUnread = MutableStateFlow(false)
    val hasUnread: StateFlow<Boolean> = _hasUnread.asStateFlow()

    fun setHasUnread(value: Boolean) {
        _hasUnread.value = value
    }
    
    suspend fun fetchInitialStatus() {
        val uid = secureStorage.getUserId() ?: return

        getUnreadNotificationCountUseCase(uid)
            .onSuccess { count ->
                _hasUnread.value = count > 0
            }
            .onFailure {
                _hasUnread.value = false
            }
    }
}
