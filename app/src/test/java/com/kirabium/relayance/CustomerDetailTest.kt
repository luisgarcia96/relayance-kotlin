package com.kirabium.relayance

import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.extension.DateExt.Companion.toHumanDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Locale

class CustomerDetailTest {

    /** Verifie que le format d'affichage d'une date est bien `dd/MM/yyyy`. */
    @Test
    fun toHumanDate_formatsDateAsDayMonthYear() {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2025)
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 5)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val previousLocale = Locale.getDefault()
        try {
            Locale.setDefault(Locale.FRANCE)
            assertEquals("05/01/2025", date.toHumanDate())
        } finally {
            Locale.setDefault(previousLocale)
        }
    }

    /** Verifie qu'un client cree recemment est considere comme "nouveau". */
    @Test
    fun isNewCustomer_returnsTrue_whenCustomerIsRecent() {
        val recentDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -1)
        }.time
        val customer = Customer(1, "Recent", "recent@example.com", recentDate)

        assertTrue(customer.isNewCustomer())
    }

    /** Verifie qu'un client trop ancien n'est plus considere comme "nouveau". */
    @Test
    fun isNewCustomer_returnsFalse_whenCustomerIsOlderThanThreeMonths() {
        val oldDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -3)
            add(Calendar.DAY_OF_MONTH, -1)
        }.time
        val customer = Customer(2, "Old", "old@example.com", oldDate)

        assertFalse(customer.isNewCustomer())
    }

    /** Verifie le cas limite: un client proche du seuil des 3 mois reste "nouveau". */
    @Test
    fun isNewCustomer_returnsTrue_whenCustomerIsWithinBoundary() {
        val boundaryDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -3)
            add(Calendar.DAY_OF_MONTH, 1)
        }.time
        val customer = Customer(3, "Boundary", "boundary@example.com", boundaryDate)

        assertTrue(customer.isNewCustomer())
    }
}
