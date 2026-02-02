# Grappling Companion — Constitution

## Незыблемые принципы проекта

Этот документ определяет правила, от которых нельзя отступать при разработке.

---

## 1. Архитектура

### Clean Architecture
- Проект ДОЛЖЕН следовать Clean Architecture с разделением на слои: `data`, `domain`, `presentation`
- Зависимости ДОЛЖНЫ направляться внутрь: presentation → domain ← data
- Domain слой НЕ ДОЛЖЕН зависеть от Android SDK, Room, Retrofit или других библиотек
- Use Cases ДОЛЖНЫ выполнять только одну операцию

### MVVM
- Каждый экран ДОЛЖЕН иметь свой ViewModel
- ViewModel НЕ ДОЛЖЕН содержать ссылки на View или Context
- UI state ДОЛЖЕН передаваться через StateFlow
- События ДОЛЖНЫ обрабатываться через sealed classes

---

## 2. Технологический стек

### Обязательные технологии
- **Язык:** Kotlin (никакой Java)
- **UI:** Jetpack Compose (никаких XML layouts)
- **DI:** Hilt
- **Database:** Room
- **Network:** Retrofit + OkHttp + Kotlin Serialization
- **Async:** Coroutines + Flow
- **Navigation:** Jetpack Navigation Compose

### Запрещённые подходы
- НЕ использовать RxJava
- НЕ использовать Dagger без Hilt
- НЕ использовать XML layouts
- НЕ использовать LiveData (только StateFlow/Flow)
- НЕ использовать сторонние библиотеки без крайней необходимости

---

## 3. Код

### Именование
- Классы: PascalCase (`UserProfileEntity`, `DashboardViewModel`)
- Функции и переменные: camelCase (`getUserProfile`, `currentWeight`)
- Константы: SCREAMING_SNAKE_CASE (`BASE_URL`, `API_KEY`)
- Пакеты: lowercase (`data.repository`, `domain.usecase`)

### Структура файлов
- Один класс = один файл (исключение: sealed classes и их подклассы)
- Entity классы ДОЛЖНЫ иметь суффикс `Entity`
- DTO классы ДОЛЖНЫ иметь суффикс `Dto` или `Response`
- Use Cases ДОЛЖНЫ иметь суффикс `UseCase`

### Функции
- Максимальная длина функции: 30 строк
- Функция ДОЛЖНА делать одну вещь
- Избегать вложенности более 3 уровней

### Null Safety
- Использовать nullable типы только когда это действительно необходимо
- Избегать `!!` оператора
- Использовать `?.let`, `?:`, `?.run` для безопасной работы с null

---

## 4. UI/UX

### Material Design 3
- Использовать только Material 3 компоненты
- Следовать Material Design guidelines
- Поддерживать светлую и тёмную тему

### Compose
- Composable функции ДОЛЖНЫ быть stateless где возможно
- State hoisting: состояние поднимается вверх
- Preview аннотации для компонентов
- Избегать side effects в композиции

### Анимации
- Использовать `animateXAsState` для простых анимаций
- Использовать `AnimatedVisibility` для появления/скрытия
- Анимации ДОЛЖНЫ быть плавными (не блокировать UI)

---

## 5. Работа с данными

### Room
- Все операции с БД ДОЛЖНЫ быть suspend или возвращать Flow
- НЕ использовать allowMainThreadQueries()
- Использовать индексы для часто запрашиваемых полей

### Network
- Все сетевые запросы ДОЛЖНЫ быть suspend
- Обрабатывать ошибки сети (показывать пользователю понятное сообщение)
- Использовать timeout для запросов
- API ключ НЕ ДОЛЖЕН храниться в коде (использовать BuildConfig или local.properties)

### Repository Pattern
- Repository ДОЛЖЕН быть единственной точкой доступа к данным
- Repository решает откуда брать данные (cache/network/db)
- Repository возвращает доменные модели, не Entity или DTO

---

## 6. Обработка ошибок

- НЕ игнорировать исключения (catch без обработки)
- Использовать sealed class для Result (Success/Error)
- Показывать пользователю понятные сообщения об ошибках
- Логировать ошибки для отладки

---

## 7. Git

### Коммиты
- Один коммит = одна задача
- Формат: `T0XX: Краткое описание`
- Пример: `T005: Setup navigation with bottom bar`

### Ветки
- Разработка ведётся в feature ветках
- Формат: `feature/TXXX-short-description`

---

## 8. Безопасность

- API ключи НЕ ДОЛЖНЫ коммититься в репозиторий
- Использовать local.properties или BuildConfig для секретов
- Валидировать пользовательский ввод

---

## 9. Производительность

- Избегать лишних рекомпозиций в Compose
- Использовать `remember` и `derivedStateOf` где нужно
- Тяжёлые операции выносить в IO dispatcher
- Не блокировать main thread

---

## 10. Документация

- Публичные API методы ДОЛЖНЫ иметь KDoc комментарии
- README ДОЛЖЕН содержать инструкцию по запуску
- Сложная бизнес-логика ДОЛЖНА быть задокументирована
