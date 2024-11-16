package com.example.supervisorar.data.service

import com.example.supervisorar.data.dto.MachineInfoDto
import retrofit2.http.GET
import retrofit2.http.Url

interface MachineInfoService {

    @GET
    suspend fun getMachineInformation(
        @Url endpoint: String
    ): List<MachineInfoDto>
}