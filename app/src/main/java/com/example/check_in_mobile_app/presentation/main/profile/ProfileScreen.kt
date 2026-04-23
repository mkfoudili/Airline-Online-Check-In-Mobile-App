package com.example.check_in_mobile_app.presentation.main.profile

import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.example.check_in_mobile_app.presentation.main.MainActivity
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Poppins
import com.example.check_in_mobile_app.ui.theme.SubtleText
import com.example.check_in_mobile_app.ui.theme.DividerColor
import com.example.check_in_mobile_app.ui.theme.SurfaceGray
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
    onTabSelected: (TabItem) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiAction.collectLatest { action ->
            when (action) {
                is ProfileUiAction.ChangeLanguage -> {
                    // Trigger the activity recreation through the companion callback
                    MainActivity.onLanguageChangeRequest?.invoke(action.languageCode)
                }
                ProfileUiAction.NavigateBack -> {
                    // Back logic handled by viewmodel internal state flags first
                }
            }
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        when {
            uiState.isChangingPassword -> {
                ChangePasswordScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent
                )
            }
            uiState.isEditing -> {
                EditProfileScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent
                )
            }
            else -> {
                ProfileScreenContent(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onTabSelected = onTabSelected,
                    onLogout = onLogout
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileBaseScreen(
    title: String,
    titleFontSize: Int = 18,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            fontFamily = Poppins,
                            fontSize = titleFontSize.sp,
                            color = NavyBlue,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        if (onBackClick != null) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.common_back),
                                    tint = NavyBlue
                                )
                            }
                        }
                    },
                    actions = actions,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
            }
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
private fun ProfileBottomActions(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    secondaryText: String,
    onSecondaryClick: () -> Unit,
    primaryIcon: @Composable (() -> Unit)? = null,
    secondaryIcon: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
        Spacer(modifier = Modifier.height(24.dp))

        ProfileActionButton(
            text = primaryText,
            onClick = onPrimaryClick,
            icon = primaryIcon
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileSecondaryActionButton(
            text = secondaryText,
            onClick = onSecondaryClick,
            icon = secondaryIcon
        )
    }
}

@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    ProfileBaseScreen(
        title = stringResource(R.string.profile_title),
        titleFontSize = 22,
        actions = {
            TextButton(onClick = { onEvent(ProfileEvent.OnEditProfileClicked) }) {
                Text(text = stringResource(R.string.common_edit), color = MaterialTheme.colorScheme.primary)
            }
        },
        bottomBar = {
            TabBarMenu(
                selectedTab = TabItem.PROFILE,
                onTabSelected = onTabSelected
            )
        }
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
                contentDescription = stringResource(R.string.profile_edit_name_desc),
                modifier = Modifier.size(20.dp),
                tint = NavyBlue.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Personal Information Section
        SectionLabel(
            text = stringResource(R.string.profile_personal_info_label),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileInfoCard(
            email = uiState.email,
            phoneNumber = uiState.phoneNumber,
            language = uiState.language,
            onEditEmailClick = {
                onEvent(ProfileEvent.OnEditEmailClicked)
            },
            onEditPhoneClick = {
                onEvent(ProfileEvent.OnEditPhoneClicked)
            },
            onEditPasswordClick = {
                onEvent(ProfileEvent.OnEditPasswordClicked)
            },
            onEditLanguageClick = {
                onEvent(ProfileEvent.OnEditProfileClicked)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SecurityStatusBanner()

        Spacer(modifier = Modifier.height(32.dp))

        ProfileSecondaryActionButton(
            text = stringResource(R.string.profile_logout),
            onClick = onLogout,
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(R.string.profile_logout),
                    modifier = Modifier.size(18.dp),
                    tint = NavyBlue
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun EditProfileScreen(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    ProfileBaseScreen(
        title = stringResource(R.string.profile_edit_title),
        onBackClick = { onEvent(ProfileEvent.OnBackClicked) },
        bottomBar = {
            ProfileBottomActions(
                primaryText = stringResource(R.string.common_save_changes),
                onPrimaryClick = { onEvent(ProfileEvent.OnSaveClicked) },
                secondaryText = stringResource(R.string.common_cancel),
                onSecondaryClick = { onEvent(ProfileEvent.OnCancelClicked) },
                primaryIcon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                secondaryIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = NavyBlue
                    )
                }
            )
        }
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
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.profile_change_photo_desc),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.profile_change_photo_label),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = NavyBlue
            ),
            modifier = Modifier.clickable { onEvent(ProfileEvent.OnChangePhotoClicked) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        BookingInputField(
            label = stringResource(R.string.common_full_name),
            value = uiState.editedName,
            placeholder = stringResource(R.string.profile_full_name_placeholder),
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
            label = stringResource(R.string.common_email_address),
            value = uiState.editedEmail,
            placeholder = stringResource(R.string.profile_email_placeholder),
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
            label = stringResource(R.string.common_phone_number),
            value = uiState.editedPhoneNumber,
            placeholder = stringResource(R.string.profile_phone_placeholder),
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

        Spacer(modifier = Modifier.height(20.dp))

        LanguageDropdownField(
            selectedLanguage = uiState.editedLanguage,
            isExpanded = uiState.isLanguageDropdownExpanded,
            onToggle = { onEvent(ProfileEvent.OnToggleLanguageDropdown) },
            onLanguageSelected = { onEvent(ProfileEvent.OnLanguageChanged(it)) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        SectionLabel(
            text = stringResource(R.string.common_security),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(ProfileEvent.OnEditPasswordClicked) },
            shape = RoundedCornerShape(12.dp),
            color = SurfaceGray,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BorderLight),
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
                        text = stringResource(R.string.common_change_password),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyBlue
                        )
                    )
                    Text(
                        text = stringResource(R.string.profile_keep_account_secure),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = SubtleText
                        )
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = SubtleText
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdownField(
    selectedLanguage: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf("English", "French", "Arabic")

    Column {
        Text(
            text = stringResource(R.string.profile_language_label),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = SubtleText,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { onToggle() },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedLanguage,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = DarkText.copy(alpha = 0.6f)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = BorderLight,
                    focusedTextColor = DarkText,
                    unfocusedTextColor = DarkText,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = onToggle,
                modifier = Modifier.background(Color.White)
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = language,
                                style = MaterialTheme.typography.bodyLarge,
                                color = DarkText
                            )
                        },
                        onClick = {
                            onLanguageSelected(language)
                        },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChangePasswordScreen(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    ProfileBaseScreen(
        title = stringResource(R.string.profile_change_password_title),
        onBackClick = { onEvent(ProfileEvent.OnBackClicked) },
        bottomBar = {
            ProfileBottomActions(
                primaryText = stringResource(R.string.profile_save_password),
                onPrimaryClick = { onEvent(ProfileEvent.OnSavePasswordClicked) },
                secondaryText = stringResource(R.string.common_cancel),
                onSecondaryClick = { onEvent(ProfileEvent.OnCancelClicked) }
            )
        }
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Security Info Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFEF9C3).copy(alpha = 0.3f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF08A))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFFA16207),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(R.string.profile_secure_your_account),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA16207)
                        )
                    )
                    Text(
                        text = stringResource(R.string.profile_password_requirement_hint),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFA16207).copy(alpha = 0.7f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        PasswordInputField(
            label = stringResource(R.string.profile_current_password_label),
            value = uiState.currentPassword,
            placeholder = stringResource(R.string.profile_current_password_placeholder),
            onValueChange = { onEvent(ProfileEvent.OnCurrentPasswordChanged(it)) },
            isVisible = uiState.isCurrentPasswordVisible,
            onToggleVisibility = { onEvent(ProfileEvent.OnToggleCurrentPasswordVisibility) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        PasswordInputField(
            label = stringResource(R.string.profile_new_password_label),
            value = uiState.newPassword,
            placeholder = stringResource(R.string.profile_new_password_placeholder),
            onValueChange = { onEvent(ProfileEvent.OnNewPasswordChanged(it)) },
            isVisible = uiState.isNewPasswordVisible,
            onToggleVisibility = { onEvent(ProfileEvent.OnToggleNewPasswordVisibility) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        PasswordInputField(
            label = stringResource(R.string.profile_confirm_password_label),
            value = uiState.confirmPassword,
            placeholder = stringResource(R.string.profile_confirm_password_placeholder),
            onValueChange = { onEvent(ProfileEvent.OnConfirmPasswordChanged(it)) },
            isVisible = uiState.isConfirmPasswordVisible,
            onToggleVisibility = { onEvent(ProfileEvent.OnToggleConfirmPasswordVisibility) }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun PasswordInputField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    BookingInputField(
        label = label,
        value = value,
        placeholder = placeholder,
        onValueChange = onValueChange,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = DarkText.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (isVisible) stringResource(R.string.common_hide_password) else stringResource(R.string.common_show_password),
                    tint = DarkText.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    )
}
