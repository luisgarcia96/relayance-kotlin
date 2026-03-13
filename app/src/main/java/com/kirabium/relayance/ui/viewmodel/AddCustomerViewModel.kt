package com.kirabium.relayance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirabium.relayance.R
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import com.kirabium.relayance.domain.usecase.GetCustomerUseCase
import com.kirabium.relayance.domain.usecase.SaveCustomerUseCase
import com.kirabium.relayance.ui.event.AddCustomerUiEvent
import com.kirabium.relayance.ui.state.AddCustomerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AddCustomerViewModel @Inject constructor(
    private val getCustomerUseCase: GetCustomerUseCase,
    private val saveCustomerUseCase: SaveCustomerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCustomerUiState())
    val uiState: StateFlow<AddCustomerUiState> = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<AddCustomerUiEvent>()
    val uiEvents: SharedFlow<AddCustomerUiEvent> = _uiEvents.asSharedFlow()

    private var isInitialized = false

    fun initialize(customerId: Int?) {
        if (isInitialized) return
        isInitialized = true

        if (customerId == null || customerId <= 0) {
            _uiState.value = AddCustomerUiState(titleResId = R.string.add_customer)
            return
        }

        viewModelScope.launch {
            val customer = getCustomerUseCase(customerId)
            _uiState.value = if (customer == null) {
                AddCustomerUiState(titleResId = R.string.add_customer)
            } else {
                AddCustomerUiState(
                    editedCustomerId = customer.id,
                    name = customer.name,
                    email = customer.email,
                    titleResId = R.string.edit_customer,
                )
            }
        }
    }

    fun saveCustomer(
        nameInput: String,
        emailInput: String,
        noNameFallback: String,
        noEmailFallback: String,
    ) {
        val currentState = _uiState.value
        val name = nameInput.trim().ifBlank { noNameFallback }
        val email = emailInput.trim().ifBlank { noEmailFallback }

        viewModelScope.launch {
            when (saveCustomerUseCase(currentState.editedCustomerId, name, email)) {
                is SaveCustomerResult.Success -> _uiEvents.emit(AddCustomerUiEvent.CustomerSaved)
                SaveCustomerResult.NotFound,
                SaveCustomerResult.InvalidEmail -> {
                    _uiEvents.emit(AddCustomerUiEvent.ShowMessage(R.string.customer_save_error))
                }
            }
        }
    }
}
