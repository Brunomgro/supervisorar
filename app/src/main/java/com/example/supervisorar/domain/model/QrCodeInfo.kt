package com.example.supervisorar.domain.model

import android.graphics.Rect

data class QrCodeInfo(
    val bounds: Rect,
    val content: String
)
