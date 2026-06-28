package com.example

import android.app.Application
import com.example.di.AppContainer

/**
 * Main Application class for OpenCpp. Holds the dependency injection container.
 */
class OpenCppApplication : Application() {

    // Global reference to the dependency container
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
