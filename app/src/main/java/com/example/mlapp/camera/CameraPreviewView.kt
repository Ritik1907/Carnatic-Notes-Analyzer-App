package com.example.mlapp.camera

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun CameraPreviewView(
    modifier: Modifier = Modifier,
    previewView: PreviewView,
    navController: NavController
) {
    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}
