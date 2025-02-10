package com.example.supervisorar.domain.model

data class MachineInfo(
    val id: String = "",
    val type: String = "",
    val value: Float = 0f,
    val min: Float = 0f,
    val max: Float = 100f
)

enum class MachineInfoType {
    temperature,
    level,
    speed
}

