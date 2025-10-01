package com.example.mlapp.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.mlapp.viewmodel.PermissionViewModel

@Composable
fun CameraPermissionRequestScreen(
    navController: NavController,
    permissionViewModel: PermissionViewModel
) {
    val context = LocalContext.current
    val hasPermission = permissionViewModel.hasCameraPermission.value  // Access MutableState's value directly

    val launcher = rememberLauncherForActivityResult(RequestPermission()) { granted ->
        permissionViewModel.setCameraPermissionGranted(granted)
        if (granted) {
            navController.navigate("camera") {
                popUpTo("permission") { inclusive = true }
            }
        }
    }

    // Check permission when entered
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        permissionViewModel.setCameraPermissionGranted(granted)
        if (granted) {
            navController.navigate("camera") {
                popUpTo("permission") { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (!hasPermission) {  // Correct boolean syntax here
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Camera permission is required to use this feature.")
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                        Text("Grant Camera Permission")
                    }
                }
            }
        }
    }
}
