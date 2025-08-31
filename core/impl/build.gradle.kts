import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlinx.kover")
}


android {
    namespace = "com.example.kovernotes.core.impl"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
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

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")

    // Test deps
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.5")
}
