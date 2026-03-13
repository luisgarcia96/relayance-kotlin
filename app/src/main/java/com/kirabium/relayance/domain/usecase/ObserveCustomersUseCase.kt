package com.kirabium.relayance.domain.usecase

import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCustomersUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
) {
    operator fun invoke(): Flow<List<Customer>> = customerRepository.observeCustomers()
}
