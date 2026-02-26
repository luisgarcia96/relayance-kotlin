package com.kirabium.relayance

import com.kirabium.relayance.data.DummyData
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Date

class CustomerListTest {

    /** Verifie que `generateDate(0)` renvoie une date tres proche du moment courant. */
    @Test
    fun generateDate_withZeroMonthBack_returnsNearNowDate() {
        assertDateIsWithinExpectedMonth(0, DummyData.generateDate(0))
    }

    /** Verifie que `generateDate(1)` recule bien la date d'environ un mois. */
    @Test
    fun generateDate_withOneMonthBack_returnsDateInPastMonth() {
        assertDateIsWithinExpectedMonth(1, DummyData.generateDate(1))
    }

    /** Verifie que `generateDate(3)` recule bien la date d'environ trois mois. */
    @Test
    fun generateDate_withThreeMonthsBack_returnsDateInPastMonth() {
        assertDateIsWithinExpectedMonth(3, DummyData.generateDate(3))
    }

    /** Verifie que `generateDate(12)` recule bien la date d'environ douze mois. */
    @Test
    fun generateDate_withTwelveMonthsBack_returnsDateInPastMonth() {
        assertDateIsWithinExpectedMonth(12, DummyData.generateDate(12))
    }

    private fun assertDateIsWithinExpectedMonth(monthsBack: Int, date: Date) {
        val lowerBound = Calendar.getInstance().apply {
            add(Calendar.MONTH, -monthsBack)
            add(Calendar.SECOND, -2)
        }.time

        val upperBound = Calendar.getInstance().apply {
            add(Calendar.MONTH, -monthsBack)
            add(Calendar.SECOND, 2)
        }.time

        assertTrue(date.after(lowerBound) && date.before(upperBound))
    }
}
