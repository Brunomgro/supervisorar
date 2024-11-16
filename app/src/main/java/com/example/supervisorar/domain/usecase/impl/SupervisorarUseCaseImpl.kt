package com.example.supervisorar.domain.usecase.impl

import com.example.supervisorar.data.repository.MachineInfoRepository
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import com.example.supervisorar.domain.model.MachineInfo

class SupervisorarUseCaseImpl(private val repository: MachineInfoRepository): SupervisorarUseCase {
    override suspend fun getInfo(): MachineInfo {
        return runCatching {
            repository.getInfo()
        }.getOrNull() ?: MachineInfo()
    }
}