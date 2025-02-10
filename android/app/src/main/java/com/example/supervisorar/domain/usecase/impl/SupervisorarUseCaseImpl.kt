package com.example.supervisorar.domain.usecase.impl

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import com.example.supervisorar.domain.model.MachineInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

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
}