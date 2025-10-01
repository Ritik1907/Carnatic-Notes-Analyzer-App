package com.example.mlapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mlapp.model.BreathingPhase
import com.example.mlapp.model.DisplacementData
import com.google.mlkit.vision.pose.PoseLandmark
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.sqrt

class TrainingSessionViewModel : ViewModel() {

    private val _currentBreathingPhase = MutableStateFlow(BreathingPhase.UNKNOWN)
    val currentBreathingPhase: StateFlow<BreathingPhase> = _currentBreathingPhase

    private val _sessionActive = MutableStateFlow(false)
    val sessionActive: StateFlow<Boolean> = _sessionActive

    private val _measurementActive = MutableStateFlow(false)
    val measurementActive: StateFlow<Boolean> = _measurementActive

    private val _currentPoseLandmarks = MutableStateFlow<List<PoseLandmark>>(emptyList())
    val currentPoseLandmarks: StateFlow<List<PoseLandmark>> = _currentPoseLandmarks

    private val _chestExpansion = MutableStateFlow(0f)
    val chestExpansion: StateFlow<Float> = _chestExpansion


    private val _isCalibrated = MutableStateFlow(false)
    val isCalibrated: StateFlow<Boolean> = _isCalibrated

    private val _showCalibrationBox = MutableStateFlow(true)
    val showCalibrationBox: StateFlow<Boolean> = _showCalibrationBox

    // Calibration and smoothing
    private val calibrationSamples = mutableListOf<Float>()
    private var baselineChestWidth: Float = 0f
    private var scaleCalibrated: Boolean = false
    private var pixelsPerMm: Float = 1f // Scale factor from pixels to millimeters

    private val smoothingWindow = 8
    private val chestWidthSamples = ArrayDeque<Float>(smoothingWindow)

    private val inhaleThreshold = 2f // mm
    private val exhaleThreshold = -2f // mm

    // Credit card dimensions in mm
    companion object {
        const val CARD_WIDTH_MM = 85.6f
        const val CARD_HEIGHT_MM = 53.98f
    }

    fun updatePoseLandmarks(landmarks: List<PoseLandmark>) {
        _currentPoseLandmarks.value = landmarks
        if (_measurementActive.value && _sessionActive.value) {
            processChestExpansion(landmarks)
        }
    }

    fun updateBreathingPhase(phase: BreathingPhase) {
        _currentBreathingPhase.value = phase
    }

    fun updateChestDisplacement(displacement: DisplacementData) {
        // In the optimized version, we handle everything in updatePoseLandmarks
        // This method can be kept empty or used for additional processing if needed
        if (_measurementActive.value && _sessionActive.value) {
            // Optional: You can add any additional displacement-specific logic here
            // The main chest expansion calculation is now handled in updatePoseLandmarks
        }
    }

    private fun processChestExpansion(landmarks: List<PoseLandmark>) {
        val leftShoulder = landmarks.find { it.landmarkType == PoseLandmark.LEFT_SHOULDER }
        val rightShoulder = landmarks.find { it.landmarkType == PoseLandmark.RIGHT_SHOULDER }

        if (leftShoulder != null && rightShoulder != null) {
            // Calculate shoulder distance in pixels
            val shoulderDistancePixels = calculateDistance(
                leftShoulder.position.x, leftShoulder.position.y,
                rightShoulder.position.x, rightShoulder.position.y
            )

            // Convert to millimeters using scale factor
            val shoulderDistanceMm = shoulderDistancePixels / pixelsPerMm

            if (!scaleCalibrated) {
                calibrationSamples.add(shoulderDistanceMm)
                if (calibrationSamples.size >= 60) { // ~6 seconds at 10fps
                    baselineChestWidth = calibrationSamples.average().toFloat()
                    scaleCalibrated = true
                    calibrationSamples.clear()
                    _isCalibrated.value = true
                    _showCalibrationBox.value = false
                }
                return
            }

            // Apply smoothing
            if (chestWidthSamples.size == smoothingWindow) chestWidthSamples.removeFirst()
            chestWidthSamples.addLast(shoulderDistanceMm)
            val smoothedWidth = chestWidthSamples.average().toFloat()

            val expansion = smoothedWidth - baselineChestWidth
            _chestExpansion.value = expansion

            // Update breathing phase
            val newPhase = when {
                expansion > inhaleThreshold -> BreathingPhase.INHALATION
                expansion < exhaleThreshold -> BreathingPhase.EXHALATION
                else -> BreathingPhase.HOLD
            }
            updateBreathingPhase(newPhase)
        }
    }

    fun calibrateScale(cardWidthInPixels: Float) {
        pixelsPerMm = cardWidthInPixels / CARD_WIDTH_MM
        resetCalibration()
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
    }

    fun startSession() {
        _sessionActive.value = true
    }

    fun stopSession() {
        _sessionActive.value = false
        _currentBreathingPhase.value = BreathingPhase.UNKNOWN
        stopMeasurement()
        resetCalibration()
        _chestExpansion.value = 0f
        _showCalibrationBox.value = true
    }

    fun startMeasurement() {
        if (_sessionActive.value) {
            _measurementActive.value = true
            resetCalibration()
            _chestExpansion.value = 0f
            _currentBreathingPhase.value = BreathingPhase.UNKNOWN
        }
    }

    fun stopMeasurement() {
        _measurementActive.value = false
    }

    private fun resetCalibration() {
        calibrationSamples.clear()
        scaleCalibrated = false
        baselineChestWidth = 0f
        chestWidthSamples.clear()
        _isCalibrated.value = false
    }

    fun hideCalibrationBox() {
        _showCalibrationBox.value = false
    }
}
