package com.sihan.studyquiz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sihan.studyquiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel = viewModel()
) {
    val uiState by quizViewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(16.dp))

            Text("Loading questions...")
        }

        return
    }

    if (uiState.quizFinished) {
        QuizResultScreen(
            score = uiState.score,
            totalQuestions = uiState.questions.size,
            onRestart = quizViewModel::restartQuiz,
            onBack = onBack,
            modifier = modifier
        )

        return
    }

    if (uiState.questions.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Questions could not be loaded.",
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = quizViewModel::loadQuestions,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Try Again")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }
        }

        return
    }

    val currentQuestion =
        uiState.questions[uiState.currentQuestionIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Question ${uiState.currentQuestionIndex + 1} of ${uiState.questions.size}"
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = uiState.errorMessage ?: "",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = currentQuestion.question,
                modifier = Modifier.padding(20.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        currentQuestion.answers.forEachIndexed { index, answer ->
            OutlinedButton(
                onClick = {
                    quizViewModel.selectAnswer(index)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                val selectedMark =
                    if (uiState.selectedAnswerIndex == index) {
                        "✓ "
                    } else {
                        ""
                    }

                Text(
                    text = selectedMark + answer
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        if (!uiState.answerSubmitted) {
            Button(
                onClick = quizViewModel::submitAnswer,
                enabled = uiState.selectedAnswerIndex != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Answer")
            }
        } else {
            val isCorrect =
                uiState.selectedAnswerIndex ==
                        currentQuestion.correctAnswerIndex

            Text(
                text = if (isCorrect) {
                    "Correct ✓"
                } else {
                    "Incorrect"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (!isCorrect) {
                Text(
                    text = "Correct answer: " +
                            currentQuestion.answers[
                                currentQuestion.correctAnswerIndex
                            ]
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = quizViewModel::nextQuestion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (
                        uiState.currentQuestionIndex ==
                        uiState.questions.lastIndex
                    ) {
                        "Finish Quiz"
                    } else {
                        "Next Question"
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

@Composable
private fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percentage =
        if (totalQuestions > 0) {
            score * 100 / totalQuestions
        } else {
            0
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Complete!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Score: $score / $totalQuestions",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restart Quiz")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}