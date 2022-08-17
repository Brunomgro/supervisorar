package com.example.supervisorar.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.supervisorar.domain.SupervisorarUseCase

class SupervisingScreenViewModel(private val useCase: SupervisorarUseCase) : ViewModel() {
    val info = MutableLiveData(useCase.getInfo())
}