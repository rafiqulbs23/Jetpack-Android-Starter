plugins {
    alias(libs.plugins.jetpack.library)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.dokka)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "com.aristopharma.v2.feature.splash"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:preferences"))
    implementation(project(":core:network"))
    implementation(project(":core:di"))
    implementation(project(":feature:auth"))

    // ... Retrofit
    implementation(libs.retrofit.core)

    // ... Firebase
    implementation(project(":firebase:auth"))
}