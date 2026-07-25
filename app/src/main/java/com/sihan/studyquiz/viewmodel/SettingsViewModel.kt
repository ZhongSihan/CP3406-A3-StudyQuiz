package com.sihan.studyquiz.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val difficulty: String = "Easy",
    val questionCount: Int = 5,
    val soundEnabled: Boolean = true,
    val largeTextEnabled: Boolean = false
)

class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setDifficulty(value: String) {
        _uiState.value = _uiState.value.copy(
            difficulty = value
        )
    }

    fun setQuestionCount(value: Int) {
        _uiState.value = _uiState.value.copy(
            questionCount = value
        )
    }

    fun setSoundEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            soundEnabled = enabled
        )
    }

    fun setLargeTextEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            largeTextEnabled = enabled
        )
    }
}