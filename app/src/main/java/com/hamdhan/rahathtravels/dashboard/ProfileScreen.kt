package com.hamdhan.rahathtravels.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.hamdhan.rahathtravels.authentication.BlackBg
import com.hamdhan.rahathtravels.authentication.Gold
import java.io.File

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val context = LocalContext.current

    val userEmail = currentUser?.email ?: "No email"
    val userId = currentUser?.uid ?: "No ID"
    val avatarLetter = userEmail.firstOrNull()?.uppercaseChar() ?: 'U'

    var showLogoutDialog by remember { mutableStateOf(false) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var locationText by remember { mutableStateOf("Tap button to get location") }
    var isLoadingLocation by remember { mutableStateOf(false) }

    val photoFile = remember {
        File(context.cacheDir, "profile_photo.jpg").also {
            if (!it.exists()) it.createNewFile()
        }
    }

    val photoUri: Uri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            profileImageUri = photoUri
            Toast.makeText(context, "Profile photo updated!", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(photoUri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isLoadingLocation = true
            try {
                val client = LocationServices.getFusedLocationProviderClient(context)
                client.lastLocation
                    .addOnSuccessListener { location ->
                        isLoadingLocation = false
                        locationText = if (location != null) {
                            "Lat: ${"%.4f".format(location.latitude)}, " +
                                    "Lng: ${"%.4f".format(location.longitude)}"
                        } else {
                            "Location not available"
                        }
                    }
                    .addOnFailureListener {
                        isLoadingLocation = false
                        locationText = "Failed to get location"
                    }
            } catch (e: SecurityException) {
                isLoadingLocation = false
                locationText = "Permission error"
            }
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = Color(0xFF1A1A1A),
            title = {
                Text("Logout", color = Gold, fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Are you sure you want to logout?", color = Color.White)
            },
            confirmButton = {
                Button(
                    onClick = {
                        auth.signOut()
                        Toast.makeText(context, "Logged out!", Toast.LENGTH_SHORT).show()
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Gold)
                ) {
                    Text("Logout", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Gold)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBg)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Back Arrow Button
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { onNavigateBack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Gold
                )
            }
            Text(
                text = "My Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Photo with Camera button
        Box(contentAlignment = Alignment.BottomEnd) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Gold, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Gold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatarLetter.toString(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // Camera button — tap to take photo
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Gold, CircleShape)
                    .clickable {
                        val granted = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                        if (granted) {
                            cameraLauncher.launch(photoUri)
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Take Photo",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userEmail,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Rahath Travels Member",
            fontSize = 14.sp,
            color = Gold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Location Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "My Location",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = locationText,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val fineGranted = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        if (fineGranted) {
                            isLoadingLocation = true
                            try {
                                val client =
                                    LocationServices.getFusedLocationProviderClient(context)
                                client.lastLocation
                                    .addOnSuccessListener { location ->
                                        isLoadingLocation = false
                                        locationText = if (location != null) {
                                            "Lat: ${"%.4f".format(location.latitude)}" +
                                                    ", Lng: ${"%.4f".format(location.longitude)}"
                                        } else {
                                            "Location not available"
                                        }
                                    }
                                    .addOnFailureListener {
                                        isLoadingLocation = false
                                        locationText = "Failed to get location"
                                    }
                            } catch (e: SecurityException) {
                                isLoadingLocation = false
                                locationText = "Permission error"
                            }
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Gold),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoadingLocation) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Get My Location",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Account Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "Account Information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )

                Spacer(modifier = Modifier.height(16.dp))

                ProfileInfoRow(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = userEmail
                )

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ProfileInfoRow(
                    icon = Icons.Default.Person,
                    label = "User ID",
                    value = userId.take(16) + "..."
                )

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ProfileInfoRow(
                    icon = Icons.Default.CheckCircle,
                    label = "Account Status",
                    value = "Active ✓"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // About Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "About",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )

                Spacer(modifier = Modifier.height(16.dp))

                ProfileInfoRow(
                    icon = Icons.Default.Star,
                    label = "App Version",
                    value = "1.0.0"
                )

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ProfileInfoRow(
                    icon = Icons.Default.LocationOn,
                    label = "Company",
                    value = "Rahath Travels Pvt Ltd"
                )

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ProfileInfoRow(
                    icon = Icons.Default.Phone,
                    label = "Support",
                    value = "+94 77 123 4567"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.ExitToApp,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Logout",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Gold,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}