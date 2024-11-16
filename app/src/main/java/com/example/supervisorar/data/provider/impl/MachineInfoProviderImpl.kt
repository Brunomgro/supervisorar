package com.example.supervisorar.data.provider.impl

import com.example.supervisorar.data.dto.MachineInfoDto
import com.example.supervisorar.data.provider.MachineInfoProvider
import com.example.supervisorar.data.service.MachineInfoService
import com.example.supervisorar.domain.mapper.Mapper
import com.example.supervisorar.domain.model.MachineInfo
import com.example.supervisorar.infrastructure.configs.Configs

class MachineInfoProviderImpl(
    private val service: MachineInfoService,
    private val mapper: Mapper<MachineInfoDto, MachineInfo>
) : MachineInfoProvider {
    override suspend fun getInfo(): MachineInfo {
        val endpoint = Configs.endpointToGetInformations

        val machineInfoDto = service.getMachineInformation(endpoint).first()

        return mapper.map(machineInfoDto)
    }
}