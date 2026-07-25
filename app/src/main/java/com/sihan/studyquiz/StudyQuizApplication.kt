package com.sihan.studyquiz

import android.app.Application
import com.sihan.studyquiz.di.AppContainer

class StudyQuizApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)
    }
}