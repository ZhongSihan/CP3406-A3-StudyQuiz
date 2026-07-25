package com.sihan.studyquiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sihan.studyquiz.data.local.QuizResultDao
import com.sihan.studyquiz.data.local.QuizResultEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StatisticsUiState(
    val totalAttempts: Int = 0,
    val highestScore: Int = 0,
    val averageScore: Int = 0,
    val latestScore: Int = 0,
    val recentResults: List<QuizResultEntity> = emptyList()
)

class StatisticsViewModel(
    private val quizResultDao: QuizResultDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        observeResults()
    }

    private fun observeResults() {
        viewModelScope.launch {
            quizResultDao.getAllResults().collect { results ->

                val totalAttempts = results.size

                val highestScore =
                    results.maxOfOrNull { it.percentage } ?: 0

                val averageScore =
                    if (results.isNotEmpty()) {
                        results.map { it.percentage }.average().toInt()
                    } else {
                        0
                    }

                val latestScore =
                    results.firstOrNull()?.percentage ?: 0

                _uiState.value = StatisticsUiState(
                    totalAttempts = totalAttempts,
                    highestScore = highestScore,
                    averageScore = averageScore,
                    latestScore = latestScore,
                    recentResults = results.take(5)
                )
            }
        }
    }

    fun clearStatistics() {
        viewModelScope.launch {
            quizResultDao.clearAllResults()
        }
    }

    class Factory(
        private val quizResultDao: QuizResultDao
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            return StatisticsViewModel(quizResultDao) as T
        }
    }
}