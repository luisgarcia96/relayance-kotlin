package com.kirabium.relayance

import com.kirabium.relayance.data.DummyData
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CustomerAddEditTest {

    @Before
    fun setUp() {
        DummyData.resetCustomers()
    }

    @After
    fun tearDown() {
        DummyData.resetCustomers()
    }

    /** Verifie qu'un ajout augmente la liste et persiste bien les informations saisies. */
    @Test
    fun addCustomer_addsNewCustomerAndIncreasesListSize() {
        val initialSize = DummyData.customers.size

        val added = DummyData.addCustomer("Test Name", "test@example.com")

        assertEquals(initialSize + 1, DummyData.customers.size)
        assertNotNull(DummyData.findCustomerById(added.id))
        assertEquals("Test Name", added.name)
        assertEquals("test@example.com", added.email)
    }

    /** Verifie que la mise a jour modifie bien le nom et l'email d'un client existant. */
    @Test
    fun updateCustomer_updatesExistingCustomerData() {
        val existingId = 1

        val updated = DummyData.updateCustomer(existingId, "Alice Updated", "alice.updated@example.com")

        assertNotNull(updated)
        assertEquals("Alice Updated", updated?.name)
        assertEquals("alice.updated@example.com", updated?.email)
        assertEquals("Alice Updated", DummyData.findCustomerById(existingId)?.name)
    }

    /** Verifie qu'une mise a jour sur un id inconnu renvoie `null` sans effet de bord. */
    @Test
    fun updateCustomer_returnsNull_whenCustomerDoesNotExist() {
        val updated = DummyData.updateCustomer(999, "Unknown", "unknown@example.com")

        assertEquals(null, updated)
    }

    /** Verifie que l'edition conserve la date de creation initiale du client. */
    @Test
    fun updateCustomer_keepsInitialCreationDate() {
        val existingId = 2
        val originalDate = DummyData.findCustomerById(existingId)?.createdAt

        val updated = DummyData.updateCustomer(existingId, "Bob Updated", "bob.updated@example.com")

        assertTrue(updated?.createdAt == originalDate)
    }
}
