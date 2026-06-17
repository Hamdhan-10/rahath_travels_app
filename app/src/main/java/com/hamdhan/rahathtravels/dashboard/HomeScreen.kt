package com.hamdhan.rahathtravels.dashboard

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamdhan.rahathtravels.authentication.BlackBg
import com.hamdhan.rahathtravels.authentication.Gold

@Composable
fun HomeScreen(
    onNavigateToBooking: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val CardBg = Color(0xFF1A1A1A)

    Scaffold(
        containerColor = BlackBg,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF111111),
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Gold
                        )
                    },
                    label = {
                        Text("Home", color = Gold, fontSize = 11.sp)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFF2A2A2A)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToBooking() },
                    icon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Bookings",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    },
                    label = {
                        Text(
                            "Bookings",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFF2A2A2A)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToProfile() },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    },
                    label = {
                        Text(
                            "Profile",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFF2A2A2A)
                    )
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Assalamu Alaikum 👋",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Welcome to Rahath Travels",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Gold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Plan Your Sacred Journey",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Gold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Hajj & Umrah packages\ntailored just for you",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { onNavigateToBooking() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Gold
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Book Now",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Our Services",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ServiceCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Star,
                    title = "Hajj",
                    subtitle = "Full Package",
                    cardBg = CardBg,
                    onClick = { onNavigateToBooking() }
                )
                ServiceCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Favorite,
                    title = "Umrah",
                    subtitle = "All Seasons",
                    cardBg = CardBg,
                    onClick = { onNavigateToBooking() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ServiceCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocationOn,
                    title = "Hotels",
                    subtitle = "Near Haram",
                    cardBg = CardBg,
                    onClick = {}
                )
                ServiceCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Send,
                    title = "Transport",
                    subtitle = "Air & Ground",
                    cardBg = CardBg,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Why Choose Us",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            FeatureRow(
                icon = Icons.Default.CheckCircle,
                text = "Licensed & Trusted Agency"
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureRow(
                icon = Icons.Default.CheckCircle,
                text = "Affordable Hajj & Umrah Packages"
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureRow(
                icon = Icons.Default.CheckCircle,
                text = "24/7 Customer Support"
            )
            Spacer(modifier = Modifier.height(10.dp))
            FeatureRow(
                icon = Icons.Default.CheckCircle,
                text = "Experienced Tour Guides"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ServiceCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    cardBg: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Gold,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun FeatureRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Gold,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}