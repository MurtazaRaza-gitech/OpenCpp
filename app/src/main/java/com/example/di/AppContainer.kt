package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.ThemePreferences
import com.example.data.database.AppDatabase
import com.example.domain.repository.RecentFileRepository
import com.example.network.CompileService
import com.example.repository.RecentFileRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Main application-wide dependency container (Service Locator).
 * Assures modularity, clean testability, and 100% compile-time resolution.
 */
class AppContainer(private val context: Context) {

    // --- Preferences Module ---
    val themePreferences: ThemePreferences by lazy {
        ThemePreferences(context)
    }

    // --- Database Module ---
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "opencpp_database"
        ).fallbackToDestructiveMigration().build()
    }

    // --- Repository Module ---
    val recentFileRepository: RecentFileRepository by lazy {
        RecentFileRepositoryImpl(database.recentFileDao())
    }

    // --- Network Module ---
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.opencpp.sandbox/") // Sandbox base URL
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val compileService: CompileService by lazy {
        retrofit.create(CompileService::class.java)
    }
}
