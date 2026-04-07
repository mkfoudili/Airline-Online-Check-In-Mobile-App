package com.example.check_in_mobile_app.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.BookingInputField
import com.example.check_in_mobile_app.presentation.components.ProfileActionButton
import com.example.check_in_mobile_app.presentation.components.ProfileSecondaryActionButton
import com.example.check_in_mobile_app.presentation.components.SectionLabel
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.components.profile.ProfileAvatar
import com.example.check_in_mobile_app.presentation.components.profile.ProfileInfoCard
import com.example.check_in_mobile_app.presentation.components.profile.SecurityStatusBanner
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Poppins
import com.example.check_in_mobile_app.ui.theme.SubtleText

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onTabSelected: (TabItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isEditing) {
        EditProfileScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    } else {
        ProfileScreenContent(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onTabSelected = onTabSelected
        )
    }
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
                    actions = {
                        TextButton(onClick = { onEvent(ProfileEvent.OnEditProfileClicked) }) {
                            Text(text = "Edit", color = MaterialTheme.colorScheme.primary)
                        }
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

            // User Name with Edit Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onEvent(ProfileEvent.OnEditProfileClicked) }
            ) {
                Text(
                    text = uiState.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    fontFamily = Poppins,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Name",
                    modifier = Modifier.size(20.dp),
                    tint = NavyBlue.copy(alpha = 0.5f)
                )
            }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Edit Profile",
                            fontFamily = Poppins,
                            fontSize = 18.sp,
                            color = NavyBlue,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onEvent(ProfileEvent.OnBackClicked) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = NavyBlue
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            }
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
            Box(contentAlignment = Alignment.BottomEnd) {
                ProfileAvatar(
                    isOnline = false
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(NavyBlue)
                        .padding(8.dp)
                        .clickable { onEvent(ProfileEvent.OnChangePhotoClicked) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, // Replace with Camera icon if available
                        contentDescription = "Change Photo",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Change Profile Photo",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = NavyBlue,
                modifier = Modifier.clickable { onEvent(ProfileEvent.OnChangePhotoClicked) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            BookingInputField(
                label = "Full Name",
                value = uiState.editedName,
                placeholder = "Enter your full name",
                onValueChange = { onEvent(ProfileEvent.OnNameChanged(it)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = DarkText.copy(alpha = 0.6f)
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            BookingInputField(
                label = "Email Address",
                value = uiState.editedEmail,
                placeholder = "Enter your email",
                onValueChange = { onEvent(ProfileEvent.OnEmailChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = DarkText.copy(alpha = 0.6f)
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            BookingInputField(
                label = "Phone Number",
                value = uiState.editedPhoneNumber,
                placeholder = "Enter your phone number",
                onValueChange = { onEvent(ProfileEvent.OnPhoneNumberChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = DarkText.copy(alpha = 0.6f)
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            SectionLabel(
                text = "Security",
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(ProfileEvent.OnEditPasswordClicked) },
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = NavyBlue
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1.0f)) {
                        Text(
                            text = "Change Password",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyBlue
                        )
                        Text(
                            text = "Keep your account secure",
                            fontSize = 12.sp,
                            color = SubtleText
                        )
                    }
                    
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = SubtleText
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            ProfileActionButton(
                text = "Save Changes",
                onClick = { onEvent(ProfileEvent.OnSaveClicked) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileSecondaryActionButton(
                text = "Cancel",
                onClick = { onEvent(ProfileEvent.OnCancelClicked) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = NavyBlue
                    )
                }
            )
            
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

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        uiState = ProfileUiState(
            isEditing = true,
            editedName = "Djerfi Fatima",
            editedEmail = "mr_mikircha@esi.dz",
            editedPhoneNumber = "+1 (555) 012-4567"
        ),
        onEvent = {}
    )
}