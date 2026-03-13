import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hiltAndroid)
    id("kotlin-kapt")
    id("jacoco")
}
tasks.withType<Test> {
    extensions.configure(JacocoTaskExtension::class) {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

jacoco {
    toolVersion = "0.8.8"
}

android {
    namespace = "com.kirabium.relayance"
    compileSdk = 34

    testCoverage {
        version = "0.8.8"
    }

    defaultConfig {
        applicationId = "com.kirabium.relayance"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.kirabium.relayance.bdd.RelayanceCucumberRunner"
        testInstrumentationRunnerArguments["optionsAnnotationPackage"] = "com.kirabium.relayance.bdd"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val jacocoClassExclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "**/*\$Lambda$*.*",
    "**/*\$inlined$*.*",
    "**/*\$serializer*.*",
    "**/*Companion*.*",
    "**/*Preview*.*",
    "**/ComposableSingletons$*.*",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*_MembersInjector*.*",
    "**/*_HiltModules*.*",
    "**/Hilt_*.*",
    "**/*_GeneratedInjector*.*",
    "**/*_ComponentTreeDeps*.*",
    "**/*_AggregatedDeps*.*",
    "**/databinding/*.*",
    "**/*Binding.*",
)

val jacocoTestReport by tasks.registering(JacocoReport::class) {
    dependsOn("testDebugUnitTest")
    group = "Reporting"
    description = "Generate JaCoCo coverage reports for debug unit tests."

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val kotlinDebugTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(jacocoClassExclusions)
    }
    val javaDebugTree = fileTree(layout.buildDirectory.dir("intermediates/javac/debug/classes")) {
        exclude(jacocoClassExclusions)
    }

    classDirectories.setFrom(files(kotlinDebugTree, javaDebugTree))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(
        layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    )
}

tasks.register("androidTestCoverageReport") {
    dependsOn("createDebugCoverageReport")
    group = "Reporting"
    description = "Generate the Android test coverage report for the debug build."
}

tasks.register("coverageReport") {
    dependsOn("jacocoTestReport", "androidTestCoverageReport")
    group = "Reporting"
    description = "Generate both unit-test and Android-test coverage reports for the debug build."
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.cucumber.android)
    androidTestImplementation(libs.cucumber.android.hilt)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
