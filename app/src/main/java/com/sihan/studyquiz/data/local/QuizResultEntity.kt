package com.sihan.studyquiz.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val completedAt: Long = System.currentTimeMillis()
)