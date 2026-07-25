package com.sihan.studyquiz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [QuizResultEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StudyQuizDatabase : RoomDatabase() {

    abstract fun quizResultDao(): QuizResultDao
}