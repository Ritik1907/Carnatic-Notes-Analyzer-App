package com.example.mlapp.camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.mlapp.model.BreathingPhase
import com.example.mlapp.model.DisplacementData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Analyzer class to perform real-time pose detection using ML Kit Pose API,
 * calculate chest displacement and breathing phase, and pass results via callback.
 */
@SuppressLint("UnsafeOptInUsageError")
class PoseImageAnalyzer(
    private val onResult: (displacement: DisplacementData, phase: BreathingPhase) -> Unit
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

        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        poseDetector.process(image)
            .addOnSuccessListener { pose ->
                processPose(pose)
            }
            .addOnFailureListener { e ->
                Log.e("PoseImageAnalyzer", "Pose detection failed", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun processPose(pose: Pose) {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

        if (leftShoulder == null || rightShoulder == null) {
            // Missing required landmarks, ignore this frame
            return
        }

        val dx = rightShoulder.position3D.x - leftShoulder.position3D.x
        val dy = rightShoulder.position3D.y - leftShoulder.position3D.y
        val dz = rightShoulder.position3D.z - leftShoulder.position3D.z

        val displacement = DisplacementData(dx, dy, dz)
        val magnitude = displacement.magnitude()

        val breathingPhase = when {
            magnitude > 0.3f -> BreathingPhase.INHALATION
            magnitude < 0.2f -> BreathingPhase.EXHALATION
            else -> BreathingPhase.HOLD
        }

        analysisScope.launch {
            onResult(displacement, breathingPhase)
        }
    }

    /**
     * Call to release detector resources when no longer needed.
     */
    fun stop() {
        poseDetector.close()
    }
}
