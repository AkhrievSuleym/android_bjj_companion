package com.example.bjjcompanion.presentation.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    viewModel: NutritionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nutrition") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showSearchDialog() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Food")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Today's Summary",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Calories: ${uiState.dailyNutrition.totalCalories.toInt()} / ${uiState.caloriesTarget}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    LinearProgressIndicator(
                        progress = { (uiState.caloriesPercentage / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }
            }

            Text("Food logs and dialogs coming soon...")
        }
    }

    // Dialogs
    if (uiState.showSearchDialog) {
        FoodSearchDialog(
            searchQuery = uiState.searchQuery,
            searchResults = uiState.searchResults,
            isSearching = uiState.isSearching,
            searchError = uiState.searchError,
            onQueryChanged = { viewModel.onSearchQueryChanged(it) },
            onFoodSelected = { food ->
                viewModel.onFoodSelected(food, uiState.selectedMealType)
            },
            onDismiss = { viewModel.hideSearchDialog() }
        )
    }

    uiState.selectedFood?.let { selectedFood ->
        if (uiState.showAddFoodDialog) {
            AddFoodDialog(
                food = selectedFood,
                onDismiss = { viewModel.hideAddFoodDialog() },
                onConfirm = { grams -> viewModel.addFoodLog(grams) },
                errorMessage = uiState.errorMessage
            )
        }
    }
}
