package com.example.supervisorar.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.supervisorar.domain.SupervisorarUseCase
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class SupervisingScreenViewModel(private val useCase: SupervisorarUseCase) : ViewModel() {

    val modelNode = MutableLiveData(
        ArModelNode(
            placementMode = PlacementMode.PLANE_VERTICAL,
            hitPosition = Position(0.0f, 0.0f, 0.0f),
            followHitPosition = true,
            instantAnchor = false,
        )
    )
}