package com.sihan.studyquiz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sihan.studyquiz.StudyQuizApplication
import com.sihan.studyquiz.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val application =
        context.applicationContext as StudyQuizApplication

    val statisticsViewModel: StatisticsViewModel = viewModel(
        factory = StatisticsViewModel.Factory(
            application.container.quizResultDao
        )
    )

    val uiState by statisticsViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        StatisticCard(
            title = "Quiz Attempts",
            value = uiState.totalAttempts.toString()
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatisticCard(
            title = "Highest Score",
            value = "${uiState.highestScore}%"
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatisticCard(
            title = "Average Score",
            value = "${uiState.averageScore}%"
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatisticCard(
            title = "Latest Score",
            value = "${uiState.latestScore}%"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recent Results",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.recentResults.isEmpty()) {
            Text(
                text = "No quiz results yet.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            uiState.recentResults.forEachIndexed { index, result ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Attempt ${uiState.totalAttempts - index}"
                        )

                        Text(
                            text = "${result.percentage}%",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                statisticsViewModel.clearStatistics()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Statistics")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your quiz results are stored locally on this device.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun StatisticCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                fontWeight = FontWeight.Bold
            )
        }
    }
}