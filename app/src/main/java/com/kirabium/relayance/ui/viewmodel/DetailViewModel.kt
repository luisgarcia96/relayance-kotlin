package com.kirabium.relayance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirabium.relayance.domain.repository.CustomerRepository
import com.kirabium.relayance.ui.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var observedCustomerId: Int? = null
    private var observationJob: Job? = null

    fun loadCustomer(customerId: Int) {
        if (observedCustomerId == customerId) return
        observedCustomerId = customerId

        if (customerId <= 0) {
            _uiState.value = DetailUiState(shouldClose = true)
            return
        }

        observationJob?.cancel()
        observationJob = viewModelScope.launch {
            customerRepository.observeCustomer(customerId).collect { customer ->
                _uiState.value = if (customer == null) {
                    DetailUiState(shouldClose = true)
                } else {
                    DetailUiState(customer = customer)
                }
            }
        }
    }

    fun onCloseHandled() {
        _uiState.update { currentState ->
            if (!currentState.shouldClose) currentState else currentState.copy(shouldClose = false)
        }
    }
}
