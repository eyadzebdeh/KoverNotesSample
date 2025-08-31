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

// New: Only-diff-files coverage control and computed include patterns
val onlyDiffFlag = (project.findProperty("only_diff_files") as? String)?.toBooleanStrictOrNull()
extra["koverOnlyDiffFiles"] = onlyDiffFlag ?: false

fun computeDiffIncludedClasses(): List<String> {
    // Run: git diff --name-only main
    val process = try {
        ProcessBuilder(listOf("git", "diff", "--name-only", "main"))
            .redirectErrorStream(true)
            .start()
    } catch (_: Exception) {
        return emptyList()
    }
    val out = process.inputStream.bufferedReader().readText()
    // Don't fail build if git returns non-zero; just treat as no diff
    val files = out.lineSequence()
        .map { it.trim() }
        .filter { it.endsWith(".kt") }
        .toList()
    if (files.isEmpty()) return emptyList()

    val names = files.map { java.io.File(it).nameWithoutExtension }.distinct()
    // Build broad glob patterns to catch class files generated from Kotlin sources
    val patterns = mutableSetOf<String>()
    names.forEach { base ->
        // Match classes and companion/object/etc. generated from this file name
        patterns += "*${'$'}base*"
        // Also include top-level file classes like FooKt
        patterns += "*${'$'}{base}Kt*"
        // And any FQN form with a dot before name
        patterns += "*.${'$'}base*"
    }
    return patterns.toList()
}

extra["koverDiffIncludedClasses"] = if (extra["koverOnlyDiffFiles"] as Boolean) computeDiffIncludedClasses() else emptyList<String>()