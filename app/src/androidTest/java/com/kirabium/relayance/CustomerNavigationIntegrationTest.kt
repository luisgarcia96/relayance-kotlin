package com.kirabium.relayance

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kirabium.relayance.data.DummyData
import com.kirabium.relayance.ui.activity.DetailActivity
import com.kirabium.relayance.ui.activity.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomerNavigationIntegrationTest {

    @After
    fun tearDown() {
        DummyData.resetCustomers()
    }

    /** Verifie que le clic sur le premier client ouvre l'ecran detail avec le bon `customer_id`. */
    @Test
    fun clickOnFirstItem_launchesDetailIntentWithCustomerId() {
        DummyData.resetCustomers()
        Intents.init()
        try {
            ActivityScenario.launch(MainActivity::class.java).use {
                onView(withId(R.id.customerRecyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
                )

                intended(
                    allOf(
                        hasComponent(DetailActivity::class.java.name),
                        hasExtra(DetailActivity.EXTRA_CUSTOMER_ID, 1)
                    )
                )
            }
        } finally {
            Intents.release()
        }
    }
}
