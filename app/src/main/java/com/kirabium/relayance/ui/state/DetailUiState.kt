package com.kirabium.relayance.ui.state

import com.kirabium.relayance.domain.model.Customer

data class DetailUiState(
    val customer: Customer? = null,
    val shouldClose: Boolean = false,
)
