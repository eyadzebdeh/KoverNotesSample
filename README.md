# Kover Notes Sample (Android + Kover)

A minimal Android sample that demonstrates:
- Kotlin + ViewModel + simple Repository/domain
- Unit tests (no instrumentation)
- Kover HTML/XML reports with centralized configuration and coverage verification

## Quick Start
1. Open in Android Studio (Giraffe/AGP 8.5+ / Kotlin 2.0+).
2. Sync Gradle.
3. Run tests:
   - All modules: `./gradlew test`
4. Generate coverage reports:
   - All Kover-enabled modules: `./gradlew koverHtmlReport`
   - Per-module HTML reports:
     - app/build/reports/kover/index.html
     - core/impl/build/reports/kover/index.html
     - feature/addnote/build/reports/kover/index.html
     - feature/noteslist/build/reports/kover/index.html
5. Verify coverage gate:
   - `./gradlew koverVerify -P line_coverage=80 -P branch_coverage=70`

## Project Structure (Modularized)
- core:api
  - Pure API module with domain model and repository contract
  - Contains: `domain/model/Note`, `domain/repository/NoteRepository`
  - No Kover plugin applied (not counted in coverage)
- core:impl
  - Concrete data layer for the API
  - Contains: `InMemoryNoteRepository` and Hilt DI provider
  - Kover enabled (repository implementation is covered)
- core:fake
  - Lightweight fake repository for tests
  - No Kover plugin applied (not counted in coverage)
- feature:addnote
  - Feature module housing `AddNoteUseCase`, Hilt wiring, and `AddNoteViewModel`/screen
  - Kover enabled; UI composables and DI classes are excluded from coverage
- feature:noteslist
  - Feature module housing `GetNotesUseCase`, Hilt wiring, and `NotesListViewModel`/screen
  - Kover enabled; UI composables and DI classes are excluded from coverage
- app
  - Compose navigation host and Android entry point (Hilt Application + Activity)
  - Depends on core:api, core:impl, and both features
  - Kover enabled; Activity/Compose screens and DI/generated classes are excluded

## Centralized Kover Configuration
Shared coverage configuration (exclusion lists and thresholds) is defined once in the root `build.gradle.kts` using Gradle `extra` properties:
- Excluded packages/classes are provided via:
  - `extra["koverExcludedPackages"]`
  - `extra["koverExcludedClasses"]`
- Thresholds are provided via:
  - `extra["koverMinLine"]` and `extra["koverMinBranch"]`
  - These can be supplied at invocation time, e.g.: `-P line_coverage=80 -P branch_coverage=70`
- Diff-only coverage option:
  - `extra["koverOnlyDiffFiles"]` (driven by Gradle property `-P only_diff_files=true`)
  - When enabled, Kover will include only classes corresponding to files changed on the current branch vs `main`.
    - Implementation uses `git diff --name-only --diff-filter=d origin/main` (or `main` if `origin/main` is not present) to detect changed Kotlin files and derives class globs from filenames.
    - If git is unavailable or no changed Kotlin files are detected, no classes are included (coverage may be zero).

Kover-enabled modules (app, core:impl, feature:addnote, feature:noteslist) consume these values and apply the same filters and verify rules. API and fake modules do not apply the Kover plugin.

Common exclusions include:
- Generated DI artifacts (Hilt): `hilt_aggregated_deps.*`, `*_Factory`, `*Dagger*`, `*_Hilt*`, `*HiltWrapper*`, `*.*_HiltModules*`
- Android boilerplate: `BuildConfig`, `*App`, `*ActivityKt*`, `*ComposableSingletons*`
- Compose screen files and DI modules: `feature.*.*ScreenKt`, `**.di.*`

## Coverage commands: diff vs no-diff

Full project (no diff filtering):
- HTML report (all Kover-enabled modules):
  - `./gradlew koverHtmlReport`
- Verify with thresholds (example 80% lines, 70% branches):
  - `./gradlew koverVerify -P line_coverage=80 -P branch_coverage=70`

Only changed files (diff vs main):
- HTML report for only changed Kotlin files:
  - `./gradlew koverHtmlReport -P only_diff_files=true`
- Verify only changed files with thresholds:
  - `./gradlew koverVerify -P only_diff_files=true -P line_coverage=80 -P branch_coverage=70`

Notes:
- Diff detection uses `git diff --name-only --diff-filter=d origin/main` (falls back to `main` if needed). If the ref isn’t available locally or no Kotlin files changed, the include set may be empty and coverage can be zero.
- Exclusion filters still apply in both modes.

## Tests
- Repository implementation: `InMemoryNoteRepositoryTest`
- Use cases: `AddNoteUseCaseTest`, `GetNotesUseCaseTest`
- ViewModels: `AddNoteViewModelTest`, `NotesListViewModelTest` (+ empty list case)

Run all tests:
```
./gradlew test
```

## Build Notes
- KSP incremental is disabled to avoid a known cache error during builds:
  - `gradle.properties`: `ksp.incremental=false`
- Android/Compose setup requires Java 17.

## Running the App
- From Android Studio, run the `app` configuration.
- The app shows a simple Notes list with navigation to an Add Note screen using Hilt-provided dependencies.