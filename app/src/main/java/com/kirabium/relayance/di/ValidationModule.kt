package com.kirabium.relayance.di

import com.kirabium.relayance.domain.validation.EmailValidator
import com.kirabium.relayance.domain.validation.RegexEmailValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ValidationModule {

    @Binds
    abstract fun bindEmailValidator(
        regexEmailValidator: RegexEmailValidator,
    ): EmailValidator
}
