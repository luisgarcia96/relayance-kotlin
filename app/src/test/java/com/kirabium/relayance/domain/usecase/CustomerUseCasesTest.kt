package com.kirabium.relayance.domain.usecase

import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.domain.repository.CustomerRepository
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import com.kirabium.relayance.domain.validation.EmailValidator
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class CustomerUseCasesTest {

    @Test
    fun getCustomerUseCase_returnsCustomerFromRepository() = runTest {
        val expectedCustomer = Customer(1, "Alice", "alice@example.com", Date())
        val repository = FakeCustomerRepository(
            customerById = mutableMapOf(1 to expectedCustomer),
        )

        val customer = GetCustomerUseCase(repository).invoke(1)

        assertEquals(expectedCustomer, customer)
    }

    @Test
    fun observeCustomersUseCase_returnsRepositoryFlow() = runTest {
        val expectedCustomers = listOf(Customer(1, "Alice", "alice@example.com", Date()))
        val repository = FakeCustomerRepository(customers = MutableStateFlow(expectedCustomers))

        val customers = ObserveCustomersUseCase(repository).invoke().first()

        assertEquals(expectedCustomers, customers)
    }

    @Test
    fun saveCustomerUseCase_returnsInvalidEmailWhenValidatorRejectsEmail() = runTest {
        val repository = FakeCustomerRepository()
        val useCase = SaveCustomerUseCase(
            customerRepository = repository,
            emailValidator = StubEmailValidator(isValid = false),
        )

        val result = useCase.invoke(id = null, name = "John", email = "invalid-email")

        assertEquals(SaveCustomerResult.InvalidEmail, result)
        assertTrue(repository.savedRequests.isEmpty())
    }

    @Test
    fun saveCustomerUseCase_delegatesToRepositoryWhenEmailIsValid() = runTest {
        val expectedCustomer = Customer(7, "John", "john@example.com", Date())
        val repository = FakeCustomerRepository(
            saveResult = SaveCustomerResult.Success(expectedCustomer),
        )
        val useCase = SaveCustomerUseCase(
            customerRepository = repository,
            emailValidator = StubEmailValidator(isValid = true),
        )

        val result = useCase.invoke(id = 7, name = "John", email = "john@example.com")

        assertSame(repository.saveResult, result)
        assertEquals(listOf(Triple(7, "John", "john@example.com")), repository.savedRequests)
    }
}

private class StubEmailValidator(
    private val isValid: Boolean,
) : EmailValidator {
    override fun isValid(email: String): Boolean = isValid
}

private class FakeCustomerRepository(
    private val customers: MutableStateFlow<List<Customer>> = MutableStateFlow(emptyList()),
    private val customerById: MutableMap<Int, Customer> = mutableMapOf(),
    val saveResult: SaveCustomerResult = SaveCustomerResult.Success(
        Customer(1, "Saved", "saved@example.com", Date())
    ),
) : CustomerRepository {

    val savedRequests = mutableListOf<Triple<Int?, String, String>>()

    override fun observeCustomers(): Flow<List<Customer>> = customers

    override fun observeCustomer(id: Int): Flow<Customer?> = MutableStateFlow(customerById[id])

    override suspend fun getCustomer(id: Int): Customer? = customerById[id]

    override suspend fun saveCustomer(id: Int?, name: String, email: String): SaveCustomerResult {
        savedRequests += Triple(id, name, email)
        return saveResult
    }

    override suspend fun reset() = Unit
}
