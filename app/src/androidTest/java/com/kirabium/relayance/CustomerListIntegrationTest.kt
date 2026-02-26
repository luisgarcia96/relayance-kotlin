package com.kirabium.relayance

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kirabium.relayance.data.DummyData
import com.kirabium.relayance.ui.activity.MainActivity
import com.kirabium.relayance.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId

@RunWith(AndroidJUnit4::class)
class CustomerListIntegrationTest {

    @After
    fun tearDown() {
        DummyData.resetCustomers()
    }

    /** Verifie qu'au lancement de l'application, la liste affiche bien les 5 clients precodes. */
    @Test
    fun appStart_displaysFiveCustomers() {
        DummyData.resetCustomers()
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.customerRecyclerView)).check(withItemCount(5))
        }
    }
}
