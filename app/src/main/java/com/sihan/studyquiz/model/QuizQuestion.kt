package com.sihan.studyquiz.model

data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)