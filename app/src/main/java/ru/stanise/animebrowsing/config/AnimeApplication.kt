package ru.stanise.animebrowsing.config

import android.app.Application

class AnimeApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}