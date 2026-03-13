package com.kirabium.relayance.data.store

import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object InMemoryCustomerStore {

    private val customersState = MutableStateFlow(buildInitialCustomers())

    fun observeCustomers(): StateFlow<List<Customer>> = customersState.asStateFlow()

    fun observeCustomer(id: Int): Flow<Customer?> {
        return customersState
            .map { customers -> customers.firstOrNull { it.id == id } }
            .distinctUntilChanged()
    }

    fun customersSnapshot(): List<Customer> = customersState.value.toList()

    fun findCustomerById(id: Int): Customer? = customersState.value.firstOrNull { it.id == id }

    fun saveCustomer(id: Int?, name: String, email: String): SaveCustomerResult {
        return if (id == null) {
            SaveCustomerResult.Success(addCustomer(name = name, email = email))
        } else {
            val updated = updateCustomer(id = id, name = name, email = email)
            if (updated == null) SaveCustomerResult.NotFound else SaveCustomerResult.Success(updated)
        }
    }

    fun addCustomer(name: String, email: String): Customer {
        var createdCustomer: Customer? = null
        customersState.update { currentCustomers ->
            val nextId = (currentCustomers.maxOfOrNull { it.id } ?: 0) + 1
            createdCustomer = Customer(id = nextId, name = name, email = email, createdAt = Date())
            currentCustomers + createdCustomer!!
        }
        return checkNotNull(createdCustomer)
    }

    fun updateCustomer(id: Int, name: String, email: String): Customer? {
        var updatedCustomer: Customer? = null
        customersState.update { currentCustomers ->
            val index = currentCustomers.indexOfFirst { it.id == id }
            if (index == -1) {
                currentCustomers
            } else {
                val existingCustomer = currentCustomers[index]
                updatedCustomer = existingCustomer.copy(name = name, email = email)
                currentCustomers.toMutableList().apply {
                    this[index] = checkNotNull(updatedCustomer)
                }
            }
        }
        return updatedCustomer
    }

    fun resetCustomers() {
        customersState.value = buildInitialCustomers()
    }

    fun generateDate(monthsBack: Int): Date {
        return Calendar.getInstance().apply {
            add(Calendar.MONTH, -monthsBack)
        }.time
    }

    private fun buildInitialCustomers(): List<Customer> {
        return listOf(
            Customer(1, "Alice Wonderland", "alice@example.com", generateDate(12)),
            Customer(2, "Bob Builder", "bob@example.com", generateDate(6)),
            Customer(3, "Charlie Chocolate", "charlie@example.com", generateDate(3)),
            Customer(4, "Diana Dream", "diana@example.com", generateDate(1)),
            Customer(5, "Evan Escape", "evan@example.com", generateDate(0)),
        )
    }
}
