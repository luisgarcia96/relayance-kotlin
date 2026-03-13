package com.kirabium.relayance.ui.event

sealed interface AddCustomerUiEvent {
    data object CustomerSaved : AddCustomerUiEvent
    data class ShowMessage(val messageResId: Int) : AddCustomerUiEvent
}
