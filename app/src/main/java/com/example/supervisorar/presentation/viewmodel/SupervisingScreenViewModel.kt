package com.example.supervisorar.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supervisorar.domain.usecase.ReadQrCodeUseCase
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import kotlinx.coroutines.launch

class SupervisingScreenViewModel(
    private val useCase: SupervisorarUseCase,
    private val qrCodeUseCase: ReadQrCodeUseCase
) : ViewModel() {
    var title = MutableLiveData("")
    val findQrCode = qrCodeUseCase::getData

    init {
        viewModelScope.launch {
            val info = useCase.getInfo()

            title.postValue(info.type + "  " + info.title + "  " + info.value)
        }
    }
}