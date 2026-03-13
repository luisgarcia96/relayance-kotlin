package com.kirabium.relayance.ui.viewmodel

import com.kirabium.relayance.R
import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.domain.repository.CustomerRepository
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import com.kirabium.relayance.domain.usecase.GetCustomerUseCase
import com.kirabium.relayance.domain.usecase.SaveCustomerUseCase
import com.kirabium.relayance.domain.validation.EmailValidator
import com.kirabium.relayance.ui.event.AddCustomerUiEvent
import com.kirabium.relayance.util.MainDispatcherRule
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddCustomerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun saveCustomer_emitsCustomerSaved_whenEmailIsValid() = runTest {
        val repository = FakeCustomerRepository()
        val viewModel = buildViewModel(
            repository = repository,
            emailValidator = StubEmailValidator(isValid = true),
        )
        var event: AddCustomerUiEvent? = null
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            event = viewModel.uiEvents.first()
        }

        viewModel.saveCustomer(
            nameInput = "John Doe",
            emailInput = "john.doe@example.com",
            noNameFallback = "No name",
            noEmailFallback = "No email",
        )
        advanceUntilIdle()

        assertTrue(event is AddCustomerUiEvent.CustomerSaved)
        assertEquals("John Doe", repository.savedNames.single())
        assertEquals("john.doe@example.com", repository.savedEmails.single())
        collectJob.cancel()
    }

    @Test
    fun saveCustomer_emitsErrorMessage_whenEmailIsInvalid() = runTest {
        val repository = FakeCustomerRepository()
        val viewModel = buildViewModel(
            repository = repository,
            emailValidator = StubEmailValidator(isValid = false),
        )
        var event: AddCustomerUiEvent? = null
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            event = viewModel.uiEvents.first()
        }

        viewModel.saveCustomer(
            nameInput = "John Doe",
            emailInput = "invalid-email",
            noNameFallback = "No name",
            noEmailFallback = "No email",
        )
        advanceUntilIdle()

        assertEquals(AddCustomerUiEvent.ShowMessage(R.string.customer_save_error), event)
        assertTrue(repository.savedNames.isEmpty())
        collectJob.cancel()
    }

    private fun buildViewModel(
        repository: FakeCustomerRepository,
        emailValidator: EmailValidator,
    ): AddCustomerViewModel {
        return AddCustomerViewModel(
            getCustomerUseCase = GetCustomerUseCase(repository),
            saveCustomerUseCase = SaveCustomerUseCase(repository, emailValidator),
        )
    }
}

private class StubEmailValidator(
    private val isValid: Boolean,
) : EmailValidator {
    override fun isValid(email: String): Boolean = isValid
}

private class FakeCustomerRepository : CustomerRepository {

    private val customers = MutableStateFlow<List<Customer>>(emptyList())
    val savedNames = mutableListOf<String>()
    val savedEmails = mutableListOf<String>()

    override fun observeCustomers(): Flow<List<Customer>> = customers

    override fun observeCustomer(id: Int): Flow<Customer?> = MutableStateFlow(customers.value.firstOrNull { it.id == id })

    override suspend fun getCustomer(id: Int): Customer? = customers.value.firstOrNull { it.id == id }

    override suspend fun saveCustomer(id: Int?, name: String, email: String): SaveCustomerResult {
        savedNames += name
        savedEmails += email
        val newCustomer = Customer(
            id = id ?: (customers.value.maxOfOrNull { it.id } ?: 0) + 1,
            name = name,
            email = email,
            createdAt = Date(),
        )
        customers.value = customers.value + newCustomer
        return SaveCustomerResult.Success(newCustomer)
    }

    override suspend fun reset() {
        customers.value = emptyList()
        savedNames.clear()
        savedEmails.clear()
    }
}
