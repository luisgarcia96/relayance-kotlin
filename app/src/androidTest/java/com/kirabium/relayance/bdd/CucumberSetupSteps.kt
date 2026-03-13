package com.kirabium.relayance.bdd

import androidx.test.platform.app.InstrumentationRegistry
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.Assert.assertEquals

class CucumberSetupSteps {

    private lateinit var targetPackageName: String

    @Given("the Relayance test instrumentation is running")
    fun theRelayanceTestInstrumentationIsRunning() {
        targetPackageName = InstrumentationRegistry.getInstrumentation().targetContext.packageName
    }

    @Then("the target application package is {string}")
    fun theTargetApplicationPackageIs(expectedPackageName: String) {
        assertEquals(expectedPackageName, targetPackageName)
    }
}
