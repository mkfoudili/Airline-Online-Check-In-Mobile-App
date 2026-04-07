package com.example.check_in_mobile_app.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.presentation.components.SectionLabel
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.components.profile.ProfileAvatar
import com.example.check_in_mobile_app.presentation.components.profile.ProfileInfoCard
import com.example.check_in_mobile_app.presentation.components.profile.SecurityStatusBanner
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Poppins

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onTabSelected: (TabItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onTabSelected = onTabSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profile",
                            fontFamily = Poppins,
                            fontSize = 22.sp,
                            color = NavyBlue,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            }
        },
        bottomBar = {
            TabBarMenu(
                selectedTab = TabItem.PROFILE,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Avatar Section
            ProfileAvatar(
                isOnline = uiState.isOnline
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Name
            Text(
                text = uiState.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue,
                fontFamily = Poppins,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Personal Information Section
            SectionLabel(
                text = "PERSONAL INFORMATION",
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            ProfileInfoCard(
                email = uiState.email,
                phoneNumber = uiState.phoneNumber,
                onEditEmailClick = {
                    onEvent(ProfileEvent.OnEditEmailClicked)
                },
                onEditPhoneClick = {
                    onEvent(ProfileEvent.OnEditPhoneClicked)
                },
                onEditPasswordClick = {
                    onEvent(ProfileEvent.OnEditPasswordClicked)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SecurityStatusBanner()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreenContent(
        uiState = ProfileUiState(
            name = "Djerfi Fatima",
            email = "mr_mikircha@esi.dz",
            phoneNumber = "+1 (555) 012-3456",
            isOnline = true
        )
    )
}
