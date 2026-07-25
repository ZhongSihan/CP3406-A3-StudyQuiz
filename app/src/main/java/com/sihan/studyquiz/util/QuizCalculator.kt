package com.sihan.studyquiz.util

object QuizCalculator {

    fun calculatePercentage(
        score: Int,
        totalQuestions: Int
    ): Int {
        if (totalQuestions <= 0) {
            return 0
        }

        return score * 100 / totalQuestions
    }

    fun calculateAverage(
        percentages: List<Int>
    ): Int {
        if (percentages.isEmpty()) {
            return 0
        }

        return percentages.average().toInt()
    }

    fun calculateHighest(
        percentages: List<Int>
    ): Int {
        return percentages.maxOrNull() ?: 0
    }
}