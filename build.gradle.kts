// Centralized Kover configuration values (used by modules)
extra["koverExcludedPackages"] = listOf(
    "dagger.hilt.*",
    "hilt_aggregated_deps",
)
extra["koverExcludedClasses"] = listOf(
    // Generated Hilt artifacts
    "hilt_aggregated_deps.*",
    "com.example.*HiltWrapper*",
    "com.example.*_Hilt*",
    "*Dagger*",
    "*_Factory",
    "*.*_HiltModules*",
    // Android/boilerplate
    "com.example.kovernotes.BuildConfig",
    "com.example.kovernotes.*App",
    "*.*ActivityKt*",
    "*.*ComposableSingletons*",
    // App entry point and Compose screens across features
    "*.*ScreenKt*",
    // DI modules in any module
    "*.di.*",
)
extra["koverMinLine"] = (project.findProperty("line_coverage") as? String)?.toInt() ?: 0
extra["koverMinBranch"] = (project.findProperty("branch_coverage") as? String)?.toInt() ?: 0