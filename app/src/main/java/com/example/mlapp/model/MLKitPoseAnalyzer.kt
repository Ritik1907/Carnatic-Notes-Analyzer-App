package com.example.mlapp.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.mlapp.viewmodel.TrainingSessionViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
class MLKitPoseAnalyzer(
    private val trainingSessionViewModel: TrainingSessionViewModel
) : ImageAnalysis.Analyzer {

    private val detectorOptions = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()

    private val poseDetector: PoseDetector by lazy {
        PoseDetection.getClient(detectorOptions)
    }

    private val analysisScope = CoroutineScope(Dispatchers.Default)

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            poseDetector.process(image)
                .addOnSuccessListener { pose: Pose ->
                    val landmarks = pose.allPoseLandmarks
                    trainingSessionViewModel.updatePoseLandmarks(landmarks)
                    processPose(pose)
                }
                .addOnFailureListener { e ->
                    Log.e("MLKitPoseAnalyzer", "Pose detection failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    fun stop() {
        poseDetector.close()
    }

    private fun processPose(pose: Pose) {
        // Extract landmarks and compute displacement or breathing phase here
        // For demonstration, assume chest expansion: distance between left and right shoulder

        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

        if (leftShoulder != null && rightShoulder != null) {
            val dx = rightShoulder.position3D.x - leftShoulder.position3D.x
            val dy = rightShoulder.position3D.y - leftShoulder.position3D.y
            val dz = rightShoulder.position3D.z - leftShoulder.position3D.z

            val displacement = DisplacementData(dx, dy, dz)

            // Analyze displacement magnitude or changes to infer breathing phase (simplified)
            val magnitude = displacement.magnitude()

            val phase = when {
                magnitude > 0.3f -> BreathingPhase.INHALATION
                magnitude < 0.2f -> BreathingPhase.EXHALATION
                else -> BreathingPhase.HOLD
            }

            analysisScope.launch {
                trainingSessionViewModel.updateChestDisplacement(displacement)
                trainingSessionViewModel.updateBreathingPhase(phase)
            }
        }
    }
}
