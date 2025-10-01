package com.example.mlapp.model

/**
 * Enumeration of breathing phases relevant to Deep Inspiration Breath Hold training.
 *
 * The phases enable classification of respiratory states for the training workflow.
 */
enum class BreathingPhase {
    /** Patient is inhaling deeply. */
    INHALATION,

    /** Patient is exhaling. */
    EXHALATION,

    /** Patient is holding breath (breath hold phase). */
    HOLD,

    /** Unknown or indeterminate breathing phase. */
    UNKNOWN;

    companion object {
        /**
         * Safely parses a string to a [BreathingPhase], case-insensitive.
         * Returns [UNKNOWN] if no match is found.
         */
        fun fromString(value: String?): BreathingPhase = values().find {
            it.name.equals(value, ignoreCase = true)
        } ?: UNKNOWN
    }
}
