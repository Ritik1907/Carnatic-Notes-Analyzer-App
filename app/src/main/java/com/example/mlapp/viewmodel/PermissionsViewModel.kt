package com.example.mlapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PermissionViewModel: ViewModel(){
    var hasCameraPermission = mutableStateOf(false)
        private set
    fun setCameraPermissionGranted(granted : Boolean){
        hasCameraPermission.value = granted

    }
}