package com.example.mlapp

import MusicNodesScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlapp.camera.CameraPermissionRequestScreen
import com.example.mlapp.camera.CameraPreviewScreen
import com.example.mlapp.ui.HomeScreen
import com.example.mlapp.ui.SettingsScreen
import com.example.mlapp.ui.musicanalyzer.CarnaticNotesScreen
import com.example.mlapp.ui.theme.MlappTheme
import com.example.mlapp.viewmodel.PermissionViewModel
import com.example.mlapp.viewmodel.TrainingSessionViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MlappTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val permissionViewModel: PermissionViewModel = viewModel()
    val trainingSessionViewModel: TrainingSessionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("musicNodes") { MusicNodesScreen(navController) }
        composable("carnaticNotesScreen") { CarnaticNotesScreen(navController) }
        composable("permission") {
            CameraPermissionRequestScreen(
                navController = navController,
                permissionViewModel = permissionViewModel
            )
        }
        composable("camera") {
            CameraPreviewScreen(
                navController = navController,
                trainingSessionViewModel = trainingSessionViewModel
            )
        }
    }
}
