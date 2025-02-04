package com.example.supervisorar.domain.usecase.impl

import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import com.example.supervisorar.domain.model.MachineInfo

class SupervisorarUseCaseImpl(): SupervisorarUseCase {
    override suspend fun getInfo(): MachineInfo {
        return MachineInfo()
    }
}