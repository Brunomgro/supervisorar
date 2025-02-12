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

class SupervisorarUseCaseImpl(): SupervisorarUseCase {
    private val database = Firebase.database
    private val myRef = database.getReference("leitura")

    private val currentData = MutableStateFlow<List<MachineInfo>>(emptyList())

    init {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                kotlin.runCatching {
                    dataSnapshot.getValue<List<MachineInfo>>()?.let {
                        currentData.value = it
                    } ?: Log.d("BRUNO", "no data found")
                }.onFailure { error ->
                    dataSnapshot.getValue<List<MachineInfo>>()?.let {
                        currentData.value = it
                    } ?: Log.d("BRUNO", error.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("BRUNO", error.message)
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

    fun Node.findChildRecursive(name: String): Node? {
        if (this.name == name) return this

        for (child in this.childNodes) {
            val result = child.findChildRecursive(name)
            if (result != null) return result
        }

        return null
    }
}