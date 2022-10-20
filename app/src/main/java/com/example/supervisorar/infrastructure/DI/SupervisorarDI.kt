package com.example.supervisorar.infrastructure.DI

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
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module as koinModule

object SupervisorarDI {
    private val useCases = koinModule {
        factory<SupervisorarUseCase> { SupervisorarUseCaseImpl(get()) }
    }

    private val viewModels = koinModule {
        viewModel { SupervisingScreenViewModel(get()) }
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

    val module = useCases + viewModels + mappers + repositories + providers + services
}