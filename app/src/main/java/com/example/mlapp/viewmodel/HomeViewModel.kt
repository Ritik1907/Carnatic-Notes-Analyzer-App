package com.example.mlapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel to manage UI state for the Home screen.
 */
class HomeViewModel : ViewModel() {

    // Example: StateFlow to hold a welcome message or other home screen data
    private val _welcomeMessage = MutableStateFlow("Welcome to App")
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    // Add additional UI state management logic as needed
}
