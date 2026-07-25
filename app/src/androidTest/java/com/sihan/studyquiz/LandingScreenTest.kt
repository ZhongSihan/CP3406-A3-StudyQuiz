package com.sihan.studyquiz

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import com.sihan.studyquiz.ui.screens.LandingScreen
import com.sihan.studyquiz.ui.theme.StudyQuizTheme

class LandingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun landingScreen_displaysMainContent() {

        composeTestRule.setContent {
            StudyQuizTheme {
                LandingScreen(
                    onStartQuiz = {},
                    onOpenStatistics = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("StudyQuiz")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Start Quiz")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("View Statistics")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Settings")
            .assertIsDisplayed()
    }

    @Test
    fun landingScreen_displaysPrivacyMessage() {

        composeTestRule.setContent {
            StudyQuizTheme {
                LandingScreen(
                    onStartQuiz = {},
                    onOpenStatistics = {},
                    onOpenSettings = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(
                "Your learning data stays on this device."
            )
            .assertIsDisplayed()
    }
}