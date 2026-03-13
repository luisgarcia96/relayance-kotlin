package com.kirabium.relayance.domain.validation

import javax.inject.Inject

class RegexEmailValidator @Inject constructor() : EmailValidator {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    override fun isValid(email: String): Boolean = emailRegex.matches(email.trim())
}
