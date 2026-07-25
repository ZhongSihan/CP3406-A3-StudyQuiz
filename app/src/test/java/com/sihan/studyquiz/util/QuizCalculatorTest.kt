package com.sihan.studyquiz.util

import org.junit.Assert.assertEquals
import org.junit.Test

class QuizCalculatorTest {

    @Test
    fun calculatePercentage_returnsCorrectPercentage() {
        val result = QuizCalculator.calculatePercentage(
            score = 4,
            totalQuestions = 5
        )

        assertEquals(80, result)
    }

    @Test
    fun calculatePercentage_returnsZeroWhenTotalQuestionsIsZero() {
        val result = QuizCalculator.calculatePercentage(
            score = 0,
            totalQuestions = 0
        )

        assertEquals(0, result)
    }

    @Test
    fun calculateAverage_returnsCorrectAverage() {
        val result = QuizCalculator.calculateAverage(
            listOf(40, 60, 80)
        )

        assertEquals(60, result)
    }

    @Test
    fun calculateAverage_returnsZeroForEmptyList() {
        val result = QuizCalculator.calculateAverage(
            emptyList()
        )

        assertEquals(0, result)
    }

    @Test
    fun calculateHighest_returnsHighestScore() {
        val result = QuizCalculator.calculateHighest(
            listOf(40, 90, 60)
        )

        assertEquals(90, result)
    }

    @Test
    fun calculateHighest_returnsZeroForEmptyList() {
        val result = QuizCalculator.calculateHighest(
            emptyList()
        )

        assertEquals(0, result)
    }
}