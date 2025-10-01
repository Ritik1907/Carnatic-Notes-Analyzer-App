package com.example.mlapp.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mlapp.model.MLKitPoseAnalyzer
import com.example.mlapp.repository.TrainingStatusView
import com.example.mlapp.viewmodel.TrainingSessionViewModel

@Composable
fun CameraPreviewScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    trainingSessionViewModel: TrainingSessionViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val poseAnalyzer = remember { MLKitPoseAnalyzer(trainingSessionViewModel) }
    val cameraManager = remember { CameraManager(context, lifecycleOwner, poseAnalyzer) }

    LaunchedEffect(Unit) { cameraManager.startCamera() }
    DisposableEffect(Unit) {
        onDispose {
            cameraManager.stopCamera()
            poseAnalyzer.stop()
        }
    }

    val sessionActive by trainingSessionViewModel.sessionActive.collectAsState()
    val measurementActive by trainingSessionViewModel.measurementActive.collectAsState()
    val chestExpansion by trainingSessionViewModel.chestExpansion.collectAsState()
    val breathingPhase by trainingSessionViewModel.currentBreathingPhase.collectAsState()
    val landmarks by trainingSessionViewModel.currentPoseLandmarks.collectAsState()
    val isCalibrated by trainingSessionViewModel.isCalibrated.collectAsState()
    val showCalibrationBox by trainingSessionViewModel.showCalibrationBox.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        CameraPreviewView(
            previewView = cameraManager.previewView,
            modifier = Modifier.fillMaxSize(),
            navController = navController
        )

        PoseOverlay(
            landmarks = landmarks,
            showCalibrationBox = showCalibrationBox,
            isCalibrated = isCalibrated,
            modifier = Modifier.fillMaxSize()
        )

        if (showCalibrationBox && sessionActive && measurementActive) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(containerColor = Color(0xDD000000))
            ) {
                Text(
                    text = "Place a credit/debit card on your chest\nAlign it with the cyan box",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        IconButton(
            onClick = { cameraManager.switchCamera() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Switch Camera",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (sessionActive) {
                        trainingSessionViewModel.stopSession()
                    } else {
                        trainingSessionViewModel.startSession()
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = if (sessionActive) "Stop Session" else "Start Session")
            }

            Button(
                enabled = sessionActive,
                onClick = {
                    if (measurementActive) {
                        trainingSessionViewModel.stopMeasurement()
                    } else {
                        trainingSessionViewModel.startMeasurement()
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = if (measurementActive) "Stop Measurement" else "Start Measurement")
            }

            if (showCalibrationBox && sessionActive && measurementActive) {
                Button(
                    onClick = { trainingSessionViewModel.hideCalibrationBox() },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text("Card Positioned")
                }
            }

            TrainingStatusView(
                trainingSessionViewModel = trainingSessionViewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
