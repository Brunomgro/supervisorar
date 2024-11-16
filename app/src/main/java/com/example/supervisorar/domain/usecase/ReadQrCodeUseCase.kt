package com.example.supervisorar.domain.usecase

import android.media.Image
import com.example.supervisorar.domain.model.QrCodeInfo

interface ReadQrCodeUseCase {
    fun getData(image: Image, onFinish: (QrCodeInfo?) -> Unit)
}