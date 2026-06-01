package com.example.check_in_mobile_app.presentation.main.notifications

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.data.NotificationManager
import com.example.data.security.SecureStorage
import com.example.domain.usecase.notification.GetNotificationsUseCase
import com.example.domain.usecase.notification.MarkAllNotificationsReadUseCase
import com.example.domain.usecase.notification.MarkNotificationReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for the Notifications screen.
 * Handles loading notifications, marking them as read, and push-based routing.
 */
@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markAllNotificationsReadUseCase: MarkAllNotificationsReadUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase,
    private val secureStorage: SecureStorage,
    private val notificationManager: NotificationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    val hasUnread: StateFlow<Boolean> = notificationManager.hasUnread
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val userId: String
        get() = secureStorage.getUserId() ?: ""

    init {
        loadNotifications()
    }

    /**
     * Fetches notifications from the remote repository and groups them by date.
     */
    fun loadNotifications() {
        if (userId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "User not logged in") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getNotificationsUseCase(userId).onSuccess { domainNotifications ->
                val items = domainNotifications.map { domain ->
                    NotificationItem(
                        id = domain.notificationId,
                        title = domain.title,
                        description = domain.body,
                        timeAgo = formatTimeAgo(domain.createdAt),
                        isRead = domain.isRead,
                        type = domain.type,
                        createdAt = domain.createdAt
                    )
                }
                val hasUnread = items.any { !it.isRead }
                notificationManager.setHasUnread(hasUnread)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        groupedNotifications = groupNotifications(items),
                        errorMessage = null,
                        isOffline = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (it.groupedNotifications.isEmpty()) error.message ?: "Failed to load notifications" else null,
                        isOffline = true
                    )
                }
            }
        }
    }

    /** Appelé par le pull-to-refresh de l'UI */
    fun refresh() {
        if (userId.isBlank()) return
        viewModelScope.launch {
            _isRefreshing.value = true
            getNotificationsUseCase(userId).onSuccess { domainNotifications ->
                val items = domainNotifications.map { domain ->
                    NotificationItem(
                        id = domain.notificationId,
                        title = domain.title,
                        description = domain.body,
                        timeAgo = formatTimeAgo(domain.createdAt),
                        isRead = domain.isRead,
                        type = domain.type,
                        createdAt = domain.createdAt
                    )
                }
                _uiState.update {
                    it.copy(
                        groupedNotifications = groupNotifications(items),
                        errorMessage = null,
                        isOffline = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        errorMessage = if (it.groupedNotifications.isEmpty()) error.message ?: "Failed to load notifications" else null,
                        isOffline = true
                    )
                }
            }
            _isRefreshing.value = false
        }
    }

    /**
     * Marks all user notifications as read on the backend.
     */
    fun markAllAsRead() {
        if (userId.isBlank()) return

        viewModelScope.launch {
            markAllNotificationsReadUseCase(userId).onSuccess {
                notificationManager.setHasUnread(false)
                loadNotifications()
            }
        }
    }

    /**
     * Marks a specific notification as read.
     */
    fun markSingleAsRead(notificationId: String) {
        viewModelScope.launch {
            markNotificationReadUseCase(notificationId).onSuccess {
                loadNotifications()
            }
        }
    }

    /**
     * Processes Intent extras from push notifications to determine the target screen.
     * This allows the UI to react and navigate accordingly.
     */
    fun handleNotificationIntent(intent: Intent?) {
        val screen = intent?.getStringExtra("screen")
        val bookingId = intent?.getStringExtra("bookingId")

        val event = when (screen) {
            "checkin" -> RoutingEvent.NavigateToCheckIn
            "booking" -> bookingId?.let { RoutingEvent.NavigateToBooking(it) }
            "notifications" -> RoutingEvent.NavigateToNotifications
            else -> null
        }

        if (event != null) {
            _uiState.update { it.copy(routingEvent = event) }
        }
    }

    /**
     * Resets the routing event once the navigation has been performed.
     */
    fun onRoutingEventHandled() {
        _uiState.update { it.copy(routingEvent = null) }
    }

    private fun groupNotifications(items: List<NotificationItem>): Map<String, List<NotificationItem>> {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val yesterday = (today.clone() as Calendar).apply { add(Calendar.DATE, -1) }
        val thisWeek = (today.clone() as Calendar).apply { add(Calendar.DATE, -7) }

        return items.sortedByDescending { it.createdAt }.groupBy { item ->
            val itemCal = Calendar.getInstance().apply { timeInMillis = item.createdAt }
            when {
                itemCal.after(today) -> "Today"
                itemCal.after(yesterday) -> "Yesterday"
                itemCal.after(thisWeek) -> "This Week"
                else -> "Earlier"
            }
        }
    }

    private fun formatTimeAgo(createdAt: Long): String {
        val diff = System.currentTimeMillis() - createdAt
        val minutes = diff / (1000 * 60)
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            else -> "${days}d ago"
        }
    }
}
