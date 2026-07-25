package com.sihan.studyquiz.data.remote

data class QuizApiResponse(
    val response_code: Int,
    val results: List<QuizQuestionDto>
)

data class QuizQuestionDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)