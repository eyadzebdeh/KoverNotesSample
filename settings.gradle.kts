pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.5.2"
        id("com.android.library") version "8.5.2"
        id("org.jetbrains.kotlin.android") version "2.0.20"
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
        id("org.jetbrains.kotlinx.kover") version "0.9.1"
        id("com.google.dagger.hilt.android") version "2.52"
        id("com.google.devtools.ksp") version "2.0.20-1.0.24"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "KoverNotesSample"
include(":app")
include(":core:api")
include(":core:impl")
include(":core:fake")
include(":feature:noteslist")
include(":feature:addnote")