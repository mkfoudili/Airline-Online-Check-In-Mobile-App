package com.example.check_in_mobile_app.presentation.notifications

import androidx.lifecycle.ViewModel
import com.example.check_in_mobile_app.presentation.main.notifications.NotificationItem
import com.example.check_in_mobile_app.presentation.main.notifications.NotificationType
import com.example.check_in_mobile_app.presentation.main.notifications.NotificationsUiState
import com.example.data.repository.NotificationRepositoryImpl
import com.example.domain.model.Notification
import com.example.domain.usecase.notification.GetNotificationsUseCase
import com.example.domain.usecase.notification.MarkAllNotificationsReadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase = GetNotificationsUseCase(NotificationRepositoryImpl()),
    private val markAllNotificationsReadUseCase: MarkAllNotificationsReadUseCase = MarkAllNotificationsReadUseCase(NotificationRepositoryImpl())
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        _uiState.update { it.copy(isLoading = true) }
        // Mock user id
        getNotificationsUseCase("user123") { result ->
            result.onSuccess { domainNotifications ->
                val items = domainNotifications.map { domain ->
                    NotificationItem(
                        id = domain.notificationId,
                        title = domain.title,
                        description = domain.body,
                        flightCode = if (domain.title.contains(
                                "AA123",
                                ignoreCase = true
                            )
                        ) "AA123" else null,
                        timeAgo = formatTimeAgo(domain.createdAt),
                        isRead = domain.isRead,
                        type = mapToUiType(domain.type),
                        createdAt = domain.createdAt
                    )
                }
                
                val grouped = groupNotifications(items)
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        groupedNotifications = grouped,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load notifications"
                    )
                }
            }
        }
    }

    private fun groupNotifications(items: List<NotificationItem>): Map<String, List<NotificationItem>> {
        val now = Calendar.getInstance()
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val yesterday = (today.clone() as Calendar).apply { add(Calendar.DATE, -1) }
        val thisWeek = (today.clone() as Calendar).apply { add(Calendar.DATE, -7) }

        return items.groupBy { item ->
            val itemCal = Calendar.getInstance().apply { timeInMillis = item.createdAt }
            when {
                itemCal.after(today) -> "Today"
                itemCal.after(yesterday) -> "Yesterday"
                itemCal.after(thisWeek) -> "This Week"
                else -> "Earlier"
            }
        }
    }

    fun markAllAsRead() {
        markAllNotificationsReadUseCase("user123") { result ->
            if (result.isSuccess) {
                loadNotifications()
            }
        }
    }

    private fun mapToUiType(domainType: com.example.domain.model.NotificationType): NotificationType {
        return when (domainType) {
            com.example.domain.model.NotificationType.BOARDING_REMINDER -> NotificationType.BOARDING
            com.example.domain.model.NotificationType.CHECK_IN_CONFIRMATION -> NotificationType.CHECK_IN
            else -> NotificationType.DOCUMENT
        }
    }

    private fun formatTimeAgo(createdAt: Long): String {
        val diff = System.currentTimeMillis() - createdAt
        val minutes = diff / (1000 * 60)
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            else -> "${days}d ago"
        }
    }
}
