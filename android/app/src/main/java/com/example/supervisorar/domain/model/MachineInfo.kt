package com.example.supervisorar.domain.model

import com.example.supervisorar.data.dto.MachineInfoDto

data class MachineInfo(
    val albumId: Int? = null,
    val id: Int? = null,
    val title: String? = null,
    val url: String? = null,
    val thumbnailUrl: String? = null,
    val value: Double? = null,
    val type: String? = null
)

fun MachineInfoDto.toDomain() = MachineInfo(
    albumId = albumId,
    id = id,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
    value = value,
    type = type
)