package com.example.bjjcompanion.presentation.settings

import com.example.bjjcompanion.domain.model.UserProfile

/**
 * UI state for the settings screen.
 */
data class SettingsUiState(
    val userProfile: UserProfile? = null,
    val isDarkTheme: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
