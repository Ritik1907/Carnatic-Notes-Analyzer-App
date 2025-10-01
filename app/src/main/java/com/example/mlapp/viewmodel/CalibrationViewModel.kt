package com.example.mlapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel to manage calibration data and logic.
 */
class CalibrationViewModel : ViewModel() {

    // Holds calibration offset in centimeters (example)
    private val _calibrationOffset = MutableStateFlow(0f)
    val calibrationOffset: StateFlow<Float> = _calibrationOffset

    /**
     * Updates the calibration offset.
     *
     * @param offset New calibration offset value in centimeters.
     */
    fun updateCalibrationOffset(offset: Float) {
        _calibrationOffset.value = offset
    }

    // Additional calibration methods can be added here
}
