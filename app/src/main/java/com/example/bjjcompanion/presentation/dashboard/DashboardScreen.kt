package com.example.bjjcompanion.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bjjcompanion.domain.model.Training
import com.example.bjjcompanion.domain.model.WeightEntry
import com.example.bjjcompanion.presentation.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome back, ${uiState.greetingName}!") }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Weight Progress Card
                WeightProgressCard(
                    currentWeight = uiState.currentWeight,
                    targetWeight = uiState.targetWeight,
                    weightDifference = uiState.weightDifference,
                    progressPercentage = uiState.weightProgress,
                    recentEntries = uiState.recentWeightEntries,
                    hasData = uiState.hasWeightData,
                    onViewDetails = { navController.navigate(Screen.Progress.route) }
                )

                // Nutrition Card
                NutritionCard(
                    caloriesConsumed = uiState.dailyNutrition.totalCalories.toInt(),
                    caloriesTarget = uiState.caloriesTarget,
                    caloriesRemaining = uiState.caloriesRemaining,
                    protein = uiState.dailyNutrition.totalProtein,
                    fat = uiState.dailyNutrition.totalFat,
                    carbs = uiState.dailyNutrition.totalCarbs,
                    progressPercentage = uiState.caloriesProgressPercentage,
                    mealsLogged = uiState.foodLogsCount,
                    hasData = uiState.hasNutritionData,
                    onLogFood = { navController.navigate(Screen.Nutrition.route) }
                )

                // Next Training Card
                NextTrainingCard(
                    training = uiState.nextTraining,
                    hasData = uiState.hasTrainingData,
                    onViewTraining = { navController.navigate(Screen.Training.route) }
                )

                // Quick Actions
                QuickActionsSection(
                    onAddWeight = { navController.navigate(Screen.Progress.route) },
                    onLogFood = { navController.navigate(Screen.Nutrition.route) },
                    onLogTraining = { navController.navigate(Screen.Training.route) }
                )
            }
        }
    }
}

@Composable
private fun WeightProgressCard(
    currentWeight: Float?,
    targetWeight: Float?,
    weightDifference: Float,
    progressPercentage: Float,
    recentEntries: List<WeightEntry>,
    hasData: Boolean,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onViewDetails
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Weight Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (!hasData) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No weight entries yet. Start tracking your weight!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Current",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = currentWeight?.let { String.format("%.1f kg", it) } ?: "-",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Target",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = targetWeight?.let { String.format("%.1f kg", it) } ?: "-",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progressPercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when {
                        weightDifference > 0 -> "+${String.format("%.1f", kotlin.math.abs(weightDifference))} kg to go"
                        weightDifference < 0 -> "${String.format("%.1f", kotlin.math.abs(weightDifference))} kg to go"
                        else -> "Goal achieved!"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onViewDetails) {
                Text("View Details")
            }
        }
    }
}

@Composable
private fun NutritionCard(
    caloriesConsumed: Int,
    caloriesTarget: Int,
    caloriesRemaining: Int,
    protein: Float,
    fat: Float,
    carbs: Float,
    progressPercentage: Float,
    mealsLogged: Int,
    hasData: Boolean,
    onLogFood: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onLogFood
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Nutrition",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (hasData) {
                    Text(
                        text = "$mealsLogged meal${if (mealsLogged != 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (!hasData) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No food logged today. Start tracking your nutrition!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Consumed",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$caloriesConsumed kcal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Remaining",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$caloriesRemaining kcal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (caloriesRemaining > 0)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { (progressPercentage / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = if (progressPercentage > 100)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MacroItem("Protein", String.format("%.1fg", protein))
                    MacroItem("Fat", String.format("%.1fg", fat))
                    MacroItem("Carbs", String.format("%.1fg", carbs))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onLogFood) {
                Icon(Icons.Default.Favorite, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Food")
            }
        }
    }
}

@Composable
private fun MacroItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NextTrainingCard(
    training: Training?,
    hasData: Boolean,
    onViewTraining: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onViewTraining
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Next Training",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (!hasData || training == null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No upcoming training scheduled",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = training.type.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SimpleDateFormat("EEE, MMM dd 'at' HH:mm", Locale.getDefault())
                                .format(Date(training.date)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "${training.durationMinutes} min",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (!training.notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = training.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onViewTraining) {
                Icon(Icons.Default.Star, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Training")
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    onAddWeight: () -> Unit,
    onLogFood: () -> Unit,
    onLogTraining: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionButton(
                    text = "Add Weight",
                    icon = Icons.Default.Add,
                    onClick = onAddWeight,
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    text = "Log Food",
                    icon = Icons.Default.Favorite,
                    onClick = onLogFood,
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    text = "Log Training",
                    icon = Icons.Default.Star,
                    onClick = onLogTraining,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
