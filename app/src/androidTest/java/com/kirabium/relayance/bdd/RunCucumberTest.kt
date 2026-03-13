package com.kirabium.relayance.bdd

import io.cucumber.junit.CucumberOptions

@CucumberOptions(
    features = ["features"],
    glue = ["com.kirabium.relayance.bdd"],
    plugin = ["pretty", "summary"]
)
class RunCucumberTest
