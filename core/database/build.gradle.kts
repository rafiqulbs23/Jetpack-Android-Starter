plugins {
    alias(libs.plugins.jetpack.library)
    alias(libs.plugins.jetpack.dagger.hilt)
    alias(libs.plugins.jetpack.dokka)
}

android {
    namespace = "com.aristopharma.v2.core.database"
}

dependencies {
    // Room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    
    // Core modules
    implementation(project(":core:android"))
}
