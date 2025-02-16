package com.example.supervisorar.presentation.presentationmodel

import com.example.supervisorar.domain.model.Medidores3d
import io.github.sceneview.node.Node

data class MachinePresentationData(
    val medidor: Node? = null, // só existe depois da inicialização correta
    val ponteiro: Node? = null, // só existe depois da inicialização correta
    val valor: Float, // dado de medição que vem do servidor
    val info: Medidores3d
) {
    val id: String = info.anchor.id
}
