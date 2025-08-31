import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlinx.kover")
}


android {
    namespace = "com.example.kovernotes.feature.noteslist"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.6.11" }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

// Centralized Kover configuration: use values from root project
val koverExcludedPackages = rootProject.extra["koverExcludedPackages"] as List<String>
val koverExcludedClasses = rootProject.extra["koverExcludedClasses"] as List<String>
val koverMinLine = rootProject.extra["koverMinLine"] as Int
val koverMinBranch = rootProject.extra["koverMinBranch"] as Int

kover {
    reports {
        total {
            filters {
                excludes {
                    androidGeneratedClasses()
                    annotatedBy("*Generated*")
                    packages(*koverExcludedPackages.toTypedArray())
                    classes(
                        *koverExcludedClasses.toTypedArray(),
                        // module-specific additional exclusions
                    )
                }
            }
            verify {
                rule { minBound(koverMinLine, CoverageUnit.LINE) }
                rule { minBound(koverMinBranch, CoverageUnit.BRANCH) }
            }
        }
    }
}


dependencies {
    implementation(project(":core:api"))

    implementation(platform("androidx.compose:compose-bom:2024.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")

    // Test deps
    testImplementation(project(":core:fake"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}
