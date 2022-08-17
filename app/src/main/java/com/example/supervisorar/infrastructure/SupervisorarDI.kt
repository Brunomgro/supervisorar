package com.example.supervisorar.infrastructure

import com.example.supervisorar.domain.SupervisorarUseCase
import com.example.supervisorar.domain.impl.SupervisorarUseCaseImpl
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module as koinModule

object SupervisorarDI {
    private val useCases = koinModule {
        factory<SupervisorarUseCase> {
            SupervisorarUseCaseImpl()
        }
    }

    private val viewModels = koinModule {
        viewModel {
            SupervisingScreenViewModel(get())
        }
    }

    val module = useCases + viewModels
}