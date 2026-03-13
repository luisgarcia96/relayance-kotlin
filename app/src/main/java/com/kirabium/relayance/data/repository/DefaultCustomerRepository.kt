package com.kirabium.relayance.data.repository

import com.kirabium.relayance.data.store.InMemoryCustomerStore
import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.domain.repository.CustomerRepository
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultCustomerRepository @Inject constructor(
    private val customerStore: InMemoryCustomerStore,
) : CustomerRepository {

    override fun observeCustomers(): Flow<List<Customer>> = customerStore.observeCustomers()

    override fun observeCustomer(id: Int): Flow<Customer?> = customerStore.observeCustomer(id)

    override suspend fun getCustomer(id: Int): Customer? = customerStore.findCustomerById(id)

    override suspend fun saveCustomer(id: Int?, name: String, email: String): SaveCustomerResult {
        return customerStore.saveCustomer(id = id, name = name, email = email)
    }

    override suspend fun reset() {
        customerStore.resetCustomers()
    }
}
