package com.kirabium.relayance.ui.viewmodel

import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.domain.repository.CustomerRepository
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import com.kirabium.relayance.util.MainDispatcherRule
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadCustomer_setsShouldCloseWhenIdIsInvalid() = runTest {
        val viewModel = DetailViewModel(FakeDetailCustomerRepository())

        viewModel.loadCustomer(0)

        assertTrue(viewModel.uiState.value.shouldClose)
    }

    @Test
    fun loadCustomer_updatesUiStateWhenCustomerExists() = runTest {
        val repository = FakeDetailCustomerRepository()
        val expectedCustomer = Customer(5, "Diana", "diana@example.com", Date())
        repository.setCustomer(5, expectedCustomer)
        val viewModel = DetailViewModel(repository)

        viewModel.loadCustomer(5)
        advanceUntilIdle()

        assertEquals(expectedCustomer, viewModel.uiState.value.customer)
        assertFalse(viewModel.uiState.value.shouldClose)
    }

    @Test
    fun loadCustomer_setsShouldCloseWhenObservedCustomerBecomesNull() = runTest {
        val repository = FakeDetailCustomerRepository()
        val customer = Customer(3, "Charlie", "charlie@example.com", Date())
        repository.setCustomer(3, customer)
        val viewModel = DetailViewModel(repository)
        viewModel.loadCustomer(3)
        advanceUntilIdle()

        repository.setCustomer(3, null)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.shouldClose)
    }

    @Test
    fun loadCustomer_ignoresRepeatedCustomerId() = runTest {
        val repository = FakeDetailCustomerRepository()
        val viewModel = DetailViewModel(repository)

        viewModel.loadCustomer(8)
        viewModel.loadCustomer(8)
        advanceUntilIdle()

        assertEquals(1, repository.observeCustomerCalls)
    }

    @Test
    fun onCloseHandled_resetsCloseFlag() = runTest {
        val viewModel = DetailViewModel(FakeDetailCustomerRepository())
        viewModel.loadCustomer(-1)

        viewModel.onCloseHandled()

        assertFalse(viewModel.uiState.value.shouldClose)
    }
}

private class FakeDetailCustomerRepository : CustomerRepository {

    private val observedCustomers = mutableMapOf<Int, MutableStateFlow<Customer?>>()
    var observeCustomerCalls: Int = 0
        private set

    override fun observeCustomers(): Flow<List<Customer>> = MutableStateFlow(emptyList())

    override fun observeCustomer(id: Int): Flow<Customer?> {
        observeCustomerCalls += 1
        return observedCustomers.getOrPut(id) { MutableStateFlow(null) }
    }

    override suspend fun getCustomer(id: Int): Customer? = observedCustomers[id]?.value

    override suspend fun saveCustomer(id: Int?, name: String, email: String): SaveCustomerResult {
        error("Not used in this test")
    }

    override suspend fun reset() = Unit

    fun setCustomer(id: Int, customer: Customer?) {
        observedCustomers.getOrPut(id) { MutableStateFlow(null) }.value = customer
    }
}
