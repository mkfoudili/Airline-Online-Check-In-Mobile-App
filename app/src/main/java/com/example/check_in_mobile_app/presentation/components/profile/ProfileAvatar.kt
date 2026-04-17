package com.example.check_in_mobile_app.presentation.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.ActiveGreen
import com.example.check_in_mobile_app.ui.theme.LightGray

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    imageRes: Int = R.drawable.user,
    avatarSize: Dp = 120.dp,
    badgeSize: Dp = 24.dp,
    isOnline: Boolean = true
) {
    Box(
        modifier = modifier.size(avatarSize),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(LightGray)
                .border(2.dp, Color(0xFFF1F5F9), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(avatarSize),
                contentScale = ContentScale.Crop
            )
        }

        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(badgeSize)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(ActiveGreen)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileAvatarPreview() {
    Box(modifier = Modifier.padding(24.dp)) {
        ProfileAvatar()
    }
}
