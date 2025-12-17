/*
 * Copyright 2025 Md. Rafiqul Islam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.jetpack.ui.library)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.dokka)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aristopharma.v2.feature.auth"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("Boolean", "ENABLE_BYPASS_OTP", "true")
            buildConfigField("Boolean", "IS_AUTOMIC_OTP_ENABLE", "true")
        }
        release {
            buildConfigField("Boolean", "ENABLE_BYPASS_OTP", "true")
            buildConfigField("Boolean", "IS_AUTOMIC_OTP_ENABLE", "true")
        }
    }
}

dependencies {
    // ... Modules
    implementation(project(":core:ui"))
    implementation(project(":core:android"))
    implementation(project(":core:preferences"))
    implementation(project(":core:network"))

    // ... DataStore
    implementation(libs.androidx.dataStore.core)
    
    // ... Retrofit
    implementation(libs.retrofit.core)
    
    // ... Firebase
    implementation(platform(libs.firebase.bom))

    // ... Google Play Services - SMS Retriever API
    implementation(libs.play.services.auth.api.phone)

    implementation(project(":firebase:auth"))
    implementation(project(":feature:notification"))

}