package com.example.check_in_mobile_app.presentation.components.boarding

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.check_in_mobile_app.ui.theme.BorderLight

@Composable
private fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color(0xFFE8EDF2),
        Color(0xFFF5F7FA),
        Color(0xFFE8EDF2)
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offset by transition.animateFloat(
        initialValue = -300f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(offset, offset),
        end = Offset(offset + 300f, offset + 300f)
    )
}

@Composable
fun ShimmerBox(modifier: Modifier) {
    val brush = shimmerBrush()
    Box(modifier = modifier.background(brush, RoundedCornerShape(8.dp)))
}

@Composable
fun BoardingPassSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFE2E8F0))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Route row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerBox(Modifier.width(72.dp).height(44.dp))
                ShimmerBox(Modifier.width(40.dp).height(24.dp).align(Alignment.CenterVertically))
                ShimmerBox(Modifier.width(72.dp).height(44.dp))
            }
            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ShimmerBox(Modifier.width(60.dp).height(36.dp))
                ShimmerBox(Modifier.width(60.dp).height(36.dp))
            }
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ShimmerBox(Modifier.width(80.dp).height(36.dp))
                ShimmerBox(Modifier.width(40.dp).height(36.dp))
            }
            Spacer(Modifier.height(16.dp))
            ShimmerBox(
                Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(24.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFFF8FAFC))
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShimmerBox(Modifier.width(120.dp).height(20.dp))
            ShimmerBox(Modifier.width(90.dp).height(28.dp))
        }
    }
}