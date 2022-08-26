package com.example.supervisorar.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supervisorar.domain.SupervisorarUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SupervisingScreenViewModel(private val useCase: SupervisorarUseCase) : ViewModel() {
    val info = MutableLiveData(useCase.getInfo())

    val contador = MutableLiveData<Unit>()

    init {
        runAgain()
    }

    fun runAgain() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(1500)
                contador.postValue(Unit)
            }
        }
    }
}