package com.example.supervisorar.infrastructure.di

import com.example.supervisorar.domain.usecase.ReadQrCodeUseCase
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import com.example.supervisorar.domain.usecase.impl.ReadQrCodeUseCaseImpl
import com.example.supervisorar.domain.usecase.impl.SupervisorarUseCaseImpl
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module as koinModule

object SupervisorarDI {
    private val infrastructure = koinModule {
        single(createdAtStart = true) {
            BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
            )
        }
    }

    private val useCases = koinModule {
        factory<SupervisorarUseCase> { SupervisorarUseCaseImpl() }
        factory<ReadQrCodeUseCase> { ReadQrCodeUseCaseImpl(get()) }
    }

    private val viewModels = koinModule {
        viewModel { SupervisingScreenViewModel(get(), get()) }
    }

    private val repositories = koinModule {
    }

    private val providers = koinModule {
    }

    private val services = koinModule {
    }

    val module =
        infrastructure + useCases + viewModels + repositories + providers + services
}