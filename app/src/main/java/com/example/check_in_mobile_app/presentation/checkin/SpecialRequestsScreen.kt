package com.example.check_in_mobile_app.presentation.checkin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.HearingDisabled
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.checkin.PreferenceCard
import com.example.check_in_mobile_app.presentation.components.checkin.PreferenceSectionLabel
import com.example.check_in_mobile_app.presentation.components.checkin.ProgressBar
import com.example.check_in_mobile_app.presentation.components.checkin.SafetyNoteCard
import com.example.check_in_mobile_app.ui.theme.NavyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun specialRequest(
    onNavigateBack: () -> Unit = {},
    onFinishCheckIn: () -> Unit = {}

){
    var specialMeal by remember { mutableStateOf(false) }
    var wheelchairSupport by remember { mutableStateOf(false) }
    var visualHearing by remember { mutableStateOf(false) }
    var infantOnLap by remember { mutableStateOf(false) }
    var serviceAnimal by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Step 5: Requests",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyBlue
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                painter = painterResource(id = R.drawable.chevron_left),
                                contentDescription = "Back",
                                tint = NavyBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
                ProgressBar(
                    progress = 1f,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        },

        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onFinishCheckIn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyBlue
                    )
                ) {
                    Text(
                        text = "Finish Check-In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        },
        containerColor = Color(0xFFF9FAFA)

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Final Preferences",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Customize your journey with additional services and assistance.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
            }

            item {
                PreferenceSectionLabel(text = "Dietary Requirements")
                PreferenceCard(
                    icon = Icons.Default.Restaurant,
                    iconTint = Color(0xFF9C6B2E),
                    iconBackground = Color(0xFFFFF3E0),
                    title = "Special Meal",
                    description = "Request vegetarian, vegan, or allergy-friendly meals.",
                    checked = specialMeal,
                    onCheckedChange = { specialMeal = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceSectionLabel(text = "Special Assistance")
                PreferenceCard(
                    icon = Icons.Default.Accessible,
                    iconTint = Color(0xFF3B6B9E),
                    iconBackground = Color(0xFFE3F0FB),
                    title = "Wheelchair Support",
                    description = "Assistance from check-in to your seat on the plane.",
                    checked = wheelchairSupport,
                    onCheckedChange = { wheelchairSupport = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceCard(
                    icon = Icons.Default.HearingDisabled,
                    iconTint = Color(0xFF7B5EA7),
                    iconBackground = Color(0xFFF3EEFB),
                    title = "Visual & Hearing",
                    description = "Support for passengers with visual or hearing impairments.",
                    checked = visualHearing,
                    onCheckedChange = { visualHearing = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceSectionLabel(text = "Travel Companions")
                PreferenceCard(
                    icon = Icons.Default.ChildCare,
                    iconTint = Color(0xFF9C6B2E),
                    iconBackground = Color(0xFFFFF3E0),
                    title = "Infant on Lap",
                    description = "Traveling with a child under 2 years of age.",
                    checked = infantOnLap,
                    onCheckedChange = { infantOnLap = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceCard(
                    icon = Icons.Default.Pets,
                    iconTint = Color(0xFF3B7A57),
                    iconBackground = Color(0xFFE6F4EC),
                    title = "Service Animal",
                    description = "Certified assistance animals allowed in cabin.",
                    checked = serviceAnimal,
                    onCheckedChange = { serviceAnimal = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SafetyNoteCard()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}