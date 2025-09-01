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
    // Helper to run git diff safely at configuration time without Gradle exec
    fun runGitDiff(refspec: String): String {
        return try {
            val proc = ProcessBuilder(listOf("git", "diff", "--name-only", "--diff-filter=d", refspec))
                .redirectErrorStream(true)
                .start()
            proc.inputStream.bufferedReader().readText()
        } catch (_: Exception) {
            ""
        }
    }

    // Prefer origin/main, fall back to main if origin isn't available
    val raw = sequenceOf("origin/main", "main")
        .map { runGitDiff(it) }
        .firstOrNull { it.isNotBlank() }
        .orEmpty()

    if (raw.isBlank()) return emptyList()

    val files = raw.lines()
        .filter { it.isNotBlank() }
        .filter { it.endsWith(".kt") }
        .mapNotNull {
            it.split(delimiters = arrayOf("kotlin/", "java/")).lastOrNull()
                ?.split(".")
                ?.firstOrNull()
                ?.replace("/", ".")
        }
        .ifEmpty { listOf("") }

    return files
}

extra["koverDiffIncludedClasses"] =
    if (extra["koverOnlyDiffFiles"] as Boolean) computeDiffIncludedClasses() else emptyList<String>()