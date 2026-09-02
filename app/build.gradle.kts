plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.nexus.app"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.nexus.app"

        minSdk = 26
        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
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

    // =========================================================
    // COMPOSE
    // =========================================================

    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    implementation(
        libs.androidx.compose.material3
    )

    implementation(
        libs.androidx.compose.ui
    )

    implementation(
        libs.androidx.compose.ui.graphics
    )

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )

    implementation(
        libs.androidx.activity.compose
    )

    // =========================================================
    // ANDROID CORE
    // =========================================================

    implementation(
        libs.androidx.core.ktx
    )

    // =========================================================
    // LIFECYCLE / VIEWMODEL
    // =========================================================

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0"
    )

    // =========================================================
    // NAVIGATION
    // =========================================================

    implementation(
        "androidx.navigation:navigation-compose:2.10.0"
    )

    // =========================================================
    // ROOM DATABASE
    // =========================================================

    implementation(
        libs.androidx.room.runtime
    )

    implementation(
        libs.androidx.room.ktx
    )

    ksp(
        libs.androidx.room.compiler
    )

    // =========================================================
    // UNIT TEST
    // =========================================================

    testImplementation(
        libs.junit
    )

    // =========================================================
    // ANDROID TEST
    // =========================================================

    androidTestImplementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    // =========================================================
    // DEBUG
    // =========================================================

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )

    // =========================================================
// NETWORK / API
// =========================================================

    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")

}