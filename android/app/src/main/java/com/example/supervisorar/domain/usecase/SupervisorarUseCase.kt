package com.example.supervisorar.domain.usecase

import com.example.supervisorar.domain.model.MachineInfo
import com.example.supervisorar.domain.model.Medidores3d
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import kotlinx.coroutines.flow.Flow

interface SupervisorarUseCase {
    fun getInfo(): Flow<List<MachineInfo>>
    fun generateNode(): (ModelLoader) -> List<Triple<ModelNode, Node?, Medidores3d>>
}