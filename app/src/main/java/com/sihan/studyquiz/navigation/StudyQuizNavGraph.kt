package com.sihan.studyquiz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sihan.studyquiz.ui.screens.LandingScreen
import com.sihan.studyquiz.ui.screens.QuizScreen
import com.sihan.studyquiz.ui.screens.SettingsScreen
import com.sihan.studyquiz.ui.screens.StatisticsScreen

object Routes {
    const val LANDING = "landing"
    const val QUIZ = "quiz"
    const val SETTINGS = "settings"
    const val STATISTICS = "statistics"
}

@Composable
fun StudyQuizNavGraph() {
    val navController = rememberNavController()

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
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
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