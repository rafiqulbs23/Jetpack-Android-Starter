plugins {
    alias(libs.plugins.jetpack.ui.library)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.dokka)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.aristopharma.v2.feature.attendance"
}

dependencies {
    // ... Room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    
    // ... Modules
    implementation(project(":core:ui"))
    implementation(project(":core:android"))
    implementation(project(":core:network"))
    implementation(project(":core:preferences"))
    implementation(project(":core:room"))
    
    // Dashboard module for integration
    implementation(project(":feature:dashboard"))
    
    // ... Retrofit
    implementation(libs.retrofit.core)
}
