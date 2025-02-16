package com.example.supervisorar.infrastructure.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.supervisorar.infrastructure.di.SupervisorarDI
import com.example.supervisorar.presentation.compose.HomeScreen
import org.koin.android.ext.koin.androidContext
import org.koin.android.java.KoinAndroidApplication
import org.koin.core.context.GlobalContext.startKoin

class MainActivity2 : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Esconde barra de status e navegação
        window.insetsController?.apply {

            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        startKoin {
            KoinAndroidApplication.create(applicationContext)
            androidContext(this@MainActivity2)
            modules(SupervisorarDI.module)
        }

        setContent {
            Box(Modifier.fillMaxSize()) {
                HomeScreen()
            }
        }
    }
}