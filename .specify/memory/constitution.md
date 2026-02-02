<!--
Sync Impact Report:
- Version: Initial (none) → 1.0.0
- Rationale: Initial constitution creation from project constitution.md
- Modified Principles: N/A (initial creation)
- Added Sections: All 5 core principles + 2 supplementary sections + governance
- Removed Sections: None
- Templates Status:
  ✅ plan-template.md - reviewed, Constitution Check section compatible
  ✅ spec-template.md - reviewed, requirements align with principles
  ✅ tasks-template.md - reviewed, task organization supports constitution
- Follow-up TODOs: None
-->


# Grappling Companion Constitution

## Core Principles

### I. Clean Architecture & MVVM

**Architecture MUST follow Clean Architecture with strict layer separation:**
- Project MUST separate into three layers: `data`, `domain`, `presentation`
- Dependencies MUST flow inward: presentation → domain ← data
- Domain layer MUST NOT depend on Android SDK, Room, Retrofit, or external libraries
- Use Cases MUST perform only one operation each

**MVVM pattern is mandatory:**
- Each screen MUST have its own ViewModel
- ViewModel MUST NOT contain references to View or Context
- UI state MUST be transmitted through StateFlow
- Events MUST be handled through sealed classes

**Rationale**: Clean Architecture ensures testability, maintainability, and independence from frameworks. MVVM provides clear separation of concerns and reactive UI patterns essential for modern Android development.

### II. Technology Stack (NON-NEGOTIABLE)

**Mandatory technologies:**
- **Language**: Kotlin (no Java allowed)
- **UI**: Jetpack Compose (no XML layouts)
- **DI**: Hilt
- **Database**: Room
- **Network**: Retrofit + OkHttp + Kotlin Serialization
- **Async**: Coroutines + Flow
- **Navigation**: Jetpack Navigation Compose

**Prohibited approaches:**
- DO NOT use RxJava
- DO NOT use Dagger without Hilt
- DO NOT use XML layouts
- DO NOT use LiveData (only StateFlow/Flow)
- DO NOT use third-party libraries without critical necessity

**Rationale**: This stack represents modern Android best practices and ensures consistency, maintainability, and team productivity. Prohibitions prevent technical debt and fragmentation.

### III. Code Quality Standards (NON-NEGOTIABLE)

**Naming conventions:**
- Classes: PascalCase (`UserProfileEntity`, `DashboardViewModel`)
- Functions and variables: camelCase (`getUserProfile`, `currentWeight`)
- Constants: SCREAMING_SNAKE_CASE (`BASE_URL`, `API_KEY`)
- Packages: lowercase (`data.repository`, `domain.usecase`)

**File structure:**
- One class = one file (exception: sealed classes with subclasses)
- Entity classes MUST have suffix `Entity`
- DTO classes MUST have suffix `Dto` or `Response`
- Use Cases MUST have suffix `UseCase`

**Function requirements:**
- Maximum function length: 30 lines
- Function MUST do one thing only
- Avoid nesting deeper than 3 levels

**Null safety:**
- Use nullable types only when truly necessary
- Avoid `!!` operator
- Use `?.let`, `?:`, `?.run` for safe null handling

**Rationale**: Consistent code quality standards reduce cognitive load, improve code review efficiency, and prevent common bugs. Null safety requirements prevent runtime crashes.

### IV. UI/UX Standards

**Material Design 3 compliance:**
- Use only Material 3 components
- Follow Material Design guidelines
- Support both light and dark themes

**Compose best practices:**
- Composable functions MUST be stateless where possible
- State hoisting: state is lifted up
- Preview annotations required for components
- Avoid side effects in composition

**Animation requirements:**
- Use `animateXAsState` for simple animations
- Use `AnimatedVisibility` for show/hide transitions
- Animations MUST be smooth (not block UI thread)

**Rationale**: Material Design 3 ensures modern, accessible UI. Compose best practices prevent recomposition issues and performance problems. Smooth animations are essential for professional user experience.

### V. Data Management & Repository Pattern

**Room database:**
- All database operations MUST be suspend or return Flow
- DO NOT use allowMainThreadQueries()
- Use indexes for frequently queried fields

**Network requirements:**
- All network requests MUST be suspend functions
- Handle network errors (show user-friendly messages)
- Use timeout for requests
- API keys MUST NOT be stored in code (use BuildConfig or local.properties)

**Repository pattern (mandatory):**
- Repository MUST be the single point of access to data
- Repository decides data source (cache/network/database)
- Repository returns domain models, not Entity or DTO

**Rationale**: Proper data management prevents main thread blocking, ensures smooth UX, and maintains security. Repository pattern provides abstraction and testability.

## Quality Assurance & Best Practices

### Error Handling

- DO NOT ignore exceptions (empty catch blocks forbidden)
- Use sealed class for Result types (Success/Error)
- Show user-friendly error messages
- Log errors for debugging purposes

### Security

- API keys MUST NOT be committed to repository
- Use local.properties or BuildConfig for secrets
- Validate all user input

### Performance

- Avoid unnecessary recompositions in Compose
- Use `remember` and `derivedStateOf` appropriately
- Move heavy operations to IO dispatcher
- Never block the main thread

**Rationale**: Quality assurance principles prevent production issues, security vulnerabilities, and poor user experience. Performance standards ensure the app remains responsive.

## Development Workflow

### Git Workflow

**Commit standards:**
- One commit = one task
- Format: `T0XX: Brief description`
- Example: `T005: Setup navigation with bottom bar`

**Branch strategy:**
- Development in feature branches
- Format: `feature/TXXX-short-description`

### Documentation Requirements

- Public API methods MUST have KDoc comments
- README MUST contain setup instructions
- Complex business logic MUST be documented

**Rationale**: Consistent Git workflow enables clear project history and team collaboration. Documentation ensures knowledge transfer and onboarding efficiency.

## Governance

**Constitution authority:**
- This constitution supersedes all other development practices
- All code reviews MUST verify compliance with these principles
- Complexity or deviations MUST be explicitly justified

**Amendment process:**
- Amendments require documentation of rationale and impact
- Version must be incremented following semantic versioning
- All dependent templates and documentation must be updated
- Migration plan required for breaking changes

**Compliance verification:**
- Every feature plan MUST include Constitution Check section
- Pull requests MUST reference applicable principles
- Code reviews MUST flag violations
- Team retrospectives MUST address systematic violations

**Version**: 1.0.0 | **Ratified**: 2026-01-28 | **Last Amended**: 2026-01-28
