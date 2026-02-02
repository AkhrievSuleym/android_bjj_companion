# Grappling Companion — Технический план

## Технологический стек

### Язык программирования
**Kotlin** — основной язык для Android-разработки. Современный, лаконичный, полностью поддерживается Google.

### UI Framework
**Jetpack Compose** — декларативный UI-фреймворк. Выбран потому что:
- Современный подход, похожий на Flutter (легче переход)
- Меньше boilerplate-кода
- Встроенная поддержка анимаций
- Material Design 3 из коробки

### Архитектура
**Clean Architecture + MVVM**

Слои:
- **data** — работа с API и базой данных, реализации репозиториев
- **domain** — бизнес-логика, use cases, интерфейсы репозиториев
- **presentation** — UI (Compose) и ViewModels

### Dependency Injection
**Hilt** — стандарт для Android, упрощает внедрение зависимостей, хорошая интеграция с ViewModel.

### Сетевой слой
- **Retrofit 2** — HTTP-клиент для REST API
- **OkHttp** — HTTP-клиент под капотом (логирование, таймауты)
- **Kotlin Serialization** — сериализация JSON

### База данных
**Room** — ORM для SQLite от Google. Работает с корутинами и Flow.

### Асинхронность
- **Kotlin Coroutines** — для асинхронных операций
- **Kotlin Flow** — реактивные потоки данных

### Навигация
**Jetpack Navigation Compose** — навигация между экранами с поддержкой анимаций.

### Дополнительные библиотеки
- **Coil** — загрузка изображений
- **DataStore** — хранение настроек
- **Vico** или **MPAndroidChart (Compose wrapper)** — графики

---

## Структура проекта

```
app/
├── src/main/java/com/example/grapplingcompanion/
│   ├── GrapplingApp.kt                 # Application class
│   ├── MainActivity.kt
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
│   │   │   ├── TrainingType.kt
│   │   │   ├── Technique.kt
│   │   │   └── TechniqueCategory.kt
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
└── build.gradle.kts
```

---

## Схема базы данных

### Таблица: user_profile
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER PK | Первичный ключ (всегда 1) |
| name | TEXT | Имя пользователя |
| current_weight | REAL | Текущий вес (кг) |
| target_weight | REAL | Целевой вес (кг) |
| weight_class | TEXT | Весовая категория |
| daily_calories | INTEGER | Дневная норма калорий |
| created_at | INTEGER | Дата создания (timestamp) |

### Таблица: weight_entries
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER PK | Автоинкремент |
| weight | REAL | Вес (кг) |
| date | INTEGER | Дата записи (timestamp) |
| note | TEXT? | Заметка (опционально) |

### Таблица: food_logs
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER PK | Автоинкремент |
| fdc_id | INTEGER | ID продукта из USDA API |
| name | TEXT | Название продукта |
| calories | REAL | Калории |
| protein | REAL | Белки (г) |
| fat | REAL | Жиры (г) |
| carbs | REAL | Углеводы (г) |
| grams | REAL | Количество (г) |
| meal_type | TEXT | Тип приёма (breakfast/lunch/dinner/snack) |
| date | INTEGER | Дата (timestamp) |

### Таблица: trainings
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER PK | Автоинкремент |
| date | INTEGER | Дата тренировки (timestamp) |
| type | TEXT | Тип (grappling/sparring/strength/cardio) |
| duration_minutes | INTEGER | Длительность (мин) |
| notes | TEXT? | Заметки |

### Таблица: techniques
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER PK | Автоинкремент |
| name | TEXT | Название техники |
| category | TEXT | Категория (takedown/submission/sweep/defense/control) |
| description | TEXT? | Описание/заметки |
| created_at | INTEGER | Дата добавления (timestamp) |

---

## API Integration (USDA FoodData Central)

### Base URL
```
https://api.nal.usda.gov/fdc/v1/
```

### Endpoints

#### Поиск продуктов
```
GET /foods/search?api_key={API_KEY}&query={query}&pageSize=25
```

**Response:**
```json
{
  "foods": [
    {
      "fdcId": 123456,
      "description": "Chicken breast, raw",
      "foodNutrients": [
        { "nutrientId": 1008, "nutrientName": "Energy", "value": 120, "unitName": "KCAL" },
        { "nutrientId": 1003, "nutrientName": "Protein", "value": 22.5, "unitName": "G" },
        { "nutrientId": 1004, "nutrientName": "Total lipid (fat)", "value": 2.6, "unitName": "G" },
        { "nutrientId": 1005, "nutrientName": "Carbohydrate", "value": 0, "unitName": "G" }
      ]
    }
  ]
}
```

### Nutrient IDs
- 1008 — Energy (KCAL)
- 1003 — Protein (G)
- 1004 — Fat (G)
- 1005 — Carbohydrates (G)

---

## Зависимости (build.gradle.kts)

```kotlin
// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose:1.8.2")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// Hilt
implementation("com.google.dagger:hilt-android:2.50")
kapt("com.google.dagger:hilt-compiler:2.50")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Retrofit + OkHttp
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Kotlin Serialization
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Coil
implementation("io.coil-kt:coil-compose:2.5.0")

// Charts (Vico)
implementation("com.patrykandpatrick.vico:compose-m3:1.13.1")
```
