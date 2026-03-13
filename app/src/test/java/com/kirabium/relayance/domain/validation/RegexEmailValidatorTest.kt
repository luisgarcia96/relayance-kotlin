package com.kirabium.relayance.domain.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegexEmailValidatorTest {

    private val validator = RegexEmailValidator()

    @Test
    fun isValid_returnsTrue_forStandardEmailAddress() {
        assertTrue(validator.isValid("john.doe@example.com"))
    }

    @Test
    fun isValid_returnsFalse_forMalformedEmailAddress() {
        assertFalse(validator.isValid("invalid-email"))
    }
}
