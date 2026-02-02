# Tasks: Grappling Companion

**Input**: Design documents from `/specs/001-grappling-companion/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests are OPTIONAL - not included in this plan unless explicitly requested

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Android app**: `app/src/main/java/com/example/grapplingcompanion/`
- Paths shown below use absolute structure from project root

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Create Android project with Jetpack Compose in Android Studio (min SDK 26, compile SDK 34)
- [ ] T002 Configure build.gradle.kts with Kotlin, Compose BOM, and plugin versions
- [ ] T003 [P] Add Hilt dependencies to app/build.gradle.kts (hilt-android:2.50, hilt-compiler, hilt-navigation-compose)
- [ ] T004 [P] Add Room dependencies to app/build.gradle.kts (room-runtime:2.6.1, room-ktx, room-compiler)
- [ ] T005 [P] Add Retrofit dependencies to app/build.gradle.kts (retrofit:2.9.0, okhttp:4.12.0, kotlinx-serialization-json, logging-interceptor)
- [ ] T006 [P] Add Navigation Compose dependency to app/build.gradle.kts (navigation-compose:2.7.7)
- [ ] T007 [P] Add Vico charts dependency to app/build.gradle.kts (vico:compose-m3:1.13.1)
- [ ] T008 [P] Add Coil dependency to app/build.gradle.kts (coil-compose:2.5.0)
- [ ] T009 [P] Add DataStore dependency to app/build.gradle.kts (datastore-preferences:1.0.0)
- [ ] T010 [P] Add Coroutines dependency to app/build.gradle.kts (kotlinx-coroutines-android:1.7.3)
- [ ] T011 Create local.properties file with USDA_API_KEY placeholder
- [ ] T012 Configure BuildConfig field for USDA_API_KEY in app/build.gradle.kts
- [ ] T013 Sync Gradle and verify all dependencies resolve correctly

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [ ] T014 Create GrapplingApp.kt application class with @HiltAndroidApp annotation in app/src/main/java/com/example/grapplingcompanion/
- [ ] T015 Update AndroidManifest.xml to reference GrapplingApp as application name
- [ ] T016 Add @AndroidEntryPoint annotation to MainActivity.kt
- [ ] T017 [P] Create Color.kt in app/src/main/java/com/example/grapplingcompanion/presentation/theme/ with Material3 color schemes (light and dark)
- [ ] T018 [P] Create Type.kt in app/src/main/java/com/example/grapplingcompanion/presentation/theme/ with Material3 typography
- [ ] T019 Create Theme.kt in app/src/main/java/com/example/grapplingcompanion/presentation/theme/ with Material3 theme composable supporting light/dark modes
- [ ] T020 Create Screen.kt sealed class in app/src/main/java/com/example/grapplingcompanion/presentation/navigation/ defining all app screens (Onboarding, Dashboard, Nutrition, Training, Techniques, Progress, Settings)
- [ ] T021 Create NavGraph.kt in app/src/main/java/com/example/grapplingcompanion/presentation/navigation/ with NavHost setup
- [ ] T022 Create BottomNavBar.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/components/
- [ ] T023 Update MainActivity.kt to set up NavHost with BottomNavBar and theme
- [ ] T024 [P] Create UserProfileEntity.kt in app/src/main/java/com/example/grapplingcompanion/data/local/database/entity/ with Room annotations
- [ ] T025 [P] Create WeightEntryEntity.kt in app/src/main/java/com/example/grapplingcompanion/data/local/database/entity/ with Room annotations and date index
- [ ] T026 [P] Create FoodLogEntity.kt in app/src/main/java/com/example/grapplingcompanion/data/local/database/entity/ with Room annotations and date index
- [ ] T027 [P] Create TrainingEntity.kt in app/src/main/java/com/example/grapplingcompanion/data/local/database/entity/ with Room annotations and date index
- [ ] T028 [P] Create TechniqueEntity.kt in app/src/main/java/com/example/grapplingcompanion/data/local/database/entity/ with Room annotations and created_at index
- [ ] T029 [P] Create UserProfileDao.kt interface in app/src/main/java/com/example/grapplingcompanion/data/local/database/dao/ with CRUD operations returning Flow
- [ ] T030 [P] Create WeightEntryDao.kt interface in app/src/main/java/com/example/grapplingcompanion/data/local/database/dao/ with CRUD and date range query operations
- [ ] T031 [P] Create FoodLogDao.kt interface in app/src/main/java/com/example/grapplingcompanion/data/local/database/dao/ with CRUD and date/meal type query operations
- [ ] T032 [P] Create TrainingDao.kt interface in app/src/main/java/com/example/grapplingcompanion/data/local/database/dao/ with CRUD and month range query operations
- [ ] T033 [P] Create TechniqueDao.kt interface in app/src/main/java/com/example/grapplingcompanion/data/local/database/dao/ with CRUD and category filter operations
- [ ] T034 Create AppDatabase.kt abstract class in app/src/main/java/com/example/grapplingcompanion/data/local/database/ annotated with @Database including all entities
- [ ] T035 Create DatabaseModule.kt in app/src/main/java/com/example/grapplingcompanion/di/ providing AppDatabase and all DAOs via Hilt
- [ ] T036 [P] Create UserProfile.kt domain model in app/src/main/java/com/example/grapplingcompanion/domain/model/
- [ ] T037 [P] Create WeightEntry.kt domain model in app/src/main/java/com/example/grapplingcompanion/domain/model/
- [ ] T038 [P] Create Food.kt domain model in app/src/main/java/com/example/grapplingcompanion/domain/model/
- [ ] T039 [P] Create FoodLog.kt domain model in app/src/main/java/com/example/grapplingcompanion/domain/model/
- [ ] T040 [P] Create Training.kt domain model in app/src/main/java/com/example/grapplingcompanion/domain/model/
- [ ] T041 [P] Create TrainingType.kt enum in app/src/main/java/com/example/grapplingcompanion/domain/model/ (GRAPPLING, SPARRING, STRENGTH, CARDIO)
- [ ] T042 [P] Create Technique.kt domain model in app/src/main/java/com/example/grapplingcompanion/domain/model/
- [ ] T043 [P] Create TechniqueCategory.kt enum in app/src/main/java/com/example/grapplingcompanion/domain/model/ (TAKEDOWN, SUBMISSION, SWEEP, DEFENSE, CONTROL)
- [ ] T044 [P] Create UserRepository.kt interface in app/src/main/java/com/example/grapplingcompanion/domain/repository/
- [ ] T045 [P] Create NutritionRepository.kt interface in app/src/main/java/com/example/grapplingcompanion/domain/repository/
- [ ] T046 [P] Create TrainingRepository.kt interface in app/src/main/java/com/example/grapplingcompanion/domain/repository/
- [ ] T047 [P] Create TechniqueRepository.kt interface in app/src/main/java/com/example/grapplingcompanion/domain/repository/
- [ ] T048 Create UserRepositoryImpl.kt in app/src/main/java/com/example/grapplingcompanion/data/repository/ implementing UserRepository with entity-to-domain mapping
- [ ] T049 Create TrainingRepositoryImpl.kt in app/src/main/java/com/example/grapplingcompanion/data/repository/ implementing TrainingRepository with entity-to-domain mapping
- [ ] T050 Create TechniqueRepositoryImpl.kt in app/src/main/java/com/example/grapplingcompanion/data/repository/ implementing TechniqueRepository with entity-to-domain mapping
- [ ] T051 Create RepositoryModule.kt in app/src/main/java/com/example/grapplingcompanion/di/ providing all repository implementations via Hilt
- [ ] T052 [P] Create UsdaFoodApi.kt Retrofit interface in app/src/main/java/com/example/grapplingcompanion/data/remote/api/ with searchFoods suspend function
- [ ] T053 [P] Create FoodSearchResponse.kt DTO in app/src/main/java/com/example/grapplingcompanion/data/remote/dto/ with @Serializable annotation
- [ ] T054 [P] Create FoodNutrientDto.kt DTO in app/src/main/java/com/example/grapplingcompanion/data/remote/dto/ with @Serializable annotation
- [ ] T055 Create NetworkModule.kt in app/src/main/java/com/example/grapplingcompanion/di/ providing Retrofit, OkHttp client with logging interceptor, and UsdaFoodApi
- [ ] T056 Create NutritionRepositoryImpl.kt in app/src/main/java/com/example/grapplingcompanion/data/repository/ implementing NutritionRepository with API integration and DTO-to-domain mapping
- [ ] T057 Create SettingsDataStore.kt in app/src/main/java/com/example/grapplingcompanion/data/local/datastore/ for theme preference storage
- [ ] T058 Create AppModule.kt in app/src/main/java/com/example/grapplingcompanion/di/ providing Context and DataStore dependencies

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Onboarding & Profile Setup (Priority: P1) 🎯 MVP

**Goal**: Enable new users to complete profile setup and calculate TDEE for personalized tracking

**Independent Test**: Complete onboarding flow from app launch, enter profile data (name, current/target weight, weight class), verify TDEE calculation, confirm data persists, and check that returning users skip onboarding

### Implementation for User Story 1

- [ ] T059 [P] [US1] Create GetUserProfileUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/user/ returning Flow<UserProfile?>
- [ ] T060 [P] [US1] Create SaveUserProfileUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/user/ with input validation
- [ ] T061 [P] [US1] Create CalculateTdeeUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/user/ implementing TDEE formula (weight × 30 for MVP)
- [ ] T062 [US1] Create OnboardingUiState.kt data class in app/src/main/java/com/example/grapplingcompanion/presentation/onboarding/ with profile fields and validation errors
- [ ] T063 [US1] Create OnboardingViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/onboarding/ with StateFlow<OnboardingUiState>, input validation, and save logic using use cases
- [ ] T064 [US1] Create OnboardingScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/onboarding/ with 2-3 welcome slides, profile input form, TDEE display, and navigation to Dashboard
- [ ] T065 [US1] Add onboarding check logic to MainActivity.kt or NavGraph.kt to skip onboarding if profile exists
- [ ] T066 [US1] Test onboarding flow end-to-end: launch app → complete slides → enter valid data → verify TDEE calculation → confirm redirect to Dashboard → restart app → verify onboarding is skipped

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Weight Tracking (Priority: P1)

**Goal**: Enable users to log daily weight and view progress graph toward target weight

**Independent Test**: Add weight entries across multiple days, view weight graph with different time periods (week/month/3 months), verify progress calculations (kg remaining and percentage), test offline functionality

### Implementation for User Story 2

- [ ] T067 [P] [US2] Create AddWeightEntryUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/weight/ with date normalization and validation (no future dates, positive weight)
- [ ] T068 [P] [US2] Create GetWeightHistoryUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/weight/ returning Flow<List<WeightEntry>> for specified date range
- [ ] T069 [US2] Create WeightChart.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/components/ using Vico library for line chart with animations
- [ ] T070 [US2] Create ProgressUiState.kt data class in app/src/main/java/com/example/grapplingcompanion/presentation/progress/ with weight history, selected period, and progress calculations
- [ ] T071 [US2] Create ProgressViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/progress/ with StateFlow<ProgressUiState>, period selection logic, and progress calculations
- [ ] T072 [US2] Create ProgressScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/progress/ with WeightChart, period selector (week/month/3 months), progress display (kg and %), and "Add Weight" FAB
- [ ] T073 [US2] Create AddWeightDialog.kt or AddWeightBottomSheet.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/progress/ with weight input, date picker, and save button
- [ ] T074 [US2] Test weight tracking: add weight for today → verify saves → add weights for past 7 days → view week graph → switch to month view → verify graph updates → test progress calculation → verify offline functionality

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Dashboard Overview (Priority: P1)

**Goal**: Provide at-a-glance overview of weight, nutrition, and training with quick actions

**Independent Test**: Verify dashboard displays weight data, current day nutrition summary, upcoming training (if any), empty states when no data, and quick action buttons navigate correctly

### Implementation for User Story 3

- [ ] T075 [P] [US3] Create GetDailyNutritionUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/nutrition/ returning Flow with daily totals (calories, protein, fat, carbs)
- [ ] T076 [P] [US3] Create GetUpcomingTrainingUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/training/ returning Flow<Training?> for next training session
- [ ] T077 [US3] Create StatCard.kt reusable composable in app/src/main/java/com/example/grapplingcompanion/presentation/components/ for dashboard cards (weight, nutrition, training)
- [ ] T078 [US3] Create ProgressBar.kt reusable composable in app/src/main/java/com/example/grapplingcompanion/presentation/components/ for macro progress visualization
- [ ] T079 [US3] Create DashboardUiState.kt data class in app/src/main/java/com/example/grapplingcompanion/presentation/dashboard/ with weight, progress, nutrition, training, and loading states
- [ ] T080 [US3] Create DashboardViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/dashboard/ with StateFlow<DashboardUiState> combining data from all use cases
- [ ] T081 [US3] Create DashboardScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/dashboard/ with weight card, nutrition card, training card, mini weight graph, quick actions, and empty states
- [ ] T082 [US3] Test dashboard: verify weight card displays current weight and progress → log meal and verify nutrition card updates → add training and verify training card shows it → test quick action navigation → test empty states when no data

**Checkpoint**: All P1 user stories (MVP core) should now be independently functional

---

## Phase 6: User Story 4 - Nutrition Diary (Priority: P2)

**Goal**: Enable food search via API, meal logging with portions, and daily macro tracking

**Independent Test**: Search for foods (chicken, rice, etc.), add foods to different meals (breakfast/lunch/dinner/snack) with gram portions, view daily nutrition summary, save favorite foods, test offline mode with saved foods, edit/delete food logs

### Implementation for User Story 4

- [ ] T083 [P] [US4] Create SearchFoodUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/nutrition/ with debounce logic and caching
- [ ] T084 [P] [US4] Create AddFoodLogUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/nutrition/ with portion calculation (value × grams / 100)
- [ ] T085 [P] [US4] Create GetFoodLogsForDateUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/nutrition/ returning Flow<List<FoodLog>> grouped by meal type
- [ ] T086 [US4] Create NutritionUiState.kt data class in app/src/main/java/com/example/grapplingcompanion/presentation/nutrition/ with selected date, food logs, daily totals, and target calories
- [ ] T087 [US4] Create NutritionViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/nutrition/ with StateFlow<NutritionUiState>, date navigation, and meal management
- [ ] T088 [US4] Create NutritionScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/nutrition/ with date picker, meal sections (breakfast/lunch/dinner/snack), daily summary card, and "Add Food" FAB
- [ ] T089 [US4] Create FoodSearchUiState.kt data class in app/src/main/java/com/example/grapplingcompanion/presentation/nutrition/ with search query, results, loading, and error states
- [ ] T090 [US4] Create FoodSearchViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/nutrition/ with StateFlow<FoodSearchUiState>, debounced search, and favorite management
- [ ] T091 [US4] Create FoodSearchScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/nutrition/ with search bar, results list, loading indicator, error handling, and food selection dialog
- [ ] T092 [US4] Create AddFoodDialog.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/nutrition/ with portion input (grams), meal type selector, calculated macros display, and save button
- [ ] T093 [US4] Test nutrition diary: search "chicken breast" → verify API results → select food → enter 200g portion → select lunch meal → verify saves → check daily totals update → test favorite foods → test offline mode → edit food log → delete food log

**Checkpoint**: User Stories 1-4 should now be independently functional

---

## Phase 7: User Story 5 - Training Calendar (Priority: P2)

**Goal**: Enable training session planning, calendar view, and training history

**Independent Test**: Create training sessions with different types (grappling/sparring/strength/cardio), view month calendar with training indicators, tap dates to see details, access training history with type filters, edit/delete sessions, test future training entries

### Implementation for User Story 5

- [X] T094 [P] [US5] Create AddTrainingUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/training/ with validation (duration > 0, notes max length)
- [X] T095 [P] [US5] Create GetTrainingsForMonthUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/training/ returning Flow<List<Training>> for date range
- [X] T096 [P] [US5] Create GetUpcomingTrainingUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/training/ with optional type filter
- [X] T097 [US5] Create TrainingCalendarUiState.kt data class in app/src/main/java/com/example/grapplingcompanion/presentation/training/ with current month, trainings, selected date
- [X] T098 [US5] Create TrainingCalendarViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/training/ with StateFlow<TrainingCalendarUiState> and month navigation
- [X] T099 [US5] Create TrainingCalendarScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/training/ with month calendar view, training indicators, date selection, and "Add Training" FAB
- [X] T100 [US5] Create AddTrainingUiState.kt data class in app/src/main/java/com/example/grapplingcompanion/presentation/training/ with form fields and validation
- [X] T101 [US5] Create AddTrainingViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/training/ with StateFlow<AddTrainingUiState> and save logic
- [X] T102 [US5] Create AddTrainingScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/training/ with date picker, type selector, duration input, notes field, and save button
- [ ] T103 [US5] Create TrainingHistoryScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/training/ with LazyColumn list, type filter chips, and training detail cards
- [ ] T104 [US5] Test training calendar: add grappling session for today → verify calendar shows indicator → tap date → verify shows details → add multiple sessions same day → add future training → view history → filter by type → edit session → delete session

**Checkpoint**: User Stories 1-5 should now be independently functional

---

## Phase 8: User Story 6 - Technique Diary (Priority: P3)

**Goal**: Enable technique logging with categories, search, and filtering for reference library

**Independent Test**: Create techniques with different categories (takedown/submission/sweep/defense/control), add descriptions, filter by category, search techniques by name/description, edit/delete techniques

### Implementation for User Story 6

- [X] T105 [P] [US6] Create AddTechniqueUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/technique/ with validation (name not blank, description max 2000 chars)
- [X] T106 [P] [US6] Create GetTechniquesUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/technique/ returning Flow<List<Technique>> with optional category filter
- [X] T107 [P] [US6] Create SearchTechniquesUseCase.kt in app/src/main/java/com/example/grapplingcompanion/domain/usecase/technique/ searching name and description
- [X] T108 [US6] Create TechniquesUiState.kt data class in app/src/main/java/com/example/grapplingcompanion/presentation/techniques/ with techniques list, selected category filter, search query
- [X] T109 [US6] Create TechniquesViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/techniques/ with StateFlow<TechniquesUiState>, filter logic, and search
- [X] T110 [US6] Create TechniquesScreen.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/techniques/ with search bar, category filter chips, LazyColumn list, and "Add Technique" FAB
- [X] T111 [US6] Create AddTechniqueDialog.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/techniques/ with name input, category selector, description field, and save button
- [X] T112 [US6] Create TechniqueDetailDialog.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/techniques/ showing full technique details with edit/delete options
- [ ] T113 [US6] Test technique diary: add "Arm Bar" submission → add "Double Leg" takedown → filter by category → verify filtering works → search "arm" → verify search results → tap technique → view details → edit technique → delete technique

**Checkpoint**: All user stories should now be independently functional

---

## Phase 9: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [X] T114 [P] Create LoadingIndicator.kt composable in app/src/main/java/com/example/grapplingcompanion/presentation/components/ for consistent loading states
- [ ] T115 [P] Add navigation transitions and animations to NavGraph.kt using AnimatedContent
- [ ] T116 [P] Add list item animations to all LazyColumn screens using animateItemPlacement()
- [ ] T117 [P] Add graph animations to WeightChart.kt using Vico animation APIs
- [ ] T118 [P] Add macro progress bar animations using animateFloatAsState in ProgressBar.kt
- [X] T119 Create SettingsScreen.kt in app/src/main/java/com/example/grapplingcompanion/presentation/settings/ with profile editing, theme toggle, and data reset options
- [X] T120 Create SettingsViewModel.kt in app/src/main/java/com/example/grapplingcompanion/presentation/settings/ with theme preference management using DataStore
- [ ] T121 Add error handling UI patterns across all screens (Snackbar for errors, retry buttons)
- [ ] T122 Add input validation feedback across all forms (real-time validation, clear error messages)
- [ ] T123 Add empty state illustrations/messages to all list screens (Dashboard, Nutrition, Training, Techniques)
- [ ] T124 Test theme switching: toggle dark mode in Settings → verify applies instantly → restart app → verify theme persists
- [ ] T125 Test offline functionality: disable network → verify weight tracking works → verify nutrition shows saved foods → verify dashboard shows cached data → verify appropriate offline messages
- [ ] T126 Performance testing: open all screens → verify <500ms navigation → scroll long lists → verify smooth 60fps → add 100+ food logs → verify queries remain fast
- [ ] T127 End-to-end testing: complete full user journey from onboarding through all features → verify data consistency → test edge cases (unrealistic weight values, empty inputs, API failures)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-8)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P1 → P1 → P2 → P2 → P3)
- **Polish (Phase 9)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1) - Onboarding**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P1) - Weight Tracking**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 3 (P1) - Dashboard**: Can start after Foundational (Phase 2) - Integrates with US1 and US2 but testable independently with empty states
- **User Story 4 (P2) - Nutrition**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 5 (P2) - Training**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 6 (P3) - Techniques**: Can start after Foundational (Phase 2) - No dependencies on other stories

### Within Each User Story

- Use cases before ViewModels
- ViewModels before Screens
- Reusable components before screens that use them
- Core implementation before integration
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel (T003-T010)
- All Foundational entity creations can run in parallel (T024-T028)
- All Foundational DAO creations can run in parallel (T029-T033)
- All domain model creations can run in parallel (T036-T043)
- All repository interface definitions can run in parallel (T044-T047)
- All repository implementations can run in parallel (T048-T050, T056)
- All network DTOs can run in parallel (T052-T054)
- Use cases within a story marked [P] can run in parallel
- Once Foundational phase completes, all user stories can start in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch all use cases for User Story 1 together:
Task: "Create GetUserProfileUseCase.kt in app/src/.../domain/usecase/user/"
Task: "Create SaveUserProfileUseCase.kt in app/src/.../domain/usecase/user/"
Task: "Create CalculateTdeeUseCase.kt in app/src/.../domain/usecase/user/"
```

---

## Implementation Strategy

### MVP First (User Stories 1-3 Only)

1. Complete Phase 1: Setup (T001-T013)
2. Complete Phase 2: Foundational (T014-T058) - CRITICAL - blocks all stories
3. Complete Phase 3: User Story 1 - Onboarding (T059-T066)
4. Complete Phase 4: User Story 2 - Weight Tracking (T067-T074)
5. Complete Phase 5: User Story 3 - Dashboard (T075-T082)
6. **STOP and VALIDATE**: Test all three P1 stories independently
7. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (Onboarding working!)
3. Add User Story 2 → Test independently → Deploy/Demo (Weight tracking added!)
4. Add User Story 3 → Test independently → Deploy/Demo (Dashboard complete - MVP!)
5. Add User Story 4 → Test independently → Deploy/Demo (Nutrition tracking added!)
6. Add User Story 5 → Test independently → Deploy/Demo (Training calendar added!)
7. Add User Story 6 → Test independently → Deploy/Demo (Techniques added!)
8. Add Polish phase → Final release
9. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together (T001-T058)
2. Once Foundational is done:
   - Developer A: User Story 1 (T059-T066)
   - Developer B: User Story 2 (T067-T074)
   - Developer C: User Story 4 (T083-T093)
3. Stories complete and integrate independently
4. Developer A moves to User Story 3 (T075-T082) after US1 done
5. Developer B moves to User Story 5 (T094-T104) after US2 done
6. Developer C moves to User Story 6 (T105-T113) after US4 done
7. All developers collaborate on Polish phase (T114-T127)

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Commit after each task or logical group using format: `T0XX: Brief description`
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
- API key MUST be added to local.properties before network tasks
- Follow constitution standards: PascalCase classes, camelCase functions, Entity/UseCase suffixes, max 30 line functions
