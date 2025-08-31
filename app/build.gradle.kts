
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlinx.kover")
}


android {
    namespace = "com.example.kovernotes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kovernotes"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.11"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

// Centralized Kover configuration: use values from root project
val koverExcludedPackages = rootProject.extra["koverExcludedPackages"] as List<String>
val koverExcludedClasses = rootProject.extra["koverExcludedClasses"] as List<String>
val koverMinLine = rootProject.extra["koverMinLine"] as Int
val koverMinBranch = rootProject.extra["koverMinBranch"] as Int
val koverOnlyDiffFiles = rootProject.extra["koverOnlyDiffFiles"] as Boolean
@Suppress("UNCHECKED_CAST")
val koverDiffIncludedClasses = rootProject.extra["koverDiffIncludedClasses"] as List<String>

kover {
    reports {
        total {
            filters {
                if (koverOnlyDiffFiles && koverDiffIncludedClasses.isNotEmpty()) {
                    includes {
                        classes(*koverDiffIncludedClasses.toTypedArray())
                    }
                }
                excludes {
                    androidGeneratedClasses()
                    annotatedBy("*Generated*")
                    packages(*koverExcludedPackages.toTypedArray())
                    classes(*koverExcludedClasses.toTypedArray())
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Jetpack Compose BOM and libs
    implementation(platform("androidx.compose:compose-bom:2024.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Module deps
    implementation(project(":core:api"))
    implementation(project(":core:impl"))
    implementation(project(":feature:noteslist"))
    implementation(project(":feature:addnote"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation(project(":core:fake"))
    // Access impl repo behavior in tests
    testImplementation(project(":core:impl"))
}
