package com.hamdhan.rahathtravels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hamdhan.rahathtravels.navigation.AppNavigation
import com.hamdhan.rahathtravels.ui.theme.RahathTravelsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RahathTravelsTheme {
                AppNavigation()
            }
        }
    }
}