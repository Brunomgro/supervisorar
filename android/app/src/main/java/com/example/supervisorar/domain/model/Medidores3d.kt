package com.example.supervisorar.domain.model

enum class Medidores3d(
    val path: String,
    val angleMin: Float,
    val angleMax: Float,
    val valueMin: Float,
    val valueMax: Float,
    val anchor: ImagensAncora
) {
    Energymeter(
        path = "3dmodels/energymeter.glb",
        valueMin = 0f,
        valueMax = 5000f,
        angleMin = 0 + 330f,
        angleMax = 270 + 330f,
        anchor = ImagensAncora.Maquina5QrCode
    ),
    Fuel(
        "3dmodels/temperatura.glb",
        valueMin = 40f,
        valueMax = 120f,
        angleMin = 0 + 25f,
        angleMax = 190f,
        anchor = ImagensAncora.Maquina1
    ),
    Temperatura(
        "3dmodels/fuel.glb",
        valueMin = 0f,
        valueMax = 1f,
        angleMin = 0 + 25f,
        angleMax = 190f,
        anchor = ImagensAncora.Maquina2
    ),
    Voltimetro(
        "3dmodels/voltimetro.glb",
        valueMin = 8f,
        valueMax = 16f,
        angleMin = 0 + 25f,
        angleMax = 190f,
        anchor = ImagensAncora.Maquina3
    ),
    RPM(
        "3dmodels/medidorderpm.glb",
        valueMin = 0f,
        valueMax = 10000f,
        angleMin = 0 + 240f,
        angleMax = 180 - 30f,
        anchor = ImagensAncora.Maquina4
    ),
}

enum class ImagensAncora(val path: String, val id: String) {
    Maquina1("images/foto1.jpg", "maquina1"),
    Maquina2("images/foto2.jpg", "maquina2"),
    Maquina3("images/foto3.jpg", "maquina3"),
    Maquina4("images/foto4.jpg", "maquina4"),
    Maquina5QrCode("images/maquina1.png", "maquina5"),
}