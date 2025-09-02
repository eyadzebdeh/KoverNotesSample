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
    namespace = "com.example.kovernotes.feature.editnote"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

// Centralized Kover configuration
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
                    project.logger.lifecycle("[KOVER_DIFF] ${project.path}: applying includes (${koverDiffIncludedClasses.size}) -> $koverDiffIncludedClasses")
                    includes { classes(*koverDiffIncludedClasses.toTypedArray()) }
                } else if (koverOnlyDiffFiles) {
                    project.logger.lifecycle("[KOVER_DIFF] ${project.path}: only_diff_files=true but include list is empty (no classes will be counted)")
                }
                excludes {
                    androidGeneratedClasses()
                    annotatedBy("*Generated*")
                    packages(*koverExcludedPackages.toTypedArray())
                    classes(
                        *koverExcludedClasses.toTypedArray(),
                        // module-specific additional exclusions
                        "com.example.kovernotes.feature.editnote.EditNoteScreenKt",
                        "com.example.kovernotes.feature.editnote.di.*",
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

    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")

    // Test deps
    testImplementation(project(":core:fake"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.5")
}
