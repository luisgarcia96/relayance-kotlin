package com.kirabium.relayance.di

import com.kirabium.relayance.data.repository.DefaultCustomerRepository
import com.kirabium.relayance.data.store.InMemoryCustomerStore
import com.kirabium.relayance.domain.repository.CustomerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideCustomerStore(): InMemoryCustomerStore = InMemoryCustomerStore

    @Provides
    @Singleton
    fun provideCustomerRepository(
        customerStore: InMemoryCustomerStore,
    ): CustomerRepository {
        return DefaultCustomerRepository(customerStore = customerStore)
    }
}
