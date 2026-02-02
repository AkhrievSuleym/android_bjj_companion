# Grappling Companion — План задач

## Phase 1: Настройка проекта и инфраструктура

### T001: Создание проекта ⭐️ Critical
**Сложность:** Простая  
**Описание:** Создать новый Android-проект с Jetpack Compose в Android Studio.
**Файлы:**
- `build.gradle.kts` (project)
- `app/build.gradle.kts`
- `settings.gradle.kts`

**Acceptance Criteria:**
- [ ] Проект создан с минимальным SDK 24
- [ ] Compose включён и настроен
- [ ] Проект компилируется без ошибок

---

### T002: Добавление зависимостей ⭐️ Critical
**Сложность:** Простая  
**Зависит от:** T001  
**Описание:** Добавить все необходимые зависимости в build.gradle.
**Файлы:**
- `app/build.gradle.kts`

**Acceptance Criteria:**
- [ ] Hilt настроен
- [ ] Room добавлен
- [ ] Retrofit + OkHttp добавлены
- [ ] Navigation Compose добавлен
- [ ] Vico (графики) добавлен
- [ ] Проект синхронизируется без ошибок

---

### T003: Настройка Hilt ⭐️ Critical
**Сложность:** Простая  
**Зависит от:** T002  
**Описание:** Создать Application class и настроить Hilt.
**Файлы:**
- `GrapplingApp.kt`
- `AndroidManifest.xml`

**Acceptance Criteria:**
- [ ] Application class создан с аннотацией @HiltAndroidApp
- [ ] MainActivity имеет аннотацию @AndroidEntryPoint
- [ ] Приложение запускается

---

### T004: Создание темы приложения ⭐️ Critical
**Сложность:** Простая  
**Зависит от:** T003  
**Описание:** Настроить Material3 тему со светлой и тёмной версией.
**Файлы:**
- `presentation/theme/Color.kt`
- `presentation/theme/Theme.kt`
- `presentation/theme/Type.kt`

**Acceptance Criteria:**
- [ ] Определены цвета для светлой и тёмной темы
- [ ] Тема применяется к приложению
- [ ] Типографика настроена

---

### T005: Настройка навигации ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T004  
**Описание:** Создать навигационный граф и Bottom Navigation.
**Файлы:**
- `presentation/navigation/Screen.kt`
- `presentation/navigation/NavGraph.kt`
- `presentation/components/BottomNavBar.kt`
- `MainActivity.kt`

**Acceptance Criteria:**
- [ ] Определены все экраны (sealed class Screen)
- [ ] NavHost настроен
- [ ] Bottom Navigation отображается и работает
- [ ] Переходы между вкладками работают

---

## Phase 2: База данных (Room)

### T006: Создание Entity классов ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T003  
**Описание:** Создать все Entity-классы для Room.
**Файлы:**
- `data/local/database/entity/UserProfileEntity.kt`
- `data/local/database/entity/WeightEntryEntity.kt`
- `data/local/database/entity/FoodLogEntity.kt`
- `data/local/database/entity/TrainingEntity.kt`
- `data/local/database/entity/TechniqueEntity.kt`

**Acceptance Criteria:**
- [ ] Все Entity созданы с правильными аннотациями
- [ ] Типы данных соответствуют схеме БД
- [ ] Primary keys настроены

---

### T007: Создание DAO интерфейсов ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T006  
**Описание:** Создать DAO для каждой таблицы.
**Файлы:**
- `data/local/database/dao/UserProfileDao.kt`
- `data/local/database/dao/WeightEntryDao.kt`
- `data/local/database/dao/FoodLogDao.kt`
- `data/local/database/dao/TrainingDao.kt`
- `data/local/database/dao/TechniqueDao.kt`

**Acceptance Criteria:**
- [ ] Все CRUD-операции определены
- [ ] Используются suspend функции и Flow
- [ ] Запросы для фильтрации по дате работают

---

### T008: Создание AppDatabase ⭐️ Critical
**Сложность:** Простая  
**Зависит от:** T007  
**Описание:** Создать класс базы данных Room.
**Файлы:**
- `data/local/database/AppDatabase.kt`
- `di/DatabaseModule.kt`

**Acceptance Criteria:**
- [ ] Database class создан со всеми entities
- [ ] Hilt module для предоставления database и DAO создан
- [ ] База данных инициализируется при запуске

---

## Phase 3: Domain слой

### T009: Создание Domain моделей ⭐️ Critical
**Сложность:** Простая  
**Зависит от:** T006  
**Описание:** Создать доменные модели (отделённые от Entity).
**Файлы:**
- `domain/model/UserProfile.kt`
- `domain/model/WeightEntry.kt`
- `domain/model/Food.kt`
- `domain/model/FoodLog.kt`
- `domain/model/Training.kt`
- `domain/model/TrainingType.kt`
- `domain/model/Technique.kt`
- `domain/model/TechniqueCategory.kt`

**Acceptance Criteria:**
- [ ] Все модели созданы как data classes
- [ ] Enum'ы для типов и категорий определены
- [ ] Модели не зависят от Room

---

### T010: Создание Repository интерфейсов
**Сложность:** Простая  
**Зависит от:** T009  
**Описание:** Определить интерфейсы репозиториев в domain слое.
**Файлы:**
- `domain/repository/UserRepository.kt`
- `domain/repository/NutritionRepository.kt`
- `domain/repository/TrainingRepository.kt`
- `domain/repository/TechniqueRepository.kt`

**Acceptance Criteria:**
- [ ] Интерфейсы определяют контракт для data слоя
- [ ] Методы возвращают Flow или suspend

---

### T011: Создание Use Cases ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T010  
**Описание:** Создать use cases для бизнес-логики.
**Файлы:**
- `domain/usecase/user/*`
- `domain/usecase/weight/*`
- `domain/usecase/nutrition/*`
- `domain/usecase/training/*`
- `domain/usecase/technique/*`

**Acceptance Criteria:**
- [ ] Каждый use case выполняет одну операцию
- [ ] Use cases инжектятся через Hilt
- [ ] CalculateTdeeUseCase корректно рассчитывает калории

---

## Phase 4: Сетевой слой (USDA API)

### T012: Настройка Retrofit ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T002  
**Описание:** Настроить Retrofit для USDA FoodData Central API.
**Файлы:**
- `data/remote/api/UsdaFoodApi.kt`
- `data/remote/dto/FoodSearchResponse.kt`
- `data/remote/dto/FoodNutrientDto.kt`
- `di/NetworkModule.kt`

**Acceptance Criteria:**
- [ ] API интерфейс создан с методом поиска
- [ ] DTO классы соответствуют API response
- [ ] API key передаётся корректно
- [ ] Logging interceptor настроен

---

### T013: Реализация Repository для питания ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T012, T008, T010  
**Описание:** Реализовать NutritionRepository с поиском через API и сохранением в БД.
**Файлы:**
- `data/repository/NutritionRepositoryImpl.kt`
- `di/RepositoryModule.kt`

**Acceptance Criteria:**
- [ ] Поиск продуктов работает через API
- [ ] Результаты маппятся в доменные модели
- [ ] Сохранение в food_logs работает
- [ ] Ошибки сети обрабатываются

---

## Phase 5: Реализация Repository

### T014: UserRepository Implementation
**Сложность:** Простая  
**Зависит от:** T008, T010  
**Файлы:** `data/repository/UserRepositoryImpl.kt`

### T015: TrainingRepository Implementation
**Сложность:** Простая  
**Зависит от:** T008, T010  
**Файлы:** `data/repository/TrainingRepositoryImpl.kt`

### T016: TechniqueRepository Implementation
**Сложность:** Простая  
**Зависит от:** T008, T010  
**Файлы:** `data/repository/TechniqueRepositoryImpl.kt`

---

## Phase 6: UI — Онбординг

### T017: Экран онбординга ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T005, T011, T014  
**Описание:** Создать экраны онбординга с вводом данных профиля.
**Файлы:**
- `presentation/onboarding/OnboardingScreen.kt`
- `presentation/onboarding/OnboardingViewModel.kt`

**Acceptance Criteria:**
- [ ] 2-3 слайда с приветствием
- [ ] Форма ввода: имя, текущий вес, целевой вес, весовая категория
- [ ] Расчёт TDEE при сохранении
- [ ] Анимация перехода между слайдами
- [ ] Редирект на Dashboard после завершения

---

## Phase 7: UI — Dashboard

### T018: Dashboard Screen ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T005, T011  
**Описание:** Создать главный экран с карточками статистики.
**Файлы:**
- `presentation/dashboard/DashboardScreen.kt`
- `presentation/dashboard/DashboardViewModel.kt`
- `presentation/components/StatCard.kt`

**Acceptance Criteria:**
- [ ] Карточка веса с прогрессом
- [ ] Карточка калорий за сегодня
- [ ] Карточка ближайшей тренировки
- [ ] Быстрые действия работают
- [ ] Данные подгружаются из БД

---

## Phase 8: UI — Питание

### T019: Экран дневника питания ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T005, T013  
**Файлы:**
- `presentation/nutrition/NutritionScreen.kt`
- `presentation/nutrition/NutritionViewModel.kt`

**Acceptance Criteria:**
- [ ] Переключатель дат
- [ ] Список приёмов пищи за день
- [ ] Итоги КБЖУ за день
- [ ] Кнопка добавления продукта

---

### T020: Экран поиска продуктов ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T019  
**Файлы:**
- `presentation/nutrition/FoodSearchScreen.kt`
- `presentation/nutrition/FoodSearchViewModel.kt`

**Acceptance Criteria:**
- [ ] Поле поиска с debounce
- [ ] Список результатов из API
- [ ] Выбор продукта открывает диалог с граммовкой
- [ ] Loading и error states
- [ ] Сохранение в дневник

---

## Phase 9: UI — Тренировки

### T021: Календарь тренировок ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T005, T015  
**Файлы:**
- `presentation/training/TrainingCalendarScreen.kt`
- `presentation/training/TrainingCalendarViewModel.kt`

**Acceptance Criteria:**
- [ ] Месячный календарь
- [ ] Отметки дней с тренировками
- [ ] Клик на день показывает тренировку

---

### T022: Добавление тренировки
**Сложность:** Простая  
**Зависит от:** T021  
**Файлы:**
- `presentation/training/AddTrainingScreen.kt`
- `presentation/training/AddTrainingViewModel.kt`

**Acceptance Criteria:**
- [ ] Выбор даты, типа, длительности
- [ ] Поле для заметок
- [ ] Сохранение в БД

---

## Phase 10: UI — Техники

### T023: Список техник
**Сложность:** Простая  
**Зависит от:** T005, T016  
**Файлы:**
- `presentation/techniques/TechniquesScreen.kt`
- `presentation/techniques/TechniquesViewModel.kt`

**Acceptance Criteria:**
- [ ] Список техник с фильтром по категории
- [ ] FAB для добавления

---

### T024: Добавление техники
**Сложность:** Простая  
**Зависит от:** T023  
**Файлы:**
- `presentation/techniques/AddTechniqueScreen.kt`

---

## Phase 11: UI — Прогресс и настройки

### T025: Экран прогресса ⭐️ Critical
**Сложность:** Средняя  
**Зависит от:** T005, T011  
**Файлы:**
- `presentation/progress/ProgressScreen.kt`
- `presentation/progress/ProgressViewModel.kt`
- `presentation/components/WeightChart.kt`

**Acceptance Criteria:**
- [ ] График веса с анимацией
- [ ] Переключатель периода (неделя/месяц)
- [ ] Статистика тренировок

---

### T026: Экран настроек
**Сложность:** Простая  
**Зависит от:** T005  
**Файлы:**
- `presentation/settings/SettingsScreen.kt`
- `presentation/settings/SettingsViewModel.kt`
- `data/local/datastore/SettingsDataStore.kt`

**Acceptance Criteria:**
- [ ] Редактирование профиля
- [ ] Переключение темы
- [ ] Тема сохраняется в DataStore

---

## Phase 12: Финализация

### T027: Анимации и polish
**Сложность:** Средняя  
**Зависит от:** T017-T026  
**Описание:** Добавить анимации переходов и улучшить UX.

**Acceptance Criteria:**
- [ ] Анимации навигации между экранами
- [ ] Анимация появления списков
- [ ] Анимация графиков
- [ ] Анимация прогресс-баров

---

### T028: Тестирование и исправление багов
**Сложность:** Средняя  
**Зависит от:** T027  

**Acceptance Criteria:**
- [ ] Все экраны открываются без крашей
- [ ] Данные сохраняются между сессиями
- [ ] API работает корректно
- [ ] Тёмная тема работает

---

## Порядок выполнения (Critical Path)

```
Phase 1: T001 → T002 → T003 → T004 → T005
Phase 2: T006 → T007 → T008
Phase 3: T009 → T010 → T011
Phase 4: T012 → T013
Phase 5: T014, T015, T016 (параллельно)
Phase 6: T017
Phase 7: T018
Phase 8: T019 → T020
Phase 9: T021 → T022
Phase 10: T023 → T024
Phase 11: T025, T026 (параллельно)
Phase 12: T027 → T028
```

**Оценка времени:** 20-30 часов разработки
