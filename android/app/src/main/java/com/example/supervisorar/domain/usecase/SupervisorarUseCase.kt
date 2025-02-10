package com.example.supervisorar.domain.usecase

import com.example.supervisorar.domain.model.MachineInfo
import kotlinx.coroutines.flow.Flow

interface SupervisorarUseCase {
    fun getInfo(): Flow<List<MachineInfo>>
}