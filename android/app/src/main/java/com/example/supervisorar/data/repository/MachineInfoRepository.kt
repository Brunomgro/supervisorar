package com.example.supervisorar.data.repository

import com.example.supervisorar.domain.model.MachineInfo

interface MachineInfoRepository {
    val getInfo: suspend () -> MachineInfo
}