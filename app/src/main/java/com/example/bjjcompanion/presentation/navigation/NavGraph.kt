package com.example.bjjcompanion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import com.example.bjjcompanion.presentation.dashboard.DashboardScreen
import com.example.bjjcompanion.presentation.nutrition.NutritionScreen
import com.example.bjjcompanion.presentation.onboarding.OnboardingScreen
import com.example.bjjcompanion.presentation.progress.ProgressScreen
import com.example.bjjcompanion.presentation.training.TrainingCalendarScreen
import com.example.bjjcompanion.presentation.techniques.TechniquesScreen
import com.example.bjjcompanion.presentation.settings.SettingsScreen
import com.example.bjjcompanion.presentation.training.AddTrainingScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Dashboard.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Dashboard.route) {
                        // Clear onboarding from back stack
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }

        composable(Screen.Nutrition.route) {
            NutritionScreen()
        }

        composable(Screen.Progress.route) {
            ProgressScreen()
        }

        composable(Screen.Training.route) {
            TrainingCalendarScreen(navController = navController)
        }

        composable(Screen.AddTraining.route) {
            AddTrainingScreen(navController = navController)
        }

        composable(Screen.Techniques.route) {
            TechniquesScreen(navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}
