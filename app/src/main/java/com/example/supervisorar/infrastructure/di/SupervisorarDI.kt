package com.example.supervisorar.infrastructure.di

import com.example.supervisorar.data.dto.MachineInfoDto
import com.example.supervisorar.data.provider.MachineInfoProvider
import com.example.supervisorar.data.provider.impl.MachineInfoProviderImpl
import com.example.supervisorar.data.repository.MachineInfoRepository
import com.example.supervisorar.data.repository.impl.MachineInfoRepositoryImpl
import com.example.supervisorar.data.service.MachineInfoService
import com.example.supervisorar.infrastructure.retrofit.getRetrofitInstanceOf
import com.example.supervisorar.domain.usecase.SupervisorarUseCase
import com.example.supervisorar.domain.usecase.impl.SupervisorarUseCaseImpl
import com.example.supervisorar.domain.mapper.Mapper
import com.example.supervisorar.data.mapper.MachineInfoMapper
import com.example.supervisorar.domain.model.MachineInfo
import com.example.supervisorar.domain.usecase.ReadQrCodeUseCase
import com.example.supervisorar.domain.usecase.impl.ReadQrCodeUseCaseImpl
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
        factory<SupervisorarUseCase> { SupervisorarUseCaseImpl(get()) }
        factory<ReadQrCodeUseCase> {
            ReadQrCodeUseCaseImpl(get())
        }
    }

    private val viewModels = koinModule {
        viewModel { SupervisingScreenViewModel(get(), get()) }
    }

    private val mappers = koinModule {
        factory<Mapper<MachineInfoDto, MachineInfo>> { MachineInfoMapper() }
    }

    private val repositories = koinModule {
        factory<MachineInfoRepository> { MachineInfoRepositoryImpl(get()) }
    }

    private val providers = koinModule {
        factory<MachineInfoProvider> { MachineInfoProviderImpl(get(), get()) }
    }

    private val services = koinModule {
        single { getRetrofitInstanceOf<MachineInfoService>() }
    }

    val module =
        infrastructure + useCases + viewModels + mappers + repositories + providers + services
}