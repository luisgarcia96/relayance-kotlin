package com.kirabium.relayance.ui.state

import com.kirabium.relayance.domain.model.Customer

data class MainUiState(
    val customers: List<Customer> = emptyList(),
)
