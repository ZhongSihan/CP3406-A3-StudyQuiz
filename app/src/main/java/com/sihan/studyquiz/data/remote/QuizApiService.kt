package com.sihan.studyquiz.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApiService {

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 5,
        @Query("type") type: String = "multiple"
    ): QuizApiResponse
}