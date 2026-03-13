package com.kirabium.relayance.ui.viewmodel

import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.domain.repository.CustomerRepository
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import com.kirabium.relayance.domain.usecase.ObserveCustomersUseCase
import com.kirabium.relayance.util.MainDispatcherRule
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun init_updatesUiStateWhenCustomersFlowEmits() = runTest {
        val repository = FakeObserveCustomersRepository()
        val viewModel = MainViewModel(ObserveCustomersUseCase(repository))
        val customers = listOf(
            Customer(1, "Alice", "alice@example.com", Date()),
            Customer(2, "Bob", "bob@example.com", Date()),
        )

        repository.emit(customers)
        advanceUntilIdle()

        assertEquals(customers, viewModel.uiState.value.customers)
    }
}

private class FakeObserveCustomersRepository : CustomerRepository {

    private val customers = MutableStateFlow<List<Customer>>(emptyList())

    override fun observeCustomers(): StateFlow<List<Customer>> = customers.asStateFlow()

    override fun observeCustomer(id: Int): Flow<Customer?> = MutableStateFlow(null)

    override suspend fun getCustomer(id: Int): Customer? = null

    override suspend fun saveCustomer(id: Int?, name: String, email: String): SaveCustomerResult {
        error("Not used in this test")
    }

    override suspend fun reset() = Unit

    fun emit(newCustomers: List<Customer>) {
        customers.value = newCustomers
    }
}
