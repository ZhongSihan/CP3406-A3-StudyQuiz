package com.sihan.studyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sihan.studyquiz.navigation.StudyQuizNavGraph
import com.sihan.studyquiz.ui.theme.StudyQuizTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            StudyQuizTheme {
                StudyQuizNavGraph()
            }
        }
    }
}