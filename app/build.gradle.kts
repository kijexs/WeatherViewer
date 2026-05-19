plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("org.jetbrains.kotlinx.kover") version "0.9.7"
}

android {
    namespace = "com.example.weatherviewer"
    compileSdk {
        version =
            release(36) {
                minorApiLevel = 1
            }
    }

    defaultConfig {
        applicationId = "com.example.weatherviewer"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Retrofit
    val retrofitVersion = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // Room
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp(libs.androidx.room.compiler)

    implementation("androidx.compose.material:material:1.11.1")

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.kotlinx.serialization.json)

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    testImplementation("app.cash.turbine:turbine:1.2.1")
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.client.mock)
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("com.google.guava:guava:33.6.0-android")
    implementation("com.google.guava:guava:33.6.0-android")
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

configurations.all {
    exclude(group = "com.google.guava", module = "listenablefuture")
}

configurations.androidTestImplementation {
    exclude(group = "com.google.guava", module = "listenablefuture")
}

tasks.withType<Test> {
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

kover {
    reports {
        filters {
            excludes {
                classes("*_HiltModules*")
                classes("*_HiltModules_*")
                classes("dagger.hilt.internal.aggregatedroot.codegen.*")
                classes("hilt_aggregated_deps.*")

                classes("com.example.weatherviewer.di.*")

                classes("com.example.weatherviewer.ui.theme.*")

                classes("com.example.weatherviewer.data.local.*")

                classes("com.example.weatherviewer.ui.screen.*")

                classes("com.example.weatherviewer.WeatherViewerApplication")
                classes("com.example.weatherviewer.MainActivity")
            }
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("config/detekt/detekt.yml")
}
