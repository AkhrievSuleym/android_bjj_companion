package com.example.bjjcompanion.presentation.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Dashboard : Screen("dashboard")
    data object Nutrition : Screen("nutrition")
    data object Progress : Screen("progress")
    data object Training : Screen("training")
    data object AddTraining : Screen("add_training")
    data object Techniques : Screen("techniques")
    data object Settings : Screen("settings")
}
