package com.kirabium.relayance.domain.usecase

import com.kirabium.relayance.domain.repository.CustomerRepository
import com.kirabium.relayance.domain.repository.SaveCustomerResult
import com.kirabium.relayance.domain.validation.EmailValidator
import javax.inject.Inject

class SaveCustomerUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val emailValidator: EmailValidator,
) {
    suspend operator fun invoke(id: Int?, name: String, email: String): SaveCustomerResult {
        if (!emailValidator.isValid(email)) {
            return SaveCustomerResult.InvalidEmail
        }
        return customerRepository.saveCustomer(id = id, name = name, email = email)
    }
}
