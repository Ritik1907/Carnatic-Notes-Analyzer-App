package com.example.mlapp.model

/**
 * Represents 3-dimensional displacement data with precise measurement units.
 *
 * This class is immutable and thread-safe.
 *
 * @property x Displacement along the X-axis in centimeters.
 * @property y Displacement along the Y-axis in centimeters.
 * @property z Displacement along the Z-axis in centimeters.
 */
data class DisplacementData(
    val x: Float,
    val y: Float,
    val z: Float
) {
    /**
     * Calculates the Euclidean norm (magnitude) of the displacement vector.
     *
     * @return The magnitude of displacement as a Float.
     */
    fun magnitude(): Float = kotlin.math.sqrt(x * x + y * y + z * z)
}
