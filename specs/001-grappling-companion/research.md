# Research: Grappling Companion

**Feature**: Grappling Companion
**Date**: 2026-01-28
**Phase**: 0 (Outline & Research)

## Purpose

This document consolidates research findings and technical decisions for the Grappling Companion mobile app. All unknowns from the Technical Context have been resolved through evaluation of modern Android development best practices and domain-specific requirements for fitness/nutrition tracking applications.

## Technical Decisions

### 1. Language & Platform

**Decision**: Kotlin for Android (API 26+, Android 8.0 Oreo and above)

**Rationale**:
- Kotlin is the official language for Android development (Google-backed)
- Modern, concise syntax reduces boilerplate compared to Java
- Excellent coroutines support for asynchronous operations
- Null safety built into the language prevents common crashes
- API 26+ covers 98%+ of active Android devices as of 2024-2025

**Alternatives Considered**:
- **Java**: Rejected due to verbose syntax, no modern language features, deprecated by Google for new Android projects
- **Flutter/React Native**: Rejected because project already targets Android-only MVP, native Android provides better Material Design 3 support and performance
- **API 21+ (Lollipop)**: Rejected because supporting older APIs requires significant compatibility code; API 26+ is modern baseline

### 2. UI Framework

**Decision**: Jetpack Compose with Material Design 3

**Rationale**:
- Declarative UI framework (similar to Flutter, React, SwiftUI) - modern industry standard
- Less boilerplate code than XML layouts
- Built-in animation support with simple APIs
- Material Design 3 components out-of-the-box (light/dark theme support)
- Better performance for complex UIs (weight graphs, progress animations)
- Strong adoption in Android community (2024+)

**Alternatives Considered**:
- **XML Layouts + View Binding**: Rejected due to imperative nature, more boilerplate, harder to maintain, deprecated approach
- **Flutter**: Rejected because Compose is native Android solution with better platform integration

### 3. Architecture Pattern

**Decision**: Clean Architecture + MVVM

**Rationale**:
- **Clean Architecture**: Separates concerns into layers (data, domain, presentation) for testability and maintainability
- **MVVM**: Standard Android pattern with ViewModel lifecycle awareness
- Domain layer remains framework-independent (no Android SDK dependencies)
- Easy to test business logic in isolation
- Scalable for future features
- Aligns with constitution requirements

**Layers**:
- **Data**: Room database, Retrofit API, DataStore preferences, repository implementations
- **Domain**: Business models, repository interfaces, use cases (single-responsibility operations)
- **Presentation**: Jetpack Compose UI, ViewModels with StateFlow

**Alternatives Considered**:
- **MVI (Model-View-Intent)**: Rejected as more complex than needed for MVP; MVVM is sufficient
- **Repository pattern only**: Rejected because use cases provide better separation of business logic and make code more testable

### 4. Dependency Injection

**Decision**: Hilt (Dagger 2 for Android)

**Rationale**:
- Official DI solution recommended by Google for Android
- Built on Dagger 2 but with reduced boilerplate specific to Android
- Excellent ViewModel integration
- Compile-time safety (errors caught at build time, not runtime)
- Scoping support (Singleton, ViewModelScoped, ActivityScoped)

**Alternatives Considered**:
- **Koin**: Rejected because runtime resolution (not compile-time safe), slower performance
- **Manual DI**: Rejected due to boilerplate and error-prone nature
- **Dagger 2 directly**: Rejected because Hilt is easier to use and Android-optimized

### 5. Database

**Decision**: Room (SQLite ORM)

**Rationale**:
- Official Android library from Google (AndroidX)
- Compile-time SQL query verification
- Native support for Kotlin Coroutines and Flow
- Type-safe database operations
- Automatic migrations support
- Offline-first capability (critical for MVP requirement)

**Schema Highlights**:
- 5 main tables: user_profile, weight_entries, food_logs, trainings, techniques
- Indexes on date fields for efficient querying
- Foreign key relationships where applicable
- Timestamp-based date storage (Long type for Room)

**Alternatives Considered**:
- **SQLite directly**: Rejected due to lack of type safety, manual query writing, no coroutine support
- **Realm**: Rejected because Room is official AndroidX library with better tooling and documentation
- **ObjectBox**: Rejected due to smaller community, less mature compared to Room

### 6. Networking

**Decision**: Retrofit 2 + OkHttp + Kotlin Serialization

**Rationale**:
- **Retrofit 2**: Industry standard HTTP client for Android, type-safe API calls
- **OkHttp**: Underlying HTTP client with connection pooling, interceptors (logging, timeouts)
- **Kotlin Serialization**: Official Kotlin JSON serialization, faster than Gson/Moshi, compile-time safe

**API Integration**:
- USDA FoodData Central API for food nutrition database
- Free tier: 1000 requests/hour (sufficient for MVP single-user app)
- RESTful JSON API with comprehensive nutrition data

**Alternatives Considered**:
- **Ktor**: Rejected because Retrofit has better Android ecosystem support and more examples
- **Gson/Moshi**: Rejected because Kotlin Serialization is modern, faster, and compile-time safe
- **Volley**: Rejected as deprecated and less feature-rich than Retrofit

### 7. Asynchronous Operations

**Decision**: Kotlin Coroutines + Flow

**Rationale**:
- **Coroutines**: Lightweight threading, perfect for Android (database, network operations)
- **Flow**: Reactive streams for observing data changes (Room Flow integration)
- Native Kotlin support, no third-party dependencies
- Easy to read and maintain compared to callbacks or RxJava
- Excellent structured concurrency (scopes, cancellation)

**Dispatchers**:
- `Dispatchers.IO`: Database and network operations
- `Dispatchers.Main`: UI updates
- `Dispatchers.Default`: CPU-intensive work (calculations)

**Alternatives Considered**:
- **RxJava**: Rejected per constitution (prohibited), steep learning curve, overkill for simple async operations
- **Callbacks**: Rejected due to callback hell, hard to maintain
- **LiveData**: Rejected per constitution (prohibited), Flow is more powerful

### 8. Navigation

**Decision**: Jetpack Navigation Compose

**Rationale**:
- Official navigation library for Compose
- Type-safe navigation arguments
- Deep linking support
- Back stack management
- Transition animations built-in
- Single Activity architecture (modern Android pattern)

**Alternatives Considered**:
- **Manual navigation with state**: Rejected due to complexity and lack of back stack management
- **Decompose**: Rejected because Jetpack Navigation is official solution with better documentation

### 9. State Management

**Decision**: StateFlow in ViewModels

**Rationale**:
- StateFlow is hot stream (always has value), perfect for UI state
- Lifecycle-aware collection in Compose (`collectAsStateWithLifecycle()`)
- Thread-safe state updates
- Compatible with Compose recomposition
- Aligns with constitution (no LiveData)

**Pattern**:
```kotlin
// ViewModel
private val _uiState = MutableStateFlow(UiState())
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

// Composable
val state by viewModel.uiState.collectAsStateWithLifecycle()
```

**Alternatives Considered**:
- **LiveData**: Rejected per constitution
- **SharedFlow**: Rejected because StateFlow is simpler for state management (events use SharedFlow)
- **Compose State**: Rejected because ViewModel provides lifecycle awareness and survives configuration changes

### 10. Local Preferences

**Decision**: DataStore (Preferences DataStore)

**Rationale**:
- Modern replacement for SharedPreferences
- Coroutine-based API (non-blocking)
- Type-safe with Kotlin Flow
- Transactional updates (atomic writes)
- Use case: Theme preference (light/dark mode)

**Alternatives Considered**:
- **SharedPreferences**: Rejected because synchronous (blocks main thread), not coroutine-friendly
- **Proto DataStore**: Rejected as overkill for simple key-value preferences

### 11. Image Loading

**Decision**: Coil (Compose)

**Rationale**:
- Built for Compose (native AsyncImage composable)
- Kotlin-first, coroutine-based
- Small library size
- Good caching strategy
- Use case: Potential future feature for meal photos (not MVP but good foundation)

**Alternatives Considered**:
- **Glide**: Rejected because Coil has better Compose integration
- **Picasso**: Rejected as less actively maintained

### 12. Charts & Graphs

**Decision**: Vico (Compose Material 3 charts)

**Rationale**:
- Material Design 3 styled charts
- Compose-native (declarative API)
- Line charts for weight tracking graphs
- Customizable and performant
- Active development

**Use Cases**:
- Weight progress line chart (week/month/3 months views)
- Macro nutrition progress bars on dashboard

**Alternatives Considered**:
- **MPAndroidChart**: Rejected because it's View-based (not Compose-native), requires wrappers
- **Custom Canvas drawing**: Rejected due to development time, Vico provides production-ready solution

### 13. Testing Strategy

**Decision**: JUnit 5 + MockK + Espresso + Compose UI Testing

**Rationale**:
- **JUnit 5**: Modern testing framework for unit tests
- **MockK**: Kotlin-friendly mocking library (better than Mockito for Kotlin)
- **Espresso**: Android instrumented tests (database, integration)
- **Compose UI Testing**: Official Compose testing library (`@Test` with `composeTestRule`)

**Test Layers**:
- **Unit Tests**: Domain use cases, ViewModels (no Android dependencies)
- **Integration Tests**: Repository implementations with Room database
- **UI Tests**: Compose screens with user interactions

**Alternatives Considered**:
- **Mockito**: Rejected because MockK is more idiomatic for Kotlin
- **Robolectric**: Rejected because not needed with modern Compose testing tools

### 14. Build System

**Decision**: Gradle with Kotlin DSL (build.gradle.kts)

**Rationale**:
- Kotlin DSL provides type-safe build configuration
- Better IDE support (autocomplete, navigation)
- Modern Android standard
- Version catalogs for dependency management

**Alternatives Considered**:
- **Groovy DSL**: Rejected because Kotlin DSL is more maintainable and type-safe

### 15. API Key Management

**Decision**: local.properties + BuildConfig

**Rationale**:
- `local.properties` is in `.gitignore` (not committed to repo)
- API key read at build time and injected into `BuildConfig`
- Secure: Keys never in version control
- Easy developer onboarding (create local.properties file)

**Implementation**:
```kotlin
// local.properties
USDA_API_KEY=your_api_key_here

// build.gradle.kts
buildConfigField("String", "USDA_API_KEY", "\"${project.properties["USDA_API_KEY"]}\"")

// Usage in code
val apiKey = BuildConfig.USDA_API_KEY
```

**Alternatives Considered**:
- **Hardcoded**: Rejected due to security risk
- **Environment variables**: Rejected because harder for team collaboration
- **Secrets Gradle Plugin**: Rejected as unnecessary for simple API key storage

## Nutrition API Research

### USDA FoodData Central API

**Base URL**: `https://api.nal.usda.gov/fdc/v1/`

**Authentication**: API key in query parameter (`api_key=...`)

**Free Tier Limits**:
- 1000 requests per hour
- Sufficient for single-user app (typical user: 10-20 food searches per day)

**Key Endpoints**:
- `GET /foods/search`: Search foods by name, returns nutrition data
- Query params: `query`, `pageSize` (default 25)

**Nutrient IDs**:
- 1008: Energy (KCAL)
- 1003: Protein (G)
- 1004: Total lipid/fat (G)
- 1005: Carbohydrate (G)

**Response Structure**:
```json
{
  "foods": [
    {
      "fdcId": 123456,
      "description": "Chicken breast, raw",
      "foodNutrients": [
        {"nutrientId": 1008, "nutrientName": "Energy", "value": 120, "unitName": "KCAL"},
        {"nutrientId": 1003, "nutrientName": "Protein", "value": 22.5, "unitName": "G"}
      ]
    }
  ]
}
```

**Caching Strategy**:
- Cache search results locally in Room database
- TTL: 7 days (nutrition data rarely changes)
- Reduces API calls, enables offline access to previously searched foods

## TDEE Calculation Research

**Decision**: Mifflin-St Jeor Equation

**Formula** (for weight loss scenario):
```
BMR (Basal Metabolic Rate) = 10 × weight(kg) + 6.25 × height(cm) - 5 × age + 5 (male) or -161 (female)
TDEE = BMR × Activity Factor
```

**Simplified for MVP** (no height/age/gender in profile):
- Use rough estimate: `TDEE ≈ current_weight × 30` (moderate activity level)
- This gives ballpark daily calorie target
- Future enhancement: Add height, age, gender, activity level for accurate calculation

**Rationale**:
- MVP focuses on weight tracking and awareness
- Simplified calculation reduces onboarding friction
- Users can manually adjust if needed
- Sufficient accuracy for competition weight management

## Performance Considerations

### Target Metrics (from spec success criteria)
- 60 fps UI rendering (Compose default)
- <500ms screen transitions (Compose Navigation default)
- <2s API responses (USDA API typically <500ms)
- <1s dashboard load (Room Flow + StateFlow)

### Optimization Strategies
- **Lazy Loading**: LazyColumn for lists (techniques, training history)
- **Image Optimization**: Coil caching (future meal photos)
- **Database Indexing**: Date fields indexed for fast queries
- **Coroutine Dispatchers**: IO for database/network, Main for UI
- **Remember & DerivedStateOf**: Prevent unnecessary Compose recomposition

## Dependency Versions

Based on latest stable releases as of January 2025:

```kotlin
// Compose BOM (Bill of Materials - ensures compatible versions)
androidx.compose:compose-bom:2024.02.00

// Core
androidx.core:core-ktx:1.12.0
androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0
androidx.activity:activity-compose:1.8.2

// Navigation
androidx.navigation:navigation-compose:2.7.7

// Hilt
com.google.dagger:hilt-android:2.50
androidx.hilt:hilt-navigation-compose:1.1.0

// Room
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Retrofit + OkHttp
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.okhttp3:okhttp:4.12.0
com.squareup.okhttp3:logging-interceptor:4.12.0

// Kotlin Serialization
org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2
com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// DataStore
androidx.datastore:datastore-preferences:1.0.0

// Coil (images)
io.coil-kt:coil-compose:2.5.0

// Vico (charts)
com.patrykandpatrick.vico:compose-m3:1.13.1
```

## Risks & Mitigations

| Risk | Impact | Mitigation |
|------|--------|------------|
| USDA API rate limit exceeded | Food search fails | Cache all searches locally, show cached results first |
| USDA API downtime | Food search unavailable | Offline mode with saved/favorite foods, graceful error messages |
| Large database size | App storage bloat | Implement data cleanup (old food logs >6 months), user-initiated |
| Complex animations hurt performance | UI lag | Use Compose profiling tools, simplify animations if needed |
| Room migration errors | Data loss on updates | Test migrations thoroughly, export backup in settings |

## Next Steps

Phase 0 research complete. All technical unknowns resolved. Ready to proceed to:
- **Phase 1**: Data model design (data-model.md)
- **Phase 1**: API contracts documentation (contracts/usda-api.md)
- **Phase 1**: Developer quickstart guide (quickstart.md)
