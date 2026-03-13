package com.kirabium.relayance.data.store

import com.kirabium.relayance.domain.repository.SaveCustomerResult
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InMemoryCustomerStoreTest {

    @Before
    fun setUp() {
        InMemoryCustomerStore.resetCustomers()
    }

    @After
    fun tearDown() {
        InMemoryCustomerStore.resetCustomers()
    }

    @Test
    fun addCustomer_appendsCustomerWithNextAvailableId() {
        val initialCustomers = InMemoryCustomerStore.customersSnapshot()

        val createdCustomer = InMemoryCustomerStore.addCustomer("Frank Future", "frank@example.com")

        val updatedCustomers = InMemoryCustomerStore.customersSnapshot()
        assertEquals(initialCustomers.size + 1, updatedCustomers.size)
        assertEquals(initialCustomers.maxOf { it.id } + 1, createdCustomer.id)
        assertEquals(createdCustomer, updatedCustomers.last())
        assertTrue(createdCustomer.createdAt is Date)
    }

    @Test
    fun updateCustomer_returnsUpdatedCustomerWhenCustomerExists() {
        val originalCustomer = checkNotNull(InMemoryCustomerStore.findCustomerById(1))

        val updatedCustomer = InMemoryCustomerStore.updateCustomer(
            id = 1,
            name = "Alice Updated",
            email = "alice.updated@example.com",
        )

        assertNotNull(updatedCustomer)
        assertEquals(originalCustomer.id, updatedCustomer?.id)
        assertEquals("Alice Updated", updatedCustomer?.name)
        assertEquals("alice.updated@example.com", updatedCustomer?.email)
        assertEquals(originalCustomer.createdAt, updatedCustomer?.createdAt)
    }

    @Test
    fun saveCustomer_returnsNotFoundWhenCustomerIdDoesNotExist() {
        val result = InMemoryCustomerStore.saveCustomer(
            id = 999,
            name = "Ghost Customer",
            email = "ghost@example.com",
        )

        assertEquals(SaveCustomerResult.NotFound, result)
    }

    @Test
    fun observeCustomer_emitsExistingAndUpdatedCustomer() = runTest {
        val emissions = mutableListOf<String>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            InMemoryCustomerStore.observeCustomer(1)
                .take(2)
                .toList()
                .map { customer -> customer?.email.orEmpty() }
                .also(emissions::addAll)
        }

        advanceUntilIdle()
        InMemoryCustomerStore.updateCustomer(
            id = 1,
            name = "Alice Wonderland",
            email = "alice+updated@example.com",
        )
        advanceUntilIdle()

        assertEquals(listOf("alice@example.com", "alice+updated@example.com"), emissions)
        job.cancel()
    }

    @Test
    fun resetCustomers_restoresInitialCustomerList() {
        InMemoryCustomerStore.addCustomer("Transient", "transient@example.com")

        InMemoryCustomerStore.resetCustomers()

        val resetCustomers = InMemoryCustomerStore.customersSnapshot()
        assertEquals(5, resetCustomers.size)
        assertEquals("Alice Wonderland", resetCustomers.first().name)
    }
}
