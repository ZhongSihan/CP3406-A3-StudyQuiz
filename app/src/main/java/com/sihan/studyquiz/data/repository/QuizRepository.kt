package com.sihan.studyquiz.data.repository

import com.sihan.studyquiz.data.remote.QuizApiService
import com.sihan.studyquiz.model.QuizQuestion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizRepository {

    private val api: QuizApiService = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuizApiService::class.java)

    suspend fun getQuestions(
        amount: Int = 5
    ): List<QuizQuestion> {

        val response = api.getQuestions(amount)

        return response.results.map { dto ->

            val allAnswers =
                (dto.incorrect_answers + dto.correct_answer).shuffled()

            QuizQuestion(
                question = dto.question,
                answers = allAnswers,
                correctAnswerIndex = allAnswers.indexOf(dto.correct_answer)
            )
        }
    }
}