package com.kirabium.relayance

import android.content.Intent
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kirabium.relayance.data.DummyData
import com.kirabium.relayance.extension.DateExt.Companion.toHumanDate
import com.kirabium.relayance.ui.activity.DetailActivity
import com.kirabium.relayance.ui.composable.DetailTestTags
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomerDetailComposeIntegrationTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @After
    fun tearDown() {
        DummyData.resetCustomers()
    }

    /** Verifie que l'ecran detail affiche bien les informations d'Alice pour l'id `1`. */
    @Test
    fun detailScreen_displaysAliceInformationWhenIntentContainsIdOne() {
        DummyData.resetCustomers()
        ActivityScenario.launch<DetailActivity>(detailIntent()).use {
            val alice = DummyData.findCustomerById(1)!!
            val context = ApplicationProvider.getApplicationContext<Context>()
            val expectedDateText = context.getString(R.string.created_at, alice.createdAt.toHumanDate())

            composeRule.onNodeWithTag(DetailTestTags.CUSTOMER_NAME)
                .assertIsDisplayed()
                .assertTextEquals("Alice Wonderland")

            composeRule.onNodeWithTag(DetailTestTags.CUSTOMER_EMAIL)
                .assertIsDisplayed()
                .assertTextEquals("alice@example.com")

            composeRule.onNodeWithTag(DetailTestTags.CUSTOMER_DATE)
                .assertIsDisplayed()
                .assertTextContains(expectedDateText)
        }
    }

    private fun detailIntent(): Intent {
        return Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_CUSTOMER_ID, 1)
        }
    }
}
