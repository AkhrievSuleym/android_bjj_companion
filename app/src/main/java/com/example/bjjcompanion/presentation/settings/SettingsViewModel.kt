package com.example.bjjcompanion.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.data.local.datastore.SettingsDataStore
import com.example.bjjcompanion.domain.usecase.user.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the settings screen.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
        loadThemePreference()
    }

    /**
     * Toggle dark/light theme.
     */
    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDarkTheme(isDark)
            _uiState.update { it.copy(isDarkTheme = isDark) }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getUserProfileUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load profile"
                        )
                    }
                }
                .collect { profile ->
                    _uiState.update {
                        it.copy(
                            userProfile = profile,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun loadThemePreference() {
        viewModelScope.launch {
            settingsDataStore.isDarkTheme()
                .catch { e ->
                    // Default to false if error
                    _uiState.update { it.copy(isDarkTheme = false) }
                }
                .collect { isDark ->
                    _uiState.update { it.copy(isDarkTheme = isDark) }
                }
        }
    }
}
