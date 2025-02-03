package com.example.supervisorar.data.mapper

import com.example.supervisorar.data.dto.MachineInfoDto
import com.example.supervisorar.domain.mapper.Mapper
import com.example.supervisorar.domain.model.MachineInfo

class MachineInfoMapper : Mapper<MachineInfoDto, MachineInfo> {
    override fun map(t: MachineInfoDto): MachineInfo {
        return t.run {
            MachineInfo(
                albumId = albumId,
                id = id,
                title = title,
                url = url,
                thumbnailUrl = thumbnailUrl,
                value = value,
                type = type
            )
        }
    }
}