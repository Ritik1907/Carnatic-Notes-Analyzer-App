package com.example.mlapp.camera

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseOverlay(
    landmarks: List<PoseLandmark>,
    showCalibrationBox: Boolean = false,
    isCalibrated: Boolean = false,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    val connections = listOf(
        PoseLandmark.LEFT_SHOULDER to PoseLandmark.RIGHT_SHOULDER,
        PoseLandmark.LEFT_SHOULDER to PoseLandmark.LEFT_HIP,
        PoseLandmark.RIGHT_SHOULDER to PoseLandmark.RIGHT_HIP,
        PoseLandmark.LEFT_HIP to PoseLandmark.RIGHT_HIP
    )

    val landmarkMap = landmarks.associateBy { it.landmarkType }

    Canvas(modifier = modifier.fillMaxSize()) {
        if (showCalibrationBox) {
            drawCalibrationBox(this, density)
        }

        connections.forEach { (startType, endType) ->
            val start = landmarkMap[startType]
            val end = landmarkMap[endType]
            if (start != null && end != null) {
                drawLine(
                    color = if (isCalibrated) Color.Green else Color.Yellow,
                    strokeWidth = 3f,
                    cap = StrokeCap.Round,
                    start = Offset(start.position.x, start.position.y),
                    end = Offset(end.position.x, end.position.y)
                )
            }
        }

        listOf(
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.LEFT_HIP,
            PoseLandmark.RIGHT_HIP
        ).forEach { landmarkType ->
            landmarkMap[landmarkType]?.let { landmark ->
                drawCircle(
                    color = Color.White,
                    radius = 12f,
                    center = Offset(landmark.position.x, landmark.position.y)
                )
                drawCircle(
                    color = if (isCalibrated) Color.Green else Color.Red,
                    radius = 8f,
                    center = Offset(landmark.position.x, landmark.position.y)
                )
            }
        }
    }
}

private fun drawCalibrationBox(drawScope: DrawScope, density: androidx.compose.ui.unit.Density) {
    with(drawScope) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val cardWidth = with(density) { 150.dp.toPx() }
        val cardHeight = cardWidth * (53.98f / 85.6f)

        val left = centerX - cardWidth / 2
        val top = centerY - cardHeight / 2

        drawRect(
            color = Color.Cyan,
            topLeft = Offset(left, top),
            size = Size(cardWidth, cardHeight),
            style = Stroke(width = 4f)
        )

        val cornerSize = 20f
        listOf(
            Offset(left, top),
            Offset(left + cardWidth - cornerSize, top),
            Offset(left, top + cardHeight - cornerSize),
            Offset(left + cardWidth - cornerSize, top + cardHeight - cornerSize)
        ).forEach { corner ->
            drawRect(
                color = Color.Cyan,
                topLeft = corner,
                size = Size(cornerSize, cornerSize),
                style = Stroke(width = 2f)
            )
        }
    }
}
