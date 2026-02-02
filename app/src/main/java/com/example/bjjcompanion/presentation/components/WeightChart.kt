package com.example.bjjcompanion.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.bjjcompanion.domain.model.WeightEntry
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeightChart(
    weightHistory: List<WeightEntry>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    val chartEntryModelProducer = remember(weightHistory) {
        val entries = weightHistory.mapIndexed { index, entry ->
            FloatEntry(x = index.toFloat(), y = entry.weight)
        }
        ChartEntryModelProducer(entries)
    }

    val dateFormatter = remember {
        SimpleDateFormat("MMM dd", Locale.getDefault())
    }

    if (weightHistory.isEmpty()) {
        return
    }

    Chart(
        chart = lineChart(),
        chartModelProducer = chartEntryModelProducer,
        startAxis = rememberStartAxis(
            title = "Weight (kg)"
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                val index = value.toInt()
                if (index in weightHistory.indices) {
                    dateFormatter.format(Date(weightHistory[index].date))
                } else {
                    ""
                }
            }
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
