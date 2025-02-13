package com.example.supervisorar.domain.usecase.impl

import android.util.Log
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import com.example.supervisorar.domain.model.MachineInfo
import com.example.supervisorar.domain.model.Medidores3d
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.Quaternion
import dev.romainguy.kotlin.math.RotationsOrder
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.toRotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SupervisorarUseCaseImpl: SupervisorarUseCase {
    private val database = Firebase.database
    private val myRef = database.getReference("leitura")
    private val currentData = MutableStateFlow<List<MachineInfo>>(emptyList())

    init {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                kotlin.runCatching {
                    dataSnapshot.getValue<List<MachineInfo>>()?.let {
                        currentData.value = it
                    }
                }.onFailure { error ->
                    Log.d("FIREBASE",error.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FIREBASE", error.message)
            }
        })
    }

    override fun getInfo(): Flow<List<MachineInfo>> = currentData


    override fun generateNode(): (modelLoader: ModelLoader) -> List<Triple<ModelNode, Node?, Medidores3d>> = { modelLoader ->
        Medidores3d.entries.map {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(assetFileLocation = it.path),
                scaleToUnits = 0.08f,
                centerOrigin = null,
                autoAnimate = false
            ).let { loadedNode ->
                loadedNode.isEditable = true
                loadedNode.isShadowCaster = false
                loadedNode.isShadowReceiver = false
                loadedNode.rotation = Quaternion
                    .fromAxisAngle(Float3(1f, 0f, 0f), -90f)
                    .toRotation(RotationsOrder.XYZ)
                return@let Triple(loadedNode, loadedNode.findChildRecursive("Body1.003"), it)
            }
        }.onEach {
            Log.d("BRUNO", "criou ${it.third.anchor.id}")
        }
    }

    override fun calculateAngle(info: Medidores3d, objectiveValue: Float): Float {
        val valor = if (objectiveValue > info.valueMax) {
            info.valueMax
        } else if (objectiveValue < info.valueMin) {
            info.valueMin
        } else objectiveValue

        return ((valor - info.valueMin) / (info.valueMax - info.valueMin)) * (info.angleMax - info.angleMin) + info.angleMin
    }

    private fun Node.findChildRecursive(name: String): Node? {
        if (this.name == name) return this

        for (child in this.childNodes) {
            val result = child.findChildRecursive(name)
            if (result != null) return result
        }

        return null
    }
}