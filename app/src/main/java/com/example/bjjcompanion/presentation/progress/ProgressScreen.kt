package com.example.bjjcompanion.presentation.progress

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
import com.example.bjjcompanion.domain.usecase.weight.TimePeriod
import com.example.bjjcompanion.presentation.components.WeightChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weight Progress") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddWeightDialog() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Weight")
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading && !uiState.hasData) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (!uiState.hasData) {
            EmptyState(
                onAddWeight = { viewModel.showAddWeightDialog() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Progress Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProgressCard(
                        title = "Current",
                        value = uiState.currentWeight?.let { String.format("%.1f kg", it) } ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                    ProgressCard(
                        title = "Target",
                        value = uiState.targetWeight?.let { String.format("%.1f kg", it) } ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                    ProgressCard(
                        title = "Remaining",
                        value = String.format("%.1f kg", uiState.remainingKg),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Indicator
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { uiState.progressPercentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${String.format("%.1f", uiState.progressPercentage)}% complete",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (uiState.isGoalAchieved) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "🎉 Goal achieved!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Period Selector
                Text(
                    text = "Time Period",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                ScrollableTabRow(
                    selectedTabIndex = TimePeriod.entries.indexOf(uiState.selectedPeriod),
                    edgePadding = 0.dp
                ) {
                    TimePeriod.entries.forEach { period ->
                        Tab(
                            selected = uiState.selectedPeriod == period,
                            onClick = { viewModel.onPeriodSelected(period) },
                            text = { Text(period.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Weight Chart
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Weight History",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        WeightChart(
                            weightHistory = uiState.weightHistory
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Weight Entries List
                Text(
                    text = "Recent Entries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                uiState.weightHistory.sortedByDescending { it.date }.take(10).forEach { entry ->
                    WeightEntryItem(entry)
                }
            }
        }

        // Add Weight Dialog
        if (uiState.showAddWeightDialog) {
            AddWeightDialog(
                onDismiss = { viewModel.hideAddWeightDialog() },
                onConfirm = { weight -> viewModel.addWeightEntry(weight) },
                errorMessage = uiState.errorMessage
            )
        }
    }
}

@Composable
private fun ProgressCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun WeightEntryItem(entry: com.example.bjjcompanion.domain.model.WeightEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                    .format(java.util.Date(entry.date)),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = String.format("%.1f kg", entry.weight),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyState(
    onAddWeight: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No weight entries yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start tracking your weight progress by adding your first entry",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddWeight) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Weight")
        }
    }
}

private val TimePeriod.displayName: String
    get() = when (this) {
        TimePeriod.WEEK -> "Week"
        TimePeriod.MONTH -> "Month"
        TimePeriod.THREE_MONTHS -> "3 Months"
        TimePeriod.SIX_MONTHS -> "6 Months"
        TimePeriod.YEAR -> "Year"
        TimePeriod.ALL -> "All"
    }
