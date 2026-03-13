package com.kirabium.relayance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirabium.relayance.domain.usecase.ObserveCustomersUseCase
import com.kirabium.relayance.ui.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeCustomersUseCase: ObserveCustomersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeCustomersUseCase().collect { customers ->
                _uiState.value = MainUiState(customers = customers)
            }
        }
    }
}
