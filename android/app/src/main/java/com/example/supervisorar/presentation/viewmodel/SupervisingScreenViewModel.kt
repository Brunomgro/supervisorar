package com.example.supervisorar.presentation.viewmodel

import android.media.Image
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supervisorar.domain.model.Medidores3d
import com.example.supervisorar.domain.model.QrCodeInfo
import com.example.supervisorar.domain.usecase.ReadQrCodeUseCase
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import com.example.supervisorar.presentation.presentationmodel.MachinePresentationData
import io.github.sceneview.loaders.ModelLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SupervisingScreenViewModel(
    private val useCase: SupervisorarUseCase,
    private val qrCodeUseCase: ReadQrCodeUseCase,
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : ViewModel(), CoroutineScope {
    var isTracking = true
    var job: Job? = null

    val temperatureData = useCase.getInfo().map {
        it.firstOrNull { info -> info.id == "maquina1" }
    }

    val levelData = useCase.getInfo().map {
        it.firstOrNull { info -> info.id == "maquina2" }
    }

    val medidasDoServidor = useCase.getInfo()

    fun generateNodes(modelLoader: ModelLoader) = useCase.generateNode()(modelLoader).map {
        MachinePresentationData(
            it.first,
            it.second,
            0f,
            it.third
        )
    }

    val calculateAngle = useCase::calculateAngle
}