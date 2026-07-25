package com.sihan.studyquiz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sihan.studyquiz.ui.screens.LandingScreen
import com.sihan.studyquiz.ui.screens.QuizScreen
import com.sihan.studyquiz.ui.screens.SettingsScreen
import com.sihan.studyquiz.ui.screens.StatisticsScreen
import com.sihan.studyquiz.viewmodel.SettingsViewModel

object Routes {
    const val LANDING = "landing"
    const val QUIZ = "quiz"
    const val SETTINGS = "settings"
    const val STATISTICS = "statistics"
}

@Composable
fun StudyQuizNavGraph() {

    val navController = rememberNavController()

    val settingsViewModel: SettingsViewModel = viewModel()

    val settingsUiState by
    settingsViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.LANDING
    ) {

        composable(Routes.LANDING) {
            LandingScreen(
                onStartQuiz = {
                    navController.navigate(Routes.QUIZ)
                },
                onOpenStatistics = {
                    navController.navigate(Routes.STATISTICS)
                },
                onOpenSettings = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(Routes.QUIZ) {
            QuizScreen(
                difficulty = settingsUiState.difficulty,
                questionCount = settingsUiState.questionCount,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.STATISTICS) {
            StatisticsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}