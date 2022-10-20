package com.example.supervisorar.data.repository.impl

import com.example.supervisorar.data.provider.MachineInfoProvider
import com.example.supervisorar.data.repository.MachineInfoRepository

class MachineInfoRepositoryImpl(private val provider: MachineInfoProvider) : MachineInfoRepository {
    override val getInfo = provider::getInfo
}