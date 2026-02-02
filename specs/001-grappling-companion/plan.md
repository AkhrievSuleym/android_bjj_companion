# Implementation Plan: Grappling Companion

**Branch**: `001-grappling-companion` | **Date**: 2026-01-28 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-grappling-companion/spec.md`

## Summary

Mobile application for grappling athletes (BJJ, wrestling, grappling) that unifies nutrition control, weight tracking, and training diary. The app targets amateur and professional athletes preparing for competition, with primary focus on weight class management. Technical approach uses modern Android development stack with Clean Architecture, Jetpack Compose for UI, Room for local data persistence, and external USDA FoodData Central API for nutrition database integration.

## Technical Context

**Language/Version**: Kotlin (latest stable for Android)
**Primary Dependencies**: Jetpack Compose, Hilt, Room, Retrofit, Coroutines/Flow, Navigation Compose
**Storage**: Room (SQLite) for local persistence, DataStore for preferences
**Testing**: JUnit, Espresso for UI tests, MockK for mocking
**Target Platform**: Android (API 26+, Android 8.0 Oreo and above)
**Project Type**: Mobile (Android single module app)
**Performance Goals**: 60 fps UI rendering, <500ms screen transitions, <2s API responses, smooth graph animations
**Constraints**: Offline-capable (all features except food search), <100MB app size, no cloud sync in MVP
**Scale/Scope**: Single-user app, ~8 main screens, local-only data storage, ~50-100 Compose components

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### ✅ I. Clean Architecture & MVVM
- **Compliant**: Project structure follows Clean Architecture with `data`, `domain`, `presentation` layers
- **Compliant**: Dependencies flow inward (presentation → domain ← data)
- **Compliant**: Domain layer contains only interfaces, models, and use cases (no Android SDK dependencies)
- **Compliant**: Each screen has dedicated ViewModel with StateFlow for UI state
- **Compliant**: Use cases perform single operations

### ✅ II. Technology Stack (NON-NEGOTIABLE)
- **Compliant**: Kotlin only (no Java)
- **Compliant**: Jetpack Compose (no XML layouts)
- **Compliant**: Hilt for DI
- **Compliant**: Room for database
- **Compliant**: Retrofit + OkHttp + Kotlin Serialization for network
- **Compliant**: Coroutines + Flow for async operations
- **Compliant**: Jetpack Navigation Compose for navigation
- **Compliant**: No RxJava, no LiveData, minimal third-party libraries (Vico for charts, Coil for images)

### ✅ III. Code Quality Standards (NON-NEGOTIABLE)
- **Compliant**: Naming conventions defined (PascalCase classes, camelCase functions, SCREAMING_SNAKE_CASE constants)
- **Compliant**: File structure follows standards (Entity suffix, UseCase suffix, Dto/Response suffix)
- **Compliant**: One class per file (except sealed classes)
- **Compliant**: Max 30 lines per function enforced during code review
- **Compliant**: Null safety practices (avoid `!!`, use safe operators)

### ✅ IV. UI/UX Standards
- **Compliant**: Material Design 3 components only
- **Compliant**: Light and dark theme support (via DataStore preference)
- **Compliant**: Stateless composables with state hoisting
- **Compliant**: Preview annotations for all composable components
- **Compliant**: Animations use `animateXAsState` and `AnimatedVisibility`

### ✅ V. Data Management & Repository Pattern
- **Compliant**: All Room operations are suspend functions or return Flow
- **Compliant**: No `allowMainThreadQueries()`
- **Compliant**: Indexes on frequently queried fields (date, user_id)
- **Compliant**: All network requests are suspend functions
- **Compliant**: Network error handling with user-friendly messages and retry
- **Compliant**: API key stored in `local.properties` (not in code)
- **Compliant**: Repository pattern as single data access point
- **Compliant**: Repositories return domain models (not Entity or DTO)

### ✅ Quality Assurance & Best Practices
- **Compliant**: Error handling uses sealed class Result<Success, Error>
- **Compliant**: Security: API key not committed, user input validated
- **Compliant**: Performance: Heavy operations on IO dispatcher, main thread never blocked
- **Compliant**: Git workflow: T0XX commit format, feature branch strategy

### ✅ Development Workflow
- **Compliant**: Commit format `T0XX: Description`
- **Compliant**: Branch format `feature/TXXX-short-description`
- **Compliant**: KDoc for public APIs, complex logic documented

**Constitution Check Result**: ✅ **PASSED** - All principles satisfied, no violations to justify

## Project Structure

### Documentation (this feature)

```text
specs/001-grappling-companion/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
│   └── usda-api.md     # USDA FoodData Central API contract
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
app/
├── src/main/java/com/example/grapplingcompanion/
│   ├── GrapplingApp.kt                 # Application class (Hilt setup)
│   ├── MainActivity.kt                 # Single activity (Compose)
│   │
│   ├── data/
│   │   ├── local/
│   │   │   ├── database/
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   ├── dao/
│   │   │   │   │   ├── UserProfileDao.kt
│   │   │   │   │   ├── WeightEntryDao.kt
│   │   │   │   │   ├── FoodLogDao.kt
│   │   │   │   │   ├── TrainingDao.kt
│   │   │   │   │   └── TechniqueDao.kt
│   │   │   │   └── entity/
│   │   │   │       ├── UserProfileEntity.kt
│   │   │   │       ├── WeightEntryEntity.kt
│   │   │   │       ├── FoodLogEntity.kt
│   │   │   │       ├── TrainingEntity.kt
│   │   │   │       └── TechniqueEntity.kt
│   │   │   └── datastore/
│   │   │       └── SettingsDataStore.kt
│   │   │
│   │   ├── remote/
│   │   │   ├── api/
│   │   │   │   └── UsdaFoodApi.kt
│   │   │   ├── dto/
│   │   │   │   ├── FoodSearchResponse.kt
│   │   │   │   └── FoodNutrientDto.kt
│   │   │   └── NetworkModule.kt
│   │   │
│   │   └── repository/
│   │       ├── UserRepositoryImpl.kt
│   │       ├── NutritionRepositoryImpl.kt
│   │       ├── TrainingRepositoryImpl.kt
│   │       └── TechniqueRepositoryImpl.kt
│   │
│   ├── domain/
│   │   ├── model/
│   │   │   ├── UserProfile.kt
│   │   │   ├── WeightEntry.kt
│   │   │   ├── Food.kt
│   │   │   ├── FoodLog.kt
│   │   │   ├── Training.kt
│   │   │   ├── TrainingType.kt           # Enum: GRAPPLING, SPARRING, STRENGTH, CARDIO
│   │   │   ├── Technique.kt
│   │   │   └── TechniqueCategory.kt      # Enum: TAKEDOWN, SUBMISSION, SWEEP, DEFENSE, CONTROL
│   │   │
│   │   ├── repository/
│   │   │   ├── UserRepository.kt
│   │   │   ├── NutritionRepository.kt
│   │   │   ├── TrainingRepository.kt
│   │   │   └── TechniqueRepository.kt
│   │   │
│   │   └── usecase/
│   │       ├── user/
│   │       │   ├── GetUserProfileUseCase.kt
│   │       │   ├── SaveUserProfileUseCase.kt
│   │       │   └── CalculateTdeeUseCase.kt
│   │       ├── weight/
│   │       │   ├── AddWeightEntryUseCase.kt
│   │       │   └── GetWeightHistoryUseCase.kt
│   │       ├── nutrition/
│   │       │   ├── SearchFoodUseCase.kt
│   │       │   ├── AddFoodLogUseCase.kt
│   │       │   └── GetDailyNutritionUseCase.kt
│   │       ├── training/
│   │       │   ├── AddTrainingUseCase.kt
│   │       │   ├── GetTrainingsForMonthUseCase.kt
│   │       │   └── GetUpcomingTrainingUseCase.kt
│   │       └── technique/
│   │           ├── AddTechniqueUseCase.kt
│   │           └── GetTechniquesUseCase.kt
│   │
│   ├── presentation/
│   │   ├── navigation/
│   │   │   ├── NavGraph.kt
│   │   │   └── Screen.kt
│   │   │
│   │   ├── theme/
│   │   │   ├── Color.kt
│   │   │   ├── Theme.kt
│   │   │   └── Type.kt
│   │   │
│   │   ├── components/
│   │   │   ├── BottomNavBar.kt
│   │   │   ├── StatCard.kt
│   │   │   ├── ProgressBar.kt
│   │   │   ├── WeightChart.kt
│   │   │   └── LoadingIndicator.kt
│   │   │
│   │   ├── onboarding/
│   │   │   ├── OnboardingScreen.kt
│   │   │   └── OnboardingViewModel.kt
│   │   │
│   │   ├── dashboard/
│   │   │   ├── DashboardScreen.kt
│   │   │   └── DashboardViewModel.kt
│   │   │
│   │   ├── nutrition/
│   │   │   ├── NutritionScreen.kt
│   │   │   ├── NutritionViewModel.kt
│   │   │   ├── FoodSearchScreen.kt
│   │   │   └── FoodSearchViewModel.kt
│   │   │
│   │   ├── training/
│   │   │   ├── TrainingCalendarScreen.kt
│   │   │   ├── TrainingCalendarViewModel.kt
│   │   │   ├── AddTrainingScreen.kt
│   │   │   └── AddTrainingViewModel.kt
│   │   │
│   │   ├── techniques/
│   │   │   ├── TechniquesScreen.kt
│   │   │   ├── TechniquesViewModel.kt
│   │   │   └── AddTechniqueScreen.kt
│   │   │
│   │   ├── progress/
│   │   │   ├── ProgressScreen.kt
│   │   │   └── ProgressViewModel.kt
│   │   │
│   │   └── settings/
│   │       ├── SettingsScreen.kt
│   │       └── SettingsViewModel.kt
│   │
│   └── di/
│       ├── AppModule.kt
│       ├── DatabaseModule.kt
│       ├── NetworkModule.kt
│       └── RepositoryModule.kt
│
├── src/main/res/
│   └── values/
│       └── strings.xml
│
├── build.gradle.kts
└── local.properties                    # API keys (not committed, .gitignore)
```

**Structure Decision**: Android single-module app structure following Clean Architecture. The `app/` module contains all three layers (data, domain, presentation) with clear separation. This structure is appropriate for MVP scope and can be modularized later if needed. Source code lives in standard Android project location (`app/src/main/java/`). Tests will be in `app/src/test/` (unit) and `app/src/androidTest/` (instrumented).

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

No violations detected. All architectural decisions align with constitution principles.
