# Feature Specification: Grappling Companion

**Feature Branch**: `001-grappling-companion`
**Created**: 2026-01-28
**Status**: Draft
**Input**: User description: "Grappling Companion mobile app for BJJ/grappling athletes with nutrition tracking, weight monitoring, and training diary"

## Overview

A mobile application for grappling athletes (BJJ, wrestling, grappling) that unifies nutrition control, weight tracking, and training diary in one place. Primary focus is competition preparation and weight class management for amateur and professional athletes ages 16-40.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Onboarding & Profile Setup (Priority: P1)

As a new athlete user, I want to complete an initial setup with my profile information (name, current weight, target weight, weight class) so that the app can calculate my nutrition goals and track my progress toward competition weight.

**Why this priority**: Foundation for all other features. Without profile and baseline data, no personalized tracking or recommendations are possible. This is the entry point for every new user.

**Independent Test**: Can be fully tested by completing the onboarding flow from app launch to profile creation, verifying all data is saved and retrievable, and confirming the app calculates daily calorie targets (TDEE) based on input data.

**Acceptance Scenarios**:

1. **Given** a new user launches the app for the first time, **When** they view the welcome screens, **Then** they see 2-3 onboarding screens explaining nutrition tracking, weight monitoring, and training diary features
2. **Given** user is on profile setup screen, **When** they enter name, current weight (kg), target weight (kg), and select weight class, **Then** the system accepts and validates all inputs
3. **Given** user completes profile setup, **When** the system calculates TDEE, **Then** daily calorie target is displayed and saved
4. **Given** user completes onboarding, **When** they return to the app, **Then** onboarding is skipped and they see the dashboard

---

### User Story 2 - Weight Tracking (Priority: P1)

As an athlete preparing for competition, I want to log my weight daily and view a graph of weight changes over time so that I can monitor my progress toward my target weight class and adjust my preparation strategy.

**Why this priority**: Core value proposition of the app. Weight management is critical for competition preparation. This feature delivers immediate value and can function independently of other features.

**Independent Test**: Can be fully tested by creating weight log entries across multiple days, viewing the weight history as a graph, and verifying progress calculation toward target weight displays correctly in both kg and percentage.

**Acceptance Scenarios**:

1. **Given** user is on weight tracking screen, **When** they tap "Add Weight", **Then** they can enter today's weight and save it
2. **Given** user has logged weight entries, **When** they view the weight graph, **Then** they see a line graph showing weight trend over the past week/month/3 months with date selector
3. **Given** user's current weight and target weight, **When** viewing progress, **Then** they see remaining kg to goal and percentage progress clearly displayed
4. **Given** user has no internet connection, **When** they log weight, **Then** data saves locally and syncs later
5. **Given** user attempts to log multiple weights for same day, **When** they save, **Then** the latest entry updates the previous one

---

### User Story 3 - Dashboard Overview (Priority: P1)

As a user, I want to see key information at a glance on the main screen (current weight, progress, today's calories, next training session) so that I can quickly assess my current status without navigating through multiple screens.

**Why this priority**: Dashboard is the primary entry point after onboarding. It provides navigation and context for all features. Essential for user experience and app usability.

**Independent Test**: Can be fully tested by verifying dashboard displays weight data from weight tracking, shows placeholder states when no data exists, and provides quick action buttons that navigate to correct screens.

**Acceptance Scenarios**:

1. **Given** user opens the app, **When** dashboard loads, **Then** they see current weight, progress to goal (kg and %), and mini-graph for the past week
2. **Given** user has logged meals today, **When** viewing dashboard, **Then** they see "Calories today / Target" with progress bar for protein/fats/carbs
3. **Given** user has upcoming training, **When** viewing dashboard, **Then** they see next training session card with date, type, and preview note
4. **Given** user taps quick action "Add Meal", **When** navigation occurs, **Then** they are taken to nutrition diary with date pre-filled to today
5. **Given** user taps quick action "Log Weight", **When** navigation occurs, **Then** they are taken to weight entry screen
6. **Given** user has no data yet, **When** viewing dashboard, **Then** they see empty states with helpful prompts to add first entries

---

### User Story 4 - Nutrition Diary (Priority: P2)

As an athlete managing weight, I want to search for foods, add them to my daily meal diary with portion sizes, and track my daily calorie and macronutrient intake so that I can ensure I'm meeting my nutrition targets while preparing for competition.

**Why this priority**: Second core feature for weight management. More complex than weight tracking (requires external API integration) but delivers significant value for athletes cutting or maintaining weight.

**Independent Test**: Can be fully tested by searching for foods via external nutrition API, adding foods to meals (breakfast/lunch/dinner/snack), entering portion sizes, saving custom foods locally, and viewing daily totals for calories and macros.

**Acceptance Scenarios**:

1. **Given** user is on nutrition diary screen, **When** they select a date, **Then** they see all meals logged for that day organized by meal time (breakfast, lunch, dinner, snack)
2. **Given** user taps "Add Food", **When** they search for a food by name, **Then** search results from external nutrition database are displayed
3. **Given** user selects a food from search results, **When** they specify grams and meal category, **Then** the food is added to that meal with calculated calories and macros
4. **Given** user adds foods throughout the day, **When** viewing daily summary, **Then** they see total calories, protein, fats, and carbs with progress toward daily goals
5. **Given** user frequently eats certain foods, **When** they save a food as favorite, **Then** it appears in "My Foods" list for quick access without search
6. **Given** user has no internet connection, **When** they attempt to search, **Then** they see an error message with option to retry or access saved foods
7. **Given** user wants to adjust an entry, **When** they tap a logged food, **Then** they can edit portion size or delete it

---

### User Story 5 - Training Calendar (Priority: P2)

As an athlete, I want to plan training sessions in a calendar, log details about what I practiced, and view my training history so that I can maintain a structured preparation schedule and track what techniques I've worked on.

**Why this priority**: Important for athletes who are systematically preparing for competition. Helps maintain training consistency and review past sessions. Less critical than weight/nutrition for immediate competition prep.

**Independent Test**: Can be fully tested by creating training entries with date, type (grappling/sparring/conditioning/cardio), duration, and notes, viewing calendar month view with training indicators, and accessing training history as a list.

**Acceptance Scenarios**:

1. **Given** user is on training calendar, **When** they view the month, **Then** they see a calendar with visual indicators for days with logged training
2. **Given** user taps a date, **When** they create a training session, **Then** they can specify date, training type, duration, and notes
3. **Given** user has logged training, **When** they tap on a training indicator in calendar, **Then** they see training details and can edit or delete
4. **Given** user wants to review past training, **When** they access training history, **Then** they see a list of all sessions sorted by date with filters by type
5. **Given** user logs multiple training types in one day, **When** viewing that day, **Then** all sessions are displayed separately
6. **Given** user wants to plan ahead, **When** they create a future training entry, **Then** it appears in calendar as a planned session

---

### User Story 6 - Technique Diary (Priority: P3)

As a grappling athlete learning new skills, I want to record techniques I'm studying with notes and categories so that I can build a personal reference library and avoid forgetting what I've learned.

**Why this priority**: Educational and reference feature that adds long-term value but isn't critical for immediate competition preparation. Nice-to-have for systematic skill development.

**Independent Test**: Can be fully tested by creating technique entries with name, category (takedowns, submissions, sweeps, defenses, control), description/notes, and date, filtering by category, and editing/deleting techniques.

**Acceptance Scenarios**:

1. **Given** user is on technique diary, **When** they tap "Add Technique", **Then** they can enter technique name, select category, add description/notes, and save
2. **Given** user has saved techniques, **When** they view the technique list, **Then** they see all techniques with category labels and can filter by category
3. **Given** user has many techniques, **When** they use category filter, **Then** only techniques in selected categories (takedowns, submissions, sweeps, defenses, control) are displayed
4. **Given** user taps a technique, **When** viewing details, **Then** they see full description, category, date added, and options to edit or delete
5. **Given** user wants to update notes, **When** they edit a technique, **Then** changes are saved and reflected in the list
6. **Given** user searches for a specific technique, **When** they use search, **Then** results match technique names and description text

---

### Edge Cases

- What happens when user enters target weight higher than current weight (gaining weight scenario)?
- How does the system handle weight entries with unrealistic values (e.g., 500kg or 10kg for an adult)?
- What happens when external nutrition API is unavailable or returns no results?
- How does the app handle date/time across midnight (logging late meals or early morning weigh-ins)?
- What happens when user changes their target weight mid-preparation?
- How does the system handle very long technique descriptions or notes?
- What happens when user tries to log weight for a future date?
- How does the app handle rapid weight fluctuations (multiple entries in short time)?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST allow users to create a profile with name, current weight, target weight, and weight class during onboarding
- **FR-002**: System MUST calculate daily calorie target (TDEE) based on user profile data
- **FR-003**: System MUST allow users to log weight entries with date and weight value
- **FR-004**: System MUST display weight progress as a graph with selectable time periods (week/month/3 months)
- **FR-005**: System MUST calculate and display progress toward target weight in kg and percentage
- **FR-006**: System MUST integrate with external nutrition database API to search foods by name
- **FR-007**: System MUST allow users to add foods to meal diary with portion size (grams) and meal category (breakfast/lunch/dinner/snack)
- **FR-008**: System MUST calculate and display daily totals for calories, protein, fats, and carbohydrates
- **FR-009**: System MUST allow users to save frequently used foods locally for offline access
- **FR-010**: System MUST display dashboard with weight card, nutrition card, next training card, and quick actions
- **FR-011**: System MUST allow users to create training sessions with date, type, duration, and notes
- **FR-012**: System MUST display training calendar in month view with visual indicators for logged training
- **FR-013**: System MUST provide training history list with filtering by training type
- **FR-014**: System MUST allow users to create technique entries with name, category, description, and date
- **FR-015**: System MUST categorize techniques into: takedowns, submissions, sweeps, defenses, control
- **FR-016**: System MUST persist all user data locally between app sessions
- **FR-017**: System MUST support both light and dark theme with user toggle in settings
- **FR-018**: System MUST provide settings screen for editing profile, theme selection, and data reset
- **FR-019**: System MUST handle network errors gracefully with user-friendly error messages and retry options
- **FR-020**: System MUST validate user inputs (e.g., weight must be positive number, dates cannot be future for weight logs)
- **FR-021**: System MUST cache nutrition search results locally to reduce API calls
- **FR-022**: System MUST provide navigation between all screens via bottom navigation bar

### Key Entities

- **User Profile**: Represents athlete's personal data including name, current weight, target weight, weight class, and calculated TDEE. One profile per app installation.
- **Weight Entry**: Records a weight measurement with date and weight value. Multiple entries per user, typically one per day.
- **Food Item**: Represents a food with nutritional data (calories, protein, fats, carbs per 100g). Can be from external API or user-saved custom foods.
- **Meal Entry**: Associates a food item with a specific meal time (breakfast/lunch/dinner/snack), portion size (grams), and date. Multiple per day.
- **Training Session**: Records a training event with date, type (grappling/sparring/conditioning/cardio), duration (minutes), and notes.
- **Technique**: Captures a learned technique with name, category, description/notes, and date added. Acts as personal reference library.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can complete onboarding and profile setup in under 2 minutes
- **SC-002**: Users can log a weight entry in under 15 seconds
- **SC-003**: Food search returns results in under 2 seconds when network is available
- **SC-004**: All user data persists between app sessions with 100% reliability
- **SC-005**: App navigation between screens completes in under 500ms
- **SC-006**: Weight graph renders all data points smoothly without lag
- **SC-007**: Users can add a meal entry (search + portion + save) in under 60 seconds
- **SC-008**: 90% of food searches return at least 5 relevant results
- **SC-009**: App functions offline for all features except food search, with appropriate user feedback
- **SC-010**: Dashboard loads and displays all cards in under 1 second
- **SC-011**: Users can create and save a training session in under 45 seconds
- **SC-012**: Users can create and save a technique entry in under 30 seconds
- **SC-013**: Theme switching (light/dark) applies instantly across entire app
- **SC-014**: 95% of user inputs are validated with clear error messages before submission

## Assumptions

- Users have basic familiarity with smartphones and mobile apps
- Users have internet connectivity for initial food database searches (offline mode uses cached/saved foods)
- Weight measurements are in metric (kg), aligning with international grappling competitions
- Daily calorie calculation (TDEE) uses standard formula based on current weight (specific formula to be determined during planning)
- External nutrition API provides reliable data in expected format (API selection to be determined during planning)
- Users manage their own data; no cloud sync or multi-device support in MVP
- Single user per device (no multi-user profiles in MVP)
- Competition date tracking is not included in MVP but may be added in future versions
- Push notifications for reminders are deferred to post-MVP
- Data export functionality is deferred to post-MVP
- Achievements and gamification are deferred to post-MVP

## Out of Scope for MVP

- Push notifications and reminders
- Data export (CSV, PDF)
- Cloud synchronization across devices
- Multi-user profiles on one device
- Competition countdown timer
- Social features (sharing progress, community)
- Achievements and gamification
- Photo logging (meals, physique progress)
- Integration with fitness trackers or wearables
- Meal planning or recipe suggestions
- Coach/athlete collaboration features
- Detailed training analytics and statistics beyond basic history
