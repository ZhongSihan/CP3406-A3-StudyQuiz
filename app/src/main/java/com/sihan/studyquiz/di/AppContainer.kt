package com.sihan.studyquiz.di

import android.content.Context
import androidx.room.Room
import com.sihan.studyquiz.data.local.StudyQuizDatabase
import com.sihan.studyquiz.data.repository.QuizRepository

class AppContainer(context: Context) {

    val database: StudyQuizDatabase = Room.databaseBuilder(
        context.applicationContext,
        StudyQuizDatabase::class.java,
        "studyquiz_database"
    ).build()

    val quizResultDao = database.quizResultDao()

    val quizRepository = QuizRepository()
}