package com.example.bjjcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bjjcompanion.presentation.MainViewModel
import com.example.bjjcompanion.presentation.components.BottomNavBar
import com.example.bjjcompanion.presentation.navigation.NavGraph
import com.example.bjjcompanion.presentation.theme.GrapplingCompanionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val hasProfile by viewModel.hasProfile.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val navController = rememberNavController()

    GrapplingCompanionTheme(darkTheme = isDarkTheme) {
        // Show loading while checking profile
        if (hasProfile == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Determine start destination based on profile existence
            val startDestination = if (hasProfile == true) {
                com.example.bjjcompanion.presentation.navigation.Screen.Dashboard.route
            } else {
                com.example.bjjcompanion.presentation.navigation.Screen.Onboarding.route
            }

            // Track current route to hide bottom bar on onboarding
            val currentRoute by navController.currentBackStackEntryAsState()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    // Hide bottom bar on onboarding screen
                    if (currentRoute?.destination?.route != com.example.bjjcompanion.presentation.navigation.Screen.Onboarding.route) {
                        BottomNavBar(navController)
                    }
                }
            ) { innerPadding ->
                NavGraph(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    GrapplingCompanionTheme {
        MainScreen()
    }
}
