package com.example.supervisorar.data.provider

import com.example.supervisorar.domain.model.MachineInfo

interface MachineInfoProvider {
    suspend fun getInfo(): MachineInfo
}