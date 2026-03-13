package com.kirabium.relayance.domain.repository

import com.kirabium.relayance.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun observeCustomers(): Flow<List<Customer>>
    fun observeCustomer(id: Int): Flow<Customer?>
    suspend fun getCustomer(id: Int): Customer?
    suspend fun saveCustomer(id: Int?, name: String, email: String): SaveCustomerResult
    suspend fun reset()
}

sealed interface SaveCustomerResult {
    data class Success(val customer: Customer) : SaveCustomerResult
    data object NotFound : SaveCustomerResult
    data object InvalidEmail : SaveCustomerResult
}
