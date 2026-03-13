package com.kirabium.relayance.domain.validation

interface EmailValidator {
    fun isValid(email: String): Boolean
}
