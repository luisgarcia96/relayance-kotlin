package com.kirabium.relayance.data.repository

import com.kirabium.relayance.data.store.InMemoryCustomerStore
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DefaultCustomerRepositoryTest {

    private lateinit var repository: DefaultCustomerRepository

    @Before
    fun setUp() {
        InMemoryCustomerStore.resetCustomers()
        repository = DefaultCustomerRepository(InMemoryCustomerStore)
    }

    @After
    fun tearDown() {
        InMemoryCustomerStore.resetCustomers()
    }

    @Test
    fun getCustomer_returnsCustomerFromStore() = runTest {
        val customer = repository.getCustomer(1)

        assertEquals("Alice Wonderland", customer?.name)
        assertEquals("alice@example.com", customer?.email)
    }

    @Test
    fun saveCustomer_addsCustomerToStore() = runTest {
        val result = repository.saveCustomer(
            id = null,
            name = "Repo Customer",
            email = "repo@example.com",
        )

        assertTrue(result is SaveCustomerResult.Success)
        assertEquals("Repo Customer", InMemoryCustomerStore.customersSnapshot().last().name)
    }

    @Test
    fun reset_restoresInitialStoreContents() = runTest {
        repository.saveCustomer(id = null, name = "Temp", email = "temp@example.com")

        repository.reset()

        assertEquals(5, InMemoryCustomerStore.customersSnapshot().size)
    }
}
