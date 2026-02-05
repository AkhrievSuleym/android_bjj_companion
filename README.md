# 🥋 BJJ Companion

> Your all-in-one companion for Brazilian Jiu-Jitsu training, nutrition, and progress tracking

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-26%2B-green.svg?style=flat&logo=android)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📱 About

BJJ Companion is a comprehensive Android application designed for Brazilian Jiu-Jitsu practitioners who want to track their training progress, manage nutrition, monitor weight changes, and build their personal technique library. Whether you're a white belt just starting out or a seasoned competitor, this app helps you stay organized and focused on your grappling journey.

## ✨ Features

### 🏠 Dashboard
- Overview of your training stats and progress
- Quick access to recent weight entries
- Upcoming training sessions
- Daily calorie and macronutrient summary

### 📊 Progress Tracking
- Log and visualize weight changes over time
- Interactive charts showing trends
- Calculate TDEE (Total Daily Energy Expenditure)
- Set and track weight goals

### 🍽️ Nutrition Management
- Track daily food intake with macronutrient breakdown
- Search foods using USDA FoodData Central API
- Log meals with detailed nutritional information
- Monitor calories, protein, carbs, and fats
- View daily and weekly nutrition summaries

### 🥋 Technique Library
- Build your personal collection of BJJ techniques
- Organize by categories (Guard, Mount, Back Control, etc.)
- Add detailed notes and descriptions
- Search and filter techniques
- Track which techniques you're learning

### 📅 Training Calendar
- Schedule and track BJJ training sessions
- Support for different training types (Gi, No-Gi, Drilling, Competition)
- Visual monthly calendar view
- Track training frequency and patterns
- Add notes to each training session

### ⚙️ Settings
- Customize app preferences
- Manage user profile
- Toggle dark mode
- Configure notifications

## 🛠️ Tech Stack

### Architecture
- **MVVM** (Model-View-ViewModel) pattern
- **Clean Architecture** with separate domain, data, and presentation layers
- **Use Cases** for business logic encapsulation

### Core Technologies
- **Kotlin** - Modern, concise, and safe programming language
- **Jetpack Compose** - Declarative UI framework for native Android
- **Coroutines & Flow** - Asynchronous programming and reactive streams
- **Material 3** - Modern Material Design components

### Dependency Injection
- **Hilt** - Dependency injection framework

### Local Data
- **Room** - SQLite database wrapper with compile-time verification
- **DataStore** - Modern data storage solution for preferences

### Networking
- **Retrofit** - Type-safe HTTP client
- **OkHttp** - HTTP client with logging interceptor
- **Kotlin Serialization** - JSON serialization/deserialization

### UI Components
- **Navigation Compose** - Navigation between screens
- **Coil** - Image loading library
- **Vico** - Chart library for data visualization

### Testing
- **JUnit** - Unit testing framework
- **MockK** - Mocking library for Kotlin
- **Espresso** - UI testing framework

## 📂 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/bjjcompanion/
│   │   │   ├── data/
│   │   │   │   ├── local/          # Room database & DataStore
│   │   │   │   ├── remote/         # API services & DTOs
│   │   │   │   └── repository/     # Repository implementations
│   │   │   ├── di/                 # Hilt dependency injection modules
│   │   │   ├── domain/
│   │   │   │   ├── model/          # Domain models
│   │   │   │   ├── repository/     # Repository interfaces
│   │   │   │   └── usecase/        # Business logic use cases
│   │   │   └── presentation/
│   │   │       ├── components/     # Reusable UI components
│   │   │       ├── dashboard/      # Dashboard feature
│   │   │       ├── navigation/     # Navigation setup
│   │   │       ├── nutrition/      # Nutrition tracking
│   │   │       ├── onboarding/     # User onboarding
│   │   │       ├── progress/       # Weight tracking
│   │   │       ├── settings/       # App settings
│   │   │       ├── techniques/     # Technique library
│   │   │       ├── training/       # Training calendar
│   │   │       └── theme/          # App theming
│   │   └── res/                    # Resources (layouts, drawables, etc.)
│   └── test/                       # Unit tests
└── build.gradle.kts
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or later
- JDK 17 or later
- Android SDK 26 (Android 8.0) or higher
- USDA FoodData Central API key (optional, for food search feature)

### Installation

1. Clone the repository
```bash
git clone https://github.com/AkhrievSuleym/android_bjj_companion.git
cd android_bjj_companion
```

2. Create a `local.properties` file in the root directory and add your USDA API key (optional):
```properties
USDA_API_KEY=your_api_key_here
```

> You can get a free API key from [USDA FoodData Central](https://fdc.nal.usda.gov/api-guide.html)

3. Open the project in Android Studio

4. Sync Gradle files and run the app

## 🎨 Screenshots

*Coming soon*

## 🗺️ Roadmap

- [ ] Exercise library with video demonstrations
- [ ] Competition tracker
- [ ] Belt promotion progress
- [ ] Training partners management
- [ ] Cloud sync across devices
- [ ] Social features (share techniques, training logs)
- [ ] Integration with fitness trackers
- [ ] Nutrition meal planning
- [ ] Progress photos gallery

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Suleyman Akhriev**

- GitHub: [@AkhrievSuleym](https://github.com/AkhrievSuleym)

## 🙏 Acknowledgments

- USDA FoodData Central for nutrition data API
- BJJ community for inspiration and feedback
- Open source libraries and their contributors

---

**Made with ❤️ for the BJJ community** | *Oss!* 🥋
