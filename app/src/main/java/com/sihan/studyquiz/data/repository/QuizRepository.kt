package com.sihan.studyquiz.data.repository

import com.sihan.studyquiz.data.remote.QuizApiService
import com.sihan.studyquiz.model.QuizQuestion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.text.Html

class QuizRepository {

    private val api: QuizApiService = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuizApiService::class.java)

    suspend fun getQuestions(
        amount: Int,
        difficulty: String
    ): List<QuizQuestion> {

        val response = api.getQuestions(
            amount = amount,
            difficulty = difficulty.lowercase()
        )

        return response.results.map { dto ->

            val allAnswers =
                (dto.incorrect_answers + dto.correct_answer).shuffled()
            val decodedQuestion = Html.fromHtml(
                dto.question,
                Html.FROM_HTML_MODE_LEGACY
            ).toString()

            val decodedAnswers = allAnswers.map {
                Html.fromHtml(
                    it,
                    Html.FROM_HTML_MODE_LEGACY
                ).toString()
            }

            val decodedCorrectAnswer = Html.fromHtml(
                dto.correct_answer,
                Html.FROM_HTML_MODE_LEGACY
            ).toString()

            QuizQuestion(
                question = decodedQuestion,
                answers = decodedAnswers,
                correctAnswerIndex = decodedAnswers.indexOf(decodedCorrectAnswer)
            )
        }
    }
}