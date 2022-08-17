package com.example.supervisorar.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.supervisorar.R
import com.example.supervisorar.infrastructure.SupervisorarDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.java.KoinAndroidApplication
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startKoin {
            KoinAndroidApplication.create(applicationContext)
            androidContext(this@MainActivity)
            modules(SupervisorarDI.module)
        }
    }
}