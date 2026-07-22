# News App

Sample Android app (Kotlin + Jetpack Compose) that fetches top headlines from [NewsAPI.org](https://newsapi.org) and displays them as a list or grid, toggled from the top bar.

## Setup

1. Get a free API key from [newsapi.org](https://newsapi.org/register).
2. Open (or create) `local.properties` in the project root and add:

   ```properties
   NEWS_API_KEY=your_api_key_here
   ```

   This file is git-ignored — the key is never committed. It's read at build time and exposed as `BuildConfig.NEWS_API_KEY`.
3. Sync Gradle and run.

## Architecture

Clean Architecture, three layers:

- **domain** — `Article` model, `NewsRepository` interface, `GetTopHeadlinesUseCase`, `AppError`/`AppResult` for typed error handling. Pure Kotlin, no Android dependencies.
- **data** — Retrofit + kotlinx.serialization DTOs, mapper to domain models, `NewsRepositoryImpl` (catches network/HTTP/serialization exceptions and normalizes them to `AppError`).
- **presentation / ui** — `NewsViewModel` + `NewsUiState`, and a stateless `NewsScreen` composable that switches between `LazyColumn` (list) and `LazyVerticalGrid` (grid).

Dependency injection via Hilt (`di/` package). Errors flow as `AppResult<T>` (`Success`/`Error`) rather than exceptions crossing layer boundaries.

## Testing

- **Unit tests** (`app/src/test`) — use case, mapper, and ViewModel, using fakes (`FakeNewsRepository`, `FakeDispatcherProvider`) instead of mocks.
- **Integration tests** (`app/src/test`) — `NewsRepositoryImplIntegrationTest` runs the real Retrofit/OkHttp/serialization stack against MockWebServer to verify actual JSON parsing and error mapping.
- **UI tests** (`app/src/androidTest`) — `NewsScreenTest` exercises the stateless `NewsScreen` composable directly (loading, error + retry, list/grid layout, view toggle, article click) — no ViewModel or DI graph needed.

All unit and integration tests run on the JVM (`./gradlew test`); UI tests need a device/emulator (`./gradlew connectedAndroidTest`).
