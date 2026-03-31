package com.example.check_in_mobile_app.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.R

private val LightGray = Color(0xFFF3F4F6)
private val MediumGray = Color(0xFF9CA3AF)
private val DarkText = Color(0xFF111827)
private val ActiveGreen = Color(0xFF34D399)
private val BadgeBg = Color(0x26FFFFFF)
private val BadgeBorder = Color(0x4DFFFFFF)
private val SubtitleWhite = Color(0xBFFFFFFF)
private val InputBorder = Color(0xFFE5E7EB)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onCheckInClick: () -> Unit = {},
    onFindFlightClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(TabItem.HOME) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            TabBarMenu(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, ${uiState.userName}",
                        fontSize = 25.sp,
                        color = Color(0xFF2A3343),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profile",
                        tint = MediumGray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier.height(1.dp)
                    .background(color = Color(0xFFF1F2F4))
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            ActiveFlightCard(
                destination = uiState.flightDestination,
                onCheckInClick = {
                    viewModel.onCheckInNow()
                    onCheckInClick()
                },
                uiState = uiState
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Retrieve Booking",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF25316B),
                letterSpacing = (-0.2).sp
            )

            Spacer(modifier = Modifier.height(18.dp))
            Column(
                modifier = Modifier
                    .height(300.dp)
                    .width(358.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFF1F2F4), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                BookingInputField(
                    label = "BOOKING REFERENCE",
                    value = uiState.bookingReference,
                    placeholder = "e.g. AB1234",
                    onValueChange = viewModel::onBookingReferenceChange,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = MediumGray,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                BookingInputField(
                    label = "LAST NAME",
                    value = uiState.lastName,
                    placeholder = "As written in passport",
                    onValueChange = viewModel::onLastNameChange,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = {
                        viewModel.onFindFlight()
                        onFindFlightClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25316B),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Find My Flight",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ActiveFlightCard(
    destination: String,
    onCheckInClick: () -> Unit,
    uiState: HomeUiState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF25316B))
            .padding(22.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.plane),
            contentScale = ContentScale.Crop,
            contentDescription = "Plane",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .offset(137.dp, 25.dp)
        )
        Column {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(BadgeBg)
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(ActiveGreen)
                )
                Text(
                    text = "ACTIVE NOW",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ready to fly?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = (-0.3).sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            if(uiState.isCheckInActive) {
                Text(
                    text = "Check-in is open for your flight to $destination.",
                    fontSize = 13.sp,
                    color = SubtitleWhite,
                    lineHeight = 19.sp,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
            }
            else {
                Text(
                    text = "Book a flight and check In online with Flight !",
                    fontSize = 13.sp,
                    color = SubtitleWhite,
                    lineHeight = 19.sp,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            if(uiState.isCheckInActive) {
                Button(
                    onClick = onCheckInClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF25316B)
                    ),
                    contentPadding = PaddingValues(horizontal = 22.dp, vertical = 11.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Check-In Now",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingInputField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0x992A3343),
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 14.sp,
                    color = Color(0xFF2A3343)
                )
            },
            leadingIcon = leadingIcon,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2A3343),
                unfocusedBorderColor = InputBorder,
                focusedTextColor = DarkText,
                unfocusedTextColor = DarkText,
                cursorColor = Color(0xFF2A3343),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}