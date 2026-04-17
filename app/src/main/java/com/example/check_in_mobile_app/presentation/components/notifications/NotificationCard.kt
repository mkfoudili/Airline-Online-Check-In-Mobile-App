package com.example.check_in_mobile_app.presentation.components.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.presentation.notifications.NotificationItem
import com.example.check_in_mobile_app.presentation.notifications.NotificationType
import com.example.check_in_mobile_app.ui.theme.*

@Composable
fun NotificationCard(
    notification: NotificationItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val isUnread = !notification.isRead
    val borderColor = if (isUnread) NavyBlue else BorderColor
    val iconBackground = if (isUnread) NavyBlue else LightGray
    val iconTint = if (isUnread) Color.White else NavyBlue

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isUnread) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Icon Box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getNotificationIcon(notification.type),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = notification.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Slate500,
                        lineHeight = 20.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    notification.flightCode?.let { code ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MediumGray),
                            color = Color.Transparent
                        ) {
                            Text(
                                text = code,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = notification.timeAgo,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MediumGray
                        )
                    )
                }
            }

            // Right Chevron
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                tint = BorderColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.BOARDING -> Icons.Outlined.History
        NotificationType.CHECK_IN -> Icons.AutoMirrored.Outlined.Assignment
        NotificationType.DOCUMENT -> Icons.AutoMirrored.Outlined.LibraryBooks
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationCardPreview() {
    Column(
        modifier = Modifier
            .background(SurfaceGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NotificationCard(
            notification = NotificationItem(
                id = "1",
                title = "Boarding Starts in 30m",
                description = "Prepare your boarding pass and ID. Boarding for Group 1 will start soon.",
                flightCode = "AA241",
                timeAgo = "45m ago",
                isRead = false,
                type = NotificationType.BOARDING
            )
        )
        NotificationCard(
            notification = NotificationItem(
                id = "2",
                title = "Check-in Confirmed",
                description = "Check-in successful! Your seat 14C is confirmed. View your boarding pass.",
                flightCode = "AA241",
                timeAgo = "2h ago",
                isRead = true,
                type = NotificationType.CHECK_IN
            )
        )
    }
}
