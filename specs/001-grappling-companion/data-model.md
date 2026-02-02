# Data Model: Grappling Companion

**Feature**: Grappling Companion
**Date**: 2026-01-28
**Phase**: 1 (Design & Contracts)

## Purpose

This document defines the data model for the Grappling Companion app, including domain models, database entities, and their relationships. The model supports offline-first architecture with local persistence using Room database.

## Domain Models

Domain models represent business entities independent of any framework or storage mechanism. These live in the `domain/model/` package.

### UserProfile

Represents the athlete's profile and goal settings.

**Fields**:
- `id`: Long (always 1 - single user per device)
- `name`: String
- `currentWeight`: Float (kg)
- `targetWeight`: Float (kg)
- `weightClass`: String (e.g., "77kg", "under 85kg")
- `dailyCalories`: Int (TDEE calculation result)
- `createdAt`: Long (timestamp)

**Validation Rules**:
- `currentWeight` > 0
- `targetWeight` > 0
- `name` not blank
- `weightClass` not blank
- `dailyCalories` > 0

**Business Logic**:
- Calculate progress: `(currentWeight - targetWeight) / (initialWeight - targetWeight) * 100`
- Calculate remaining kg: `currentWeight - targetWeight`

### WeightEntry

Represents a single weight measurement.

**Fields**:
- `id`: Long (auto-generated)
- `weight`: Float (kg)
- `date`: Long (timestamp, normalized to start of day)
- `note`: String? (optional note)

**Validation Rules**:
- `weight` > 0 and < 500 (sanity check)
- `date` cannot be in future
- One entry per day (update if exists)

**Relationships**:
- Belongs to UserProfile (implicit - single user)

### Food

Represents a food item from USDA API or user-saved custom food.

**Fields**:
- `fdcId`: Int (USDA FoodData Central ID, 0 for custom foods)
- `name`: String
- `caloriesPer100g`: Float
- `proteinPer100g`: Float (grams)
- `fatPer100g`: Float (grams)
- `carbsPer100g`: Float (grams)
- `isCustom`: Boolean (true if user-created, false if from API)

**Validation Rules**:
- `name` not blank
- All nutrition values >= 0

**Business Logic**:
- Calculate nutrition for portion: `value * (grams / 100.0)`

### FoodLog

Represents a logged meal entry (food + portion + meal time).

**Fields**:
- `id`: Long (auto-generated)
- `food`: Food (embedded)
- `grams`: Float (portion size)
- `mealType`: MealType (enum: BREAKFAST, LUNCH, DINNER, SNACK)
- `date`: Long (timestamp, normalized to start of day)
- `calories`: Float (calculated: food.caloriesPer100g * grams / 100)
- `protein`: Float (calculated)
- `fat`: Float (calculated)
- `carbs`: Float (calculated)

**Validation Rules**:
- `grams` > 0
- `date` cannot be in future

**Relationships**:
- Contains one Food (embedded)
- Grouped by date for daily summary

**Business Logic**:
- Daily totals: Sum all FoodLog entries for a given date
- Macro percentages: `(macro / totalCalories) * 100`

### Training

Represents a training session.

**Fields**:
- `id`: Long (auto-generated)
- `date`: Long (timestamp)
- `type`: TrainingType (enum)
- `durationMinutes`: Int
- `notes`: String? (optional)

**Validation Rules**:
- `durationMinutes` > 0
- `notes` max length 1000 characters

**Relationships**:
- Multiple trainings can exist for same day

**TrainingType Enum**:
- `GRAPPLING` - Technical grappling/BJJ training
- `SPARRING` - Live rolling/sparring
- `STRENGTH` - Strength/conditioning training
- `CARDIO` - Cardio/endurance work

### Technique

Represents a learned grappling technique.

**Fields**:
- `id`: Long (auto-generated)
- `name`: String
- `category`: TechniqueCategory (enum)
- `description`: String? (optional notes)
- `createdAt`: Long (timestamp)

**Validation Rules**:
- `name` not blank
- `description` max length 2000 characters

**Relationships**:
- Standalone entity, no direct relationships

**TechniqueCategory Enum**:
- `TAKEDOWN` - Takedowns and throws
- `SUBMISSION` - Submission techniques
- `SWEEP` - Sweeps and reversals
- `DEFENSE` - Defensive techniques
- `CONTROL` - Control positions and transitions

## Database Schema (Room)

Room entities map domain models to SQLite tables. These live in `data/local/database/entity/` package.

### Table: user_profile

**Entity**: `UserProfileEntity`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY | Always 1 (single user) |
| name | TEXT | NOT NULL | User's name |
| current_weight | REAL | NOT NULL | Current weight (kg) |
| target_weight | REAL | NOT NULL | Goal weight (kg) |
| weight_class | TEXT | NOT NULL | Competition weight class |
| daily_calories | INTEGER | NOT NULL | Calculated TDEE |
| created_at | INTEGER | NOT NULL | Creation timestamp |

**Indexes**: None (single row table)

**DAO Methods**:
- `getUserProfile(): Flow<UserProfileEntity?>` - Observe profile changes
- `insertOrUpdateProfile(profile: UserProfileEntity)` - Upsert operation
- `getProfileOnce(): UserProfileEntity?` - One-time fetch (for calculations)

### Table: weight_entries

**Entity**: `WeightEntryEntity`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique ID |
| weight | REAL | NOT NULL | Weight measurement (kg) |
| date | INTEGER | NOT NULL, INDEXED | Date timestamp (start of day) |
| note | TEXT | NULLABLE | Optional note |

**Indexes**:
- `date` (for efficient date range queries)
- UNIQUE constraint on `date` (one entry per day)

**DAO Methods**:
- `insertWeightEntry(entry: WeightEntryEntity): Long` - Returns ID
- `getWeightEntriesForRange(startDate: Long, endDate: Long): Flow<List<WeightEntryEntity>>` - Date range query
- `getLatestWeightEntry(): Flow<WeightEntryEntity?>` - Most recent entry
- `deleteWeightEntry(id: Long)` - Delete entry

### Table: food_logs

**Entity**: `FoodLogEntity`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique ID |
| fdc_id | INTEGER | NOT NULL | USDA API food ID (0 for custom) |
| name | TEXT | NOT NULL | Food name |
| calories | REAL | NOT NULL | Total calories (for portion) |
| protein | REAL | NOT NULL | Protein grams (for portion) |
| fat | REAL | NOT NULL | Fat grams (for portion) |
| carbs | REAL | NOT NULL | Carbs grams (for portion) |
| grams | REAL | NOT NULL | Portion size (grams) |
| meal_type | TEXT | NOT NULL | Meal category (breakfast/lunch/dinner/snack) |
| date | INTEGER | NOT NULL, INDEXED | Date timestamp (start of day) |

**Indexes**:
- `date` (for daily summary queries)
- `fdc_id` (for finding previously logged foods)

**DAO Methods**:
- `insertFoodLog(log: FoodLogEntity): Long`
- `getFoodLogsForDate(date: Long): Flow<List<FoodLogEntity>>` - All meals for a day
- `getFoodLogsByMealType(date: Long, mealType: String): Flow<List<FoodLogEntity>>` - Specific meal
- `deleteFoodLog(id: Long)`
- `updateFoodLog(log: FoodLogEntity)`

**Business Queries**:
- Daily nutrition summary: `SELECT SUM(calories), SUM(protein), SUM(fat), SUM(carbs) FROM food_logs WHERE date = ?`

### Table: trainings

**Entity**: `TrainingEntity`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique ID |
| date | INTEGER | NOT NULL, INDEXED | Training date timestamp |
| type | TEXT | NOT NULL | Training type (grappling/sparring/strength/cardio) |
| duration_minutes | INTEGER | NOT NULL | Duration in minutes |
| notes | TEXT | NULLABLE | Optional notes (max 1000 chars) |

**Indexes**:
- `date` (for calendar month queries)

**DAO Methods**:
- `insertTraining(training: TrainingEntity): Long`
- `getTrainingsForMonth(startDate: Long, endDate: Long): Flow<List<TrainingEntity>>` - Calendar view
- `getTrainingsByType(type: String): Flow<List<TrainingEntity>>` - Filter by type
- `getUpcomingTraining(currentDate: Long): Flow<TrainingEntity?>` - Next training (for dashboard)
- `deleteTraining(id: Long)`
- `updateTraining(training: TrainingEntity)`

### Table: techniques

**Entity**: `TechniqueEntity`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique ID |
| name | TEXT | NOT NULL | Technique name |
| category | TEXT | NOT NULL | Category (takedown/submission/sweep/defense/control) |
| description | TEXT | NULLABLE | Notes (max 2000 chars) |
| created_at | INTEGER | NOT NULL, INDEXED | Creation timestamp |

**Indexes**:
- `created_at` (for chronological listing)
- `category` (for filtering by category)

**DAO Methods**:
- `insertTechnique(technique: TechniqueEntity): Long`
- `getAllTechniques(): Flow<List<TechniqueEntity>>` - All techniques sorted by date
- `getTechniquesByCategory(category: String): Flow<List<TechniqueEntity>>` - Filter
- `searchTechniques(query: String): Flow<List<TechniqueEntity>>` - Search name and description
- `deleteTechnique(id: Long)`
- `updateTechnique(technique: TechniqueEntity)`

## Data Flow

### Repository Pattern

Repositories act as the single source of truth, abstracting data sources (Room, Retrofit, DataStore).

**Key Repositories**:

1. **UserRepository**
   - Sources: Room (user_profile table), DataStore (theme preference)
   - Operations: Get/save profile, calculate TDEE

2. **NutritionRepository**
   - Sources: Room (food_logs table), Retrofit (USDA API)
   - Operations: Search foods, add food log, get daily nutrition

3. **TrainingRepository**
   - Sources: Room (trainings table)
   - Operations: CRUD trainings, get month view, get upcoming

4. **TechniqueRepository**
   - Sources: Room (techniques table)
   - Operations: CRUD techniques, filter by category, search

### Data Mapping

**Entity ↔ Domain Model Mapping**:

Repositories convert between database entities and domain models:

```kotlin
// Example: WeightEntryEntity → WeightEntry (domain)
fun WeightEntryEntity.toDomain() = WeightEntry(
    id = id,
    weight = weight,
    date = date,
    note = note
)

// Example: WeightEntry (domain) → WeightEntryEntity
fun WeightEntry.toEntity() = WeightEntryEntity(
    id = id,
    weight = weight,
    date = date,
    note = note
)
```

**DTO → Domain Mapping**:

Network DTOs (from USDA API) convert to domain models:

```kotlin
// FoodSearchResponse (DTO) → List<Food> (domain)
fun FoodSearchResponse.toDomainModels(): List<Food> {
    return foods.map { foodDto ->
        Food(
            fdcId = foodDto.fdcId,
            name = foodDto.description,
            caloriesPer100g = foodDto.getNutrientValue(1008) ?: 0f,
            proteinPer100g = foodDto.getNutrientValue(1003) ?: 0f,
            fatPer100g = foodDto.getNutrientValue(1004) ?: 0f,
            carbsPer100g = foodDto.getNutrientValue(1005) ?: 0f,
            isCustom = false
        )
    }
}
```

## State Management

### UI State Models

Each screen has a corresponding UI state data class (in ViewModel):

**Example: DashboardUiState**
```kotlin
data class DashboardUiState(
    val currentWeight: Float? = null,
    val targetWeight: Float? = null,
    val progressPercent: Float = 0f,
    val todayCalories: Int = 0,
    val targetCalories: Int = 0,
    val macros: Macros = Macros(),
    val nextTraining: Training? = null,
    val weightGraphData: List<WeightEntry> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
```

ViewModels expose `StateFlow<UiState>` for Compose to observe.

## Data Persistence Strategy

### Offline-First Architecture

- **Primary Storage**: Room database (local SQLite)
- **Network**: USDA API (food search only)
- **Cache**: Food search results stored in Room for offline access
- **Sync**: None (MVP is local-only, no cloud sync)

### Data Lifecycle

1. **User Input** → ViewModel validates → UseCase processes → Repository saves to Room
2. **Data Changes** → Room emits Flow → Repository maps to domain → ViewModel updates StateFlow → Compose recomposes
3. **API Calls** → Retrofit fetches → Repository caches in Room → Returns domain model

### Migration Strategy

Room supports schema versioning and migrations:

```kotlin
@Database(
    entities = [UserProfileEntity::class, WeightEntryEntity::class, ...],
    version = 1
)
abstract class AppDatabase : RoomDatabase() { ... }
```

Future migrations handled via `Migration` objects when schema changes.

## Data Validation

### Input Validation

Validation occurs at multiple layers:

1. **UI Layer**: Immediate feedback (e.g., weight must be positive number)
2. **ViewModel Layer**: Business rule validation before calling use cases
3. **Use Case Layer**: Domain-specific validation (e.g., date cannot be future)
4. **Repository Layer**: Final validation before database insert

### Validation Examples

```kotlin
// Weight validation
fun validateWeight(weight: Float): Result<Float, String> {
    return when {
        weight <= 0 -> Result.Error("Weight must be positive")
        weight > 500 -> Result.Error("Weight seems unrealistic")
        else -> Result.Success(weight)
    }
}

// Date validation (no future dates for weight)
fun validateWeightDate(date: Long): Result<Long, String> {
    val now = System.currentTimeMillis()
    return if (date > now) {
        Result.Error("Cannot log weight for future date")
    } else {
        Result.Success(date)
    }
}
```

## Performance Considerations

### Database Optimization

- **Indexes**: Date fields indexed for fast range queries
- **Batch Operations**: Use transactions for multiple inserts
- **Pagination**: LazyColumn with paging for large lists (training history, techniques)
- **Observers**: Flow emissions only when data actually changes

### Memory Management

- **Lazy Loading**: Don't load all data at once
- **Image Caching**: Coil handles image memory (future feature)
- **State Cleanup**: ViewModels clear state on screen exit

## Summary

The data model supports:
- ✅ Offline-first architecture (Room primary storage)
- ✅ Reactive data flow (Room Flow → StateFlow → Compose)
- ✅ Clean separation (Entity ↔ Domain ↔ DTO mapping)
- ✅ Type safety (Kotlin data classes, Room compile-time checks)
- ✅ Validation at multiple layers
- ✅ Scalable for future features (modular repository pattern)
