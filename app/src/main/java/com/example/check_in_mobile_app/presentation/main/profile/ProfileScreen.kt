package com.example.check_in_mobile_app.presentation.main.profile

import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.core.os.LocaleListCompat
import com.example.check_in_mobile_app.utils.LanguagePreferences
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
                    // Save to preferences for legacy/backup support
                    LanguagePreferences.saveLanguage(context, action.languageCode)
                    
                    // Trigger global language change
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(action.languageCode)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
                ProfileUiAction.NavigateBack -> {
                    // Back logic
                }
                ProfileUiAction.Logout -> {
                    onLogout()
                }
            }
        }
    }

    if (uiState.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(ProfileEvent.OnLogoutDismissClicked) },
            title = { Text(text = stringResource(R.string.profile_logout_confirm_title)) },
            text = { Text(text = stringResource(R.string.profile_logout_confirm_message)) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(ProfileEvent.OnLogoutConfirmClicked) }) {
                    Text(text = stringResource(R.string.common_logout), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(ProfileEvent.OnLogoutDismissClicked) }) {
                    Text(text = stringResource(R.string.common_cancel))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
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
                    onTabSelected = onTabSelected
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
    onTabSelected: (TabItem) -> Unit = {}
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
            onClick = { onEvent(ProfileEvent.OnLogoutClicked) },
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
                        contentDescription = null
                    )
                },
                secondaryIcon = {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null
                    )
                }
            )
        }
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Form fields for editing profile
        BookingInputField(
            label = stringResource(R.string.profile_name_label),
            value = uiState.editedName,
            onValueChange = { onEvent(ProfileEvent.OnNameChanged(it)) },
            placeholder = stringResource(R.string.profile_name_placeholder)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        BookingInputField(
            label = stringResource(R.string.profile_email_label),
            value = uiState.editedEmail,
            onValueChange = { onEvent(ProfileEvent.OnEmailChanged(it)) },
            placeholder = stringResource(R.string.profile_email_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        BookingInputField(
            label = stringResource(R.string.profile_phone_label),
            value = uiState.editedPhoneNumber,
            onValueChange = { onEvent(ProfileEvent.OnPhoneNumberChanged(it)) },
            placeholder = stringResource(R.string.profile_phone_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Language Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            BookingInputField(
                label = stringResource(R.string.profile_language_label),
                value = uiState.editedLanguage,
                onValueChange = { },
                enabled = false,
                placeholder = "",
                trailingIcon = {
                    IconButton(onClick = { onEvent(ProfileEvent.OnToggleLanguageDropdown) }) {
                        Icon(
                            imageVector = if (uiState.isLanguageDropdownExpanded) 
                                Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.clickable { onEvent(ProfileEvent.OnToggleLanguageDropdown) }
            )
            
            DropdownMenu(
                expanded = uiState.isLanguageDropdownExpanded,
                onDismissRequest = { onEvent(ProfileEvent.OnToggleLanguageDropdown) },
                modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
            ) {
                LanguagePreferences.getAvailableLanguages().forEach { language ->
                    DropdownMenuItem(
                        text = { Text(language) },
                        onClick = { onEvent(ProfileEvent.OnLanguageChanged(language)) }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
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
                primaryText = stringResource(R.string.common_update_password),
                onPrimaryClick = { onEvent(ProfileEvent.OnSavePasswordClicked) },
                secondaryText = stringResource(R.string.common_cancel),
                onSecondaryClick = { onEvent(ProfileEvent.OnCancelClicked) },
                primaryIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                secondaryIcon = {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null
                    )
                }
            )
        }
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        BookingInputField(
            label = stringResource(R.string.profile_current_password_label),
            value = uiState.currentPassword,
            onValueChange = { onEvent(ProfileEvent.OnCurrentPasswordChanged(it)) },
            placeholder = stringResource(R.string.profile_current_password_placeholder),
            visualTransformation = if (uiState.isCurrentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { onEvent(ProfileEvent.OnToggleCurrentPasswordVisibility) }) {
                    Icon(
                        imageVector = if (uiState.isCurrentPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        BookingInputField(
            label = stringResource(R.string.profile_new_password_label),
            value = uiState.newPassword,
            onValueChange = { onEvent(ProfileEvent.OnNewPasswordChanged(it)) },
            placeholder = stringResource(R.string.profile_new_password_placeholder),
            visualTransformation = if (uiState.isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { onEvent(ProfileEvent.OnToggleNewPasswordVisibility) }) {
                    Icon(
                        imageVector = if (uiState.isNewPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        BookingInputField(
            label = stringResource(R.string.profile_confirm_password_label),
            value = uiState.confirmPassword,
            onValueChange = { onEvent(ProfileEvent.OnConfirmPasswordChanged(it)) },
            placeholder = stringResource(R.string.profile_confirm_password_placeholder),
            visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { onEvent(ProfileEvent.OnToggleConfirmPasswordVisibility) }) {
                    Icon(
                        imageVector = if (uiState.isConfirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
