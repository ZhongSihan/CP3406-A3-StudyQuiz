package com.sihan.studyquiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sihan.studyquiz.data.local.QuizResultDao
import com.sihan.studyquiz.data.local.QuizResultEntity
import com.sihan.studyquiz.data.repository.QuizRepository
import com.sihan.studyquiz.model.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val score: Int = 0,
    val answerSubmitted: Boolean = false,
    val quizFinished: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class QuizViewModel(
    private val quizResultDao: QuizResultDao,
    private val difficulty: String,
    private val questionCount: Int
) : ViewModel() {

    private val repository = QuizRepository()

    private val fallbackQuestions = listOf(
        QuizQuestion(
            question = "What does API stand for?",
            answers = listOf(
                "Application Programming Interface",
                "Android Program Internet",
                "Application Process Input",
                "Android Programming Interface"
            ),
            correctAnswerIndex = 0
        ),
        QuizQuestion(
            question = "Which language is mainly used for modern Android development?",
            answers = listOf(
                "Python",
                "Kotlin",
                "PHP",
                "Ruby"
            ),
            correctAnswerIndex = 1
        ),
        QuizQuestion(
            question = "What is Jetpack Compose used for?",
            answers = listOf(
                "Database management",
                "Building Android user interfaces",
                "Creating web servers",
                "Managing GitHub repositories"
            ),
            correctAnswerIndex = 1
        ),
        QuizQuestion(
            question = "What does Room help an Android app do?",
            answers = listOf(
                "Store local data",
                "Take photos",
                "Send SMS messages",
                "Change screen brightness"
            ),
            correctAnswerIndex = 0
        ),
        QuizQuestion(
            question = "What is a ViewModel mainly used for?",
            answers = listOf(
                "Managing UI-related data",
                "Drawing app icons",
                "Installing Android Studio",
                "Creating Git branches"
            ),
            correctAnswerIndex = 0
        )
    )

    private val _uiState = MutableStateFlow(
        QuizUiState()
    )

    val uiState: StateFlow<QuizUiState> =
        _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {

            _uiState.value = QuizUiState(
                isLoading = true
            )

            try {
                val questions = repository.getQuestions(
                    amount = questionCount,
                    difficulty = difficulty
                )

                _uiState.value = QuizUiState(
                    questions =
                        if (questions.isNotEmpty()) {
                            questions
                        } else {
                            fallbackQuestions.take(
                                questionCount.coerceAtMost(
                                    fallbackQuestions.size
                                )
                            )
                        }
                )

            } catch (exception: Exception) {

                _uiState.value = QuizUiState(
                    questions = fallbackQuestions.take(
                        questionCount.coerceAtMost(
                            fallbackQuestions.size
                        )
                    ),
                    errorMessage =
                        "Online questions could not be loaded. Using offline questions."
                )
            }
        }
    }

    fun selectAnswer(index: Int) {
        if (!_uiState.value.answerSubmitted) {
            _uiState.value =
                _uiState.value.copy(
                    selectedAnswerIndex = index
                )
        }
    }

    fun submitAnswer() {
        val state = _uiState.value

        val selectedIndex =
            state.selectedAnswerIndex ?: return

        if (state.questions.isEmpty()) {
            return
        }

        val currentQuestion =
            state.questions[
                state.currentQuestionIndex
            ]

        val newScore =
            if (
                selectedIndex ==
                currentQuestion.correctAnswerIndex
            ) {
                state.score + 1
            } else {
                state.score
            }

        _uiState.value =
            state.copy(
                score = newScore,
                answerSubmitted = true
            )
    }

    fun nextQuestion() {
        val state = _uiState.value

        if (
            state.currentQuestionIndex <
            state.questions.lastIndex
        ) {

            _uiState.value =
                state.copy(
                    currentQuestionIndex =
                        state.currentQuestionIndex + 1,
                    selectedAnswerIndex = null,
                    answerSubmitted = false
                )

        } else {

            _uiState.value =
                state.copy(
                    quizFinished = true
                )

            saveResult()
        }
    }

    private fun saveResult() {
        val state = _uiState.value

        val percentage =
            if (state.questions.isNotEmpty()) {
                state.score * 100 /
                        state.questions.size
            } else {
                0
            }

        viewModelScope.launch {
            quizResultDao.insertResult(
                QuizResultEntity(
                    score = state.score,
                    totalQuestions =
                        state.questions.size,
                    percentage = percentage
                )
            )
        }
    }

    fun restartQuiz() {
        loadQuestions()
    }

    class Factory(
        private val quizResultDao: QuizResultDao,
        private val difficulty: String,
        private val questionCount: Int
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            return QuizViewModel(
                quizResultDao = quizResultDao,
                difficulty = difficulty,
                questionCount = questionCount
            ) as T
        }
    }
}