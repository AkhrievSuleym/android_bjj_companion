package com.example.bjjcompanion.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.data.local.datastore.SettingsDataStore
import com.example.bjjcompanion.domain.usecase.user.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getUserProfileUseCase: GetUserProfileUseCase,
    settingsDataStore: SettingsDataStore
) : ViewModel() {

    val hasProfile: StateFlow<Boolean?> = getUserProfileUseCase()
        .map { profile -> profile != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null // null = loading
        )

    val isDarkTheme: StateFlow<Boolean> = settingsDataStore.isDarkTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
}
