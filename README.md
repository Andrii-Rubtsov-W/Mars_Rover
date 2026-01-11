# Mars Rover (Rick & Morty Characters)

An Android Jetpack Compose app for browsing Rick & Morty characters, viewing character details, episodes, and managing favorites. Includes search by name and sorting.

## Features

- Characters list with pagination
- Search characters by name (debounce ~350ms)
- Sorting by:
  - `status`
  - `species`
  - `gender`
- Character details screen:
  - main info
  - episodes list (up to 5 visible; if more — scroll)
  - episode info popup dialog
- Favorites:
  - add/remove character to Favorites
  - Favorites screen listing saved characters
- Adaptive background:
  - portrait: `font_vert`
  - landscape: `font_hor`

## Architecture

- Presentation (Compose UI + ViewModel)
- Domain (UseCases + Models)
- Data (Repository/Network/DB — depending on your project implementation)

## Tech Stack

- Kotlin
- Jetpack Compose + Material3
- Navigation Compose
- Koin (DI)
- Coroutines + Flow
- Retrofit (networking)
- Room (local storage)
- Coil (image loading)

## Requirements

- Latest Android Studio
- JDK 17
- minSdk: 26
- targetSdk: 36
- compileSdk: 36

## Run

1. Open the project in Android Studio
2. Wait for Gradle sync
3. Run the `app` configuration

## Testing

### Unit tests
Run:
- Android Studio: `StartScreenViewModelTest`, `DetailsScreenViewModelTest`, `FavoritesScreenViewModelTest` (or others)
- Gradle:
  - `./gradlew testDebugUnitTest`

Dependencies:
- `kotlinx-coroutines-test`
- `mockk`
- `junit`

### UI tests (androidTest)
Run:
- `./gradlew connectedDebugAndroidTest`

Recommendations for stable UI tests:
- Add `Modifier.testTag(...)` to key UI elements (buttons, dialogs, lists).
- Avoid calling `painterResource()` with non-vector XML in tests/preview (or provide a `LocalInspectionMode` fallback).

## Resources

- Backgrounds:
  - `res/drawable/font_vert`
  - `res/drawable/font_hor`
- Icons:
  - `ic_favorite`, `ic_chevron_right`, `ic_filter` (must be VectorDrawable or PNG/JPG/WEBP)

## Change the App Icon

1. Prepare an image (1024x1024 PNG) or an SVG.
2. Android Studio → `app` → `New` → `Image Asset`
3. Select `Launcher Icons (Adaptive and Legacy)`
4. Choose your source image
5. Generate the `mipmap-*` resources and verify `android:icon` / `android:roundIcon` in the manifest.

## Navigation

Main screens:
- StartScreen (list)
- DetailsScreen (character details)
- FavoritesScreen (favorites list)

## Notes

- Preview uses `LocalInspectionMode` to avoid dependencies on real resources/network calls.
- If Preview crashes:
  - check that `R.drawable.*` resources exist
  - ensure XML icons are true VectorDrawables
  - avoid runtime DI (Koin) in Preview or use preview-only state stubs

## License / API

Data is fetched from the public Rick and Morty API.

