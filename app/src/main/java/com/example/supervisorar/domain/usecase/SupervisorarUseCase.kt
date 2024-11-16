package com.example.supervisorar.domain.usecase

import com.example.supervisorar.domain.model.MachineInfo

interface SupervisorarUseCase {
    suspend fun getInfo(): MachineInfo
}