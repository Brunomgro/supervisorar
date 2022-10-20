package com.example.supervisorar.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import kotlinx.coroutines.launch

class SupervisingScreenViewModel(private val useCase: SupervisorarUseCase) : ViewModel() {

    val modelNode = MutableLiveData(
        ArModelNode(
            placementMode = PlacementMode.PLANE_VERTICAL,
            hitPosition = Position(0.0f, 0.0f, 0.0f),
            followHitPosition = true,
            instantAnchor = false,
        )
    )

    var title = MutableLiveData("")

    init {
        viewModelScope.launch {
            val info = useCase.getInfo()

            title.postValue(info.type + "  " + info.title + "  " + info.value)
        }
    }
}