package com.kirabium.relayance.bdd

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.kirabium.relayance.R
import com.kirabium.relayance.data.store.InMemoryCustomerStore
import com.kirabium.relayance.ui.activity.MainActivity
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.assertNull

class AddCustomerSteps {

    private var mainScenario: ActivityScenario<MainActivity>? = null

    @Before
    fun resetCustomers() {
        InMemoryCustomerStore.resetCustomers()
    }

    @After
    fun closeScenario() {
        mainScenario?.close()
        mainScenario = null
        InMemoryCustomerStore.resetCustomers()
    }

    @Given("the customer list is displayed")
    fun theCustomerListIsDisplayed() {
        mainScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.customerRecyclerView)).check(matches(isDisplayed()))
    }

    @Given("I am on the add customer screen")
    fun iAmOnTheAddCustomerScreen() {
        onView(withId(R.id.addCustomerFab)).perform(click())
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()))
    }

    @When("I enter {string} as the customer name")
    fun iEnterAsTheCustomerName(name: String) {
        onView(withId(R.id.nameEditText)).perform(clearText(), replaceText(name), closeSoftKeyboard())
    }

    @When("I enter {string} as the customer email")
    fun iEnterAsTheCustomerEmail(email: String) {
        onView(withId(R.id.emailEditText)).perform(clearText(), replaceText(email), closeSoftKeyboard())
    }

    @When("I save the customer")
    fun iSaveTheCustomer() {
        onView(withId(R.id.saveFab)).perform(click())
    }

    @Then("I return to the customer list")
    fun iReturnToTheCustomerList() {
        onView(withId(R.id.customerRecyclerView)).check(matches(isDisplayed()))
    }

    @Then("I see a message confirming that the customer was saved")
    fun iSeeAMessageConfirmingThatTheCustomerWasSaved() {
        onView(withText(R.string.customer_saved_success)).check(matches(isDisplayed()))
    }

    @Then("I see {string} in the customer list")
    fun iSeeInTheCustomerList(name: String) {
        onView(withText(name)).check(matches(isDisplayed()))
    }

    @And("I see {string} for this customer")
    fun iSeeForThisCustomer(email: String) {
        onView(withText(email)).check(matches(isDisplayed()))
    }

    @Then("I remain on the add customer screen")
    fun iRemainOnTheAddCustomerScreen() {
        onView(withId(R.id.saveFab)).check(matches(isDisplayed()))
    }

    @Then("I see a message indicating that the customer cannot be saved")
    fun iSeeAMessageIndicatingThatTheCustomerCannotBeSaved() {
        onView(withText(R.string.customer_save_error)).check(matches(isDisplayed()))
    }

    @Then("I do not see {string} in the customer list")
    fun iDoNotSeeInTheCustomerList(name: String) {
        assertNull(InMemoryCustomerStore.customersSnapshot().firstOrNull { it.name == name })
    }
}
