package com.kirabium.relayance.data

import com.kirabium.relayance.domain.model.Customer
import java.util.Calendar
import java.util.Date


object DummyData {
    fun generateDate(monthsBack: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -monthsBack)
        return calendar.time
    }

    private val initialCustomers = listOf(
        Customer(1, "Alice Wonderland", "alice@example.com", generateDate(12)),
        Customer(2, "Bob Builder", "bob@example.com", generateDate(6)),
        Customer(3, "Charlie Chocolate", "charlie@example.com", generateDate(3)),
        Customer(4, "Diana Dream", "diana@example.com", generateDate(1)),
        Customer(5, "Evan Escape", "evan@example.com", generateDate(0)),
    )

    private val mutableCustomers = initialCustomers.toMutableList()

    val customers: List<Customer>
        get() = mutableCustomers.toList()

    fun findCustomerById(id: Int): Customer? {
        return mutableCustomers.find { it.id == id }
    }

    fun addCustomer(name: String, email: String): Customer {
        val nextId = (mutableCustomers.maxOfOrNull { it.id } ?: 0) + 1
        val customer = Customer(nextId, name, email, Date())
        mutableCustomers.add(customer)
        return customer
    }

    fun updateCustomer(id: Int, name: String, email: String): Customer? {
        val index = mutableCustomers.indexOfFirst { it.id == id }
        if (index == -1) return null
        val existing = mutableCustomers[index]
        val updated = existing.copy(name = name, email = email)
        mutableCustomers[index] = updated
        return updated
    }

    fun resetCustomers() {
        mutableCustomers.clear()
        mutableCustomers.addAll(initialCustomers)
    }
}
