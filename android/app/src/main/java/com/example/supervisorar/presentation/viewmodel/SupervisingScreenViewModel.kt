package com.example.supervisorar.presentation.viewmodel

import android.media.Image
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supervisorar.domain.model.QrCodeInfo
import com.example.supervisorar.domain.usecase.ReadQrCodeUseCase
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SupervisingScreenViewModel(
    private val useCase: SupervisorarUseCase,
    private val qrCodeUseCase: ReadQrCodeUseCase,
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : ViewModel(), CoroutineScope {
    var isTracking = true
    var job: Job? = null

    fun findQrCode(
        image: () -> Image,
        onFinish: (QrCodeInfo?) -> Unit
    ) {
        if (job?.isCompleted == false && isTracking) return
        job = launch(coroutineContext) {
            val imageFrame = runCatching { image() }.fold(
                onSuccess = { it },
                onFailure = { Log.d("BRUNO", it.message.orEmpty()); null }
            ) ?: return@launch

            runCatching { qrCodeUseCase.getData(imageFrame, onFinish) }
        }
    }

    init {
        viewModelScope.launch {
            val info = useCase.getInfo()
        }
    }
}