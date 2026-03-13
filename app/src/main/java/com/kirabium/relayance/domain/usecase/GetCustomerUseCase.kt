package com.kirabium.relayance.domain.usecase

import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.domain.repository.CustomerRepository
import javax.inject.Inject

class GetCustomerUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
) {
    suspend operator fun invoke(id: Int): Customer? = customerRepository.getCustomer(id)
}
