package com.example.check_in_mobile_app.presentation.checkin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.HearingDisabled
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.checkin.CheckInTopBar
import com.example.check_in_mobile_app.presentation.components.checkin.PreferenceCard
import com.example.check_in_mobile_app.presentation.components.checkin.PreferenceSectionLabel
import com.example.check_in_mobile_app.presentation.components.checkin.SafetyNoteCard
import com.example.check_in_mobile_app.ui.theme.NavyBlue

@Composable
fun specialRequest(
    onNavigateBack: () -> Unit = {},
    onFinishCheckIn: () -> Unit = {}
) {
    var specialMeal by remember { mutableStateOf(false) }
    var wheelchairSupport by remember { mutableStateOf(false) }
    var visualHearing by remember { mutableStateOf(false) }
    var infantOnLap by remember { mutableStateOf(false) }
    var serviceAnimal by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CheckInTopBar(
                onBack = onNavigateBack,
                currentStep = 5,
                title = stringResource(R.string.checkin_step5_title)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = onFinishCheckIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                    ) {
                        Text(
                            text = stringResource(R.string.checkin_finish_checkin),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
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
                    text = stringResource(R.string.checkin_final_preferences),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = stringResource(R.string.checkin_preferences_subtitle),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
            }

            item {
                PreferenceSectionLabel(text = stringResource(R.string.checkin_dietary_requirements))
                PreferenceCard(
                    icon = Icons.Default.Restaurant,
                    iconTint = Color(0xFF9C6B2E),
                    iconBackground = Color(0xFFFFF3E0),
                    title = stringResource(R.string.checkin_special_meal),
                    description = stringResource(R.string.checkin_special_meal_desc),
                    checked = specialMeal,
                    onCheckedChange = { specialMeal = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceSectionLabel(text = stringResource(R.string.checkin_special_assistance))
                PreferenceCard(
                    icon = Icons.Default.Accessible,
                    iconTint = Color(0xFF3B6B9E),
                    iconBackground = Color(0xFFE3F0FB),
                    title = stringResource(R.string.checkin_wheelchair_support),
                    description = stringResource(R.string.checkin_wheelchair_support_desc),
                    checked = wheelchairSupport,
                    onCheckedChange = { wheelchairSupport = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceCard(
                    icon = Icons.Default.HearingDisabled,
                    iconTint = Color(0xFF7B5EA7),
                    iconBackground = Color(0xFFF3EEFB),
                    title = stringResource(R.string.checkin_visual_hearing),
                    description = stringResource(R.string.checkin_visual_hearing_desc),
                    checked = visualHearing,
                    onCheckedChange = { visualHearing = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceSectionLabel(text = stringResource(R.string.checkin_travel_companions))
                PreferenceCard(
                    icon = Icons.Default.ChildCare,
                    iconTint = Color(0xFF9C6B2E),
                    iconBackground = Color(0xFFFFF3E0),
                    title = stringResource(R.string.checkin_infant_on_lap),
                    description = stringResource(R.string.checkin_infant_on_lap_desc),
                    checked = infantOnLap,
                    onCheckedChange = { infantOnLap = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreferenceCard(
                    icon = Icons.Default.Pets,
                    iconTint = Color(0xFF3B7A57),
                    iconBackground = Color(0xFFE6F4EC),
                    title = stringResource(R.string.checkin_service_animal),
                    description = stringResource(R.string.checkin_service_animal_desc),
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