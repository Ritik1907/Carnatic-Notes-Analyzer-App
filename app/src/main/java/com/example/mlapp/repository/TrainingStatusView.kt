package com.example.mlapp.repository

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mlapp.model.BreathingPhase
import com.example.mlapp.viewmodel.TrainingSessionViewModel

@Composable
fun TrainingStatusView(
    trainingSessionViewModel: TrainingSessionViewModel,
    modifier: Modifier = Modifier
) {
    val chestExpansion = trainingSessionViewModel.chestExpansion.collectAsState()
    val breathingPhase = trainingSessionViewModel.currentBreathingPhase.collectAsState()

    val expansion = chestExpansion.value
    val phase = breathingPhase.value

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "Chest Expansion:",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${expansion.format(1)} mm",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Breathing Phase: ${phase.name}",
            style = MaterialTheme.typography.titleMedium
        )
        LinearProgressIndicator(
            progress = breathingPhaseProgress(phase),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(top = 8.dp),
            color = breathingPhaseColor(phase)
        )
    }
}

// Extension to format float to n decimal places
private fun Float.format(digits: Int) = "%.${digits}f".format(this)

// Example progress mapping for breathing phases
private fun breathingPhaseProgress(phase: BreathingPhase): Float = when (phase) {
    BreathingPhase.INHALATION -> 1f
    BreathingPhase.EXHALATION -> 0.5f
    BreathingPhase.HOLD -> 0.6f
    else -> 0f
}

// Example color mapping (customize colors)
@Composable
private fun breathingPhaseColor(phase: BreathingPhase): androidx.compose.ui.graphics.Color = when (phase) {
    BreathingPhase.INHALATION -> MaterialTheme.colorScheme.primary
    BreathingPhase.EXHALATION -> MaterialTheme.colorScheme.secondary
    BreathingPhase.HOLD -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.onSurfaceVariant
}
