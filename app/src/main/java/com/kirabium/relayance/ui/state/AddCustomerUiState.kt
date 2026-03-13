package com.kirabium.relayance.ui.state

import com.kirabium.relayance.R

data class AddCustomerUiState(
    val editedCustomerId: Int? = null,
    val name: String = "",
    val email: String = "",
    val titleResId: Int = R.string.add_customer,
)
