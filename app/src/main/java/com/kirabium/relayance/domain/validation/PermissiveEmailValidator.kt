package com.kirabium.relayance.domain.validation

import javax.inject.Inject

class PermissiveEmailValidator @Inject constructor() : EmailValidator {
    override fun isValid(email: String): Boolean = true
}
