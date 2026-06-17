package com.hamdhan.rahathtravels.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hamdhan.rahathtravels.authentication.BlackBg
import com.hamdhan.rahathtravels.authentication.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    onNavigateBack: () -> Unit,
    bookingViewModel: BookingViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var numberOfPersons by remember { mutableStateOf("") }
    var preferredDate by remember { mutableStateOf("") }
    var specialRequests by remember { mutableStateOf("") }
    var selectedPackage by remember { mutableStateOf("") }
    var packageExpanded by remember { mutableStateOf(false) }

    val packageOptions = listOf("Hajj Package", "Umrah Package", "Hajj + Umrah Combined")

    val bookingState by bookingViewModel.bookingState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(bookingState) {
        when (bookingState) {
            is BookingState.Success -> {
                Toast.makeText(
                    context,
                    "Booking Successful! We'll contact you soon 🎉",
                    Toast.LENGTH_LONG
                ).show()
                bookingViewModel.resetState()
                onNavigateBack()
            }
            is BookingState.Error -> {
                Toast.makeText(
                    context,
                    (bookingState as BookingState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
                bookingViewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        containerColor = BlackBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New Booking",
                        color = Gold,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Gold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF111111)
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = "Fill in your details",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f)
            )

            // Full Name
            BookingTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full Name",
                icon = Icons.Default.Person
            )

            // Email
            BookingTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                icon = Icons.Default.Email
            )

            // Phone
            BookingTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone Number",
                icon = Icons.Default.Phone
            )

            // Package Type Dropdown
            ExposedDropdownMenuBox(
                expanded = packageExpanded,
                onExpandedChange = { packageExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedPackage,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            "Package Type",
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Gold
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = packageExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Gold
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = packageExpanded,
                    onDismissRequest = { packageExpanded = false },
                    modifier = Modifier.background(Color(0xFF1A1A1A))
                ) {
                    packageOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option, color = Color.White)
                            },
                            onClick = {
                                selectedPackage = option
                                packageExpanded = false
                            }
                        )
                    }
                }
            }

            // Number of Persons
            BookingTextField(
                value = numberOfPersons,
                onValueChange = { numberOfPersons = it },
                label = "Number of Persons",
                icon = Icons.Default.Person
            )

            // Preferred Date
            BookingTextField(
                value = preferredDate,
                onValueChange = { preferredDate = it },
                label = "Preferred Date (DD/MM/YYYY)",
                icon = Icons.Default.DateRange
            )

            // Special Requests
            OutlinedTextField(
                value = specialRequests,
                onValueChange = { specialRequests = it },
                label = {
                    Text(
                        "Special Requests (Optional)",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Gold
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Gold,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Gold
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Submit Button
            if (bookingState is BookingState.Loading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Gold)
                }
            } else {
                Button(
                    onClick = {
                        bookingViewModel.submitBooking(
                            fullName = fullName,
                            email = email,
                            phone = phone,
                            packageType = selectedPackage,
                            numberOfPersons = numberOfPersons,
                            preferredDate = preferredDate,
                            specialRequests = specialRequests
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Confirm Booking",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// Reusable text field for booking form
@Composable
fun BookingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, color = Color.White.copy(alpha = 0.6f))
        },
        leadingIcon = {
            Icon(icon, contentDescription = null, tint = Gold)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Gold,
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Gold
        ),
        shape = RoundedCornerShape(12.dp)
    )
}