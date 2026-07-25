package com.sihan.studyquiz.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sihan.studyquiz.StudyQuizApplication
import com.sihan.studyquiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    difficulty: String,
    questionCount: Int,
    soundEnabled: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val application =
        context.applicationContext as StudyQuizApplication

    val quizViewModel: QuizViewModel = viewModel(
        key = "quiz_${difficulty}_$questionCount",
        factory = QuizViewModel.Factory(
            quizResultDao = application.container.quizResultDao,
            difficulty = difficulty,
            questionCount = questionCount
        )
    )

    val uiState by quizViewModel.uiState.collectAsState()

    val toneGenerator = remember {
        ToneGenerator(
            AudioManager.STREAM_NOTIFICATION,
            70
        )
    }

    LaunchedEffect(
        uiState.answerSubmitted,
        uiState.currentQuestionIndex
    ) {
        if (
            soundEnabled &&
            uiState.answerSubmitted &&
            uiState.questions.isNotEmpty()
        ) {
            val currentQuestion =
                uiState.questions[
                    uiState.currentQuestionIndex
                ]

            val isCorrect =
                uiState.selectedAnswerIndex ==
                        currentQuestion.correctAnswerIndex

            if (isCorrect) {
                toneGenerator.startTone(
                    ToneGenerator.TONE_PROP_ACK,
                    150
                )
            } else {
                toneGenerator.startTone(
                    ToneGenerator.TONE_PROP_NACK,
                    200
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        if (uiState.isLoading) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Loading questions..."
                )
            }

            return@Column
        }

        if (uiState.questions.isEmpty()) {

            Text(
                text = "Quiz",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Questions could not be loaded."
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = {
                    quizViewModel.loadQuestions()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Try Again")
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }

            return@Column
        }

        if (uiState.quizFinished) {

            val percentage =
                uiState.score * 100 /
                        uiState.questions.size

            Text(
                text = "Quiz Complete!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text =
                    "Score: ${uiState.score} / ${uiState.questions.size}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(
                onClick = {
                    quizViewModel.restartQuiz()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Restart Quiz")
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }

            return@Column
        }

        val currentQuestion =
            uiState.questions[
                uiState.currentQuestionIndex
            ]

        Text(
            text = "Quiz",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Difficulty: $difficulty",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "Question ${uiState.currentQuestionIndex + 1} of ${uiState.questions.size}",
            style = MaterialTheme.typography.titleMedium
        )

        if (uiState.errorMessage != null) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = uiState.errorMessage ?: "",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = currentQuestion.question,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        currentQuestion.answers.forEachIndexed { index, answer ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected =
                        uiState.selectedAnswerIndex == index,
                    onClick = {
                        quizViewModel.selectAnswer(index)
                    },
                    enabled =
                        !uiState.answerSubmitted
                )

                Text(
                    text = answer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (!uiState.answerSubmitted) {

            Button(
                onClick = {
                    quizViewModel.submitAnswer()
                },
                enabled =
                    uiState.selectedAnswerIndex != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Answer")
            }

        } else {

            val selectedAnswer =
                uiState.selectedAnswerIndex

            val isCorrect =
                selectedAnswer ==
                        currentQuestion.correctAnswerIndex

            Text(
                text =
                    if (isCorrect) {
                        "Correct!"
                    } else {
                        "Incorrect."
                    },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (!isCorrect) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Correct answer: ${
                            currentQuestion.answers[
                                currentQuestion.correctAnswerIndex
                            ]
                        }"
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = {
                    quizViewModel.nextQuestion()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (
                        uiState.currentQuestionIndex <
                        uiState.questions.lastIndex
                    ) {
                        "Next Question"
                    } else {
                        "Finish Quiz"
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}