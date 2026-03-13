package com.kirabium.relayance.data

import com.kirabium.relayance.data.store.InMemoryCustomerStore
import com.kirabium.relayance.domain.model.Customer
import java.util.Date

@Deprecated("Use CustomerRepository through ViewModels and use cases.")
object DummyData {
    fun generateDate(monthsBack: Int): Date {
        return InMemoryCustomerStore.generateDate(monthsBack)
    }

    val customers: List<Customer>
        get() = InMemoryCustomerStore.customersSnapshot()

    fun findCustomerById(id: Int): Customer? {
        return InMemoryCustomerStore.findCustomerById(id)
    }

    fun addCustomer(name: String, email: String): Customer {
        return InMemoryCustomerStore.addCustomer(name = name, email = email)
    }

    fun updateCustomer(id: Int, name: String, email: String): Customer? {
        return InMemoryCustomerStore.updateCustomer(id = id, name = name, email = email)
    }

    fun resetCustomers() {
        InMemoryCustomerStore.resetCustomers()
    }
}
