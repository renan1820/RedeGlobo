<div align="center">
  <h1>📺 Globeplay Android</h1>
  <p>Portfolio app demonstrating enterprise-grade Android architecture for Rede Globo technical interview</p>

  ![Build](https://github.com/renan1820/RedeGlobo/actions/workflows/ci.yml/badge.svg)
  ![Kotlin](https://img.shields.io/badge/Kotlin-2.1.20-7F52FF?logo=kotlin)
  ![API](https://img.shields.io/badge/API-24%2B-brightgreen)
  ![License](https://img.shields.io/badge/License-MIT-blue)
</div>

---

## Overview

A streaming catalog app built with the same architectural pillars used in production at scale —
**MVVM + Clean Architecture**, **Apollo GraphQL**, **RxJava 3**, **Hilt**, and **Jetpack Compose**
for both **Mobile** and **Android TV** via product flavors.

Data is sourced from the [Rick and Morty GraphQL API](https://rickandmortyapi.com/graphql) as a
streaming catalog simulation — characters become titles, episodes become episodes, locations become
categories.

---
## 📱 Demonstração do Projeto

Para visualizar o funcionamento do sistema em ambas as plataformas, confira os vídeos abaixo:

### 📺 Android TV
<video src="https://github.com/user-attachments/assets/ab47982d-630f-47df-adc4-94a967fa9d23" width="800" controls></video>

### 📱 Mobile Android
<video src="https://github.com/user-attachments/assets/ac3f0d82-8459-4fa6-b9ee-78cab8eee4d9" width="300" controls></video>

## Architecture

```
app  ──►  feature-*  ──►  core-domain  ──►  core-common
              │                │
              │         data-content  ──►  core-network
              │                │                │
              └────────────────┴──►  core-database
```

The project follows **Clean Architecture** with three explicit layers:

| Layer | Module | Responsibility |
|---|---|---|
| **Presentation** | `feature-home`, `feature-detail`, `feature-search` | Compose UI + ViewModel + UiState |
| **Domain** | `core-domain` | Models, Repository interfaces, Use Cases |
| **Data** | `data-content`, `core-network`, `core-database` | API, Room, Mappers, RepositoryImpl |

### Key architectural decisions

**Offline-First with RxJava 3 cache-then-network:**
```kotlin
Observable.concat(
    localDataSource.getCachedContentRails().toObservable(),   // instant cache
    remoteDataSource.getContentRails(page)
        .doOnSuccess { localDataSource.cacheContentRails(it).subscribe() }
        .toObservable()
)
.distinctUntilChanged()
.onErrorResumeNext { _: Throwable -> localDataSource.getCachedContentRails().toObservable() }
```

**BehaviorSubject → StateFlow bridge (RxJava + Compose):**
```kotlin
private val currentPage = BehaviorSubject.createDefault(1)

init {
    currentPage
        .switchMap { page -> getContentRailsUseCase.execute(Params(page)).startWithItem(Loading) }
        .subscribe { state -> _uiState.value = state }
        .addTo(disposables)
}
```

**Debounced search (300ms) with PublishSubject:**
```kotlin
querySubject
    .debounce(300, TimeUnit.MILLISECONDS)
    .distinctUntilChanged()
    .filter { it.isNotBlank() }
    .switchMap { searchContentUseCase.execute(Params(it)) }
    .subscribe { _uiState.value = it }
```

---

## Tech Stack

| Category | Library | Version |
|---|---|---|
| Language | Kotlin | 2.1.20 |
| Build | AGP + KSP | 9.1.1 / 2.1.20-1.0.32 |
| UI | Jetpack Compose BOM | 2025.04.00 |
| TV UI | Compose for TV | 1.0.0 |
| DI | Hilt (Dagger 2) | 2.56.1 |
| GraphQL | Apollo Kotlin | 4.1.0 |
| Reactive | RxJava 3 + RxKotlin + RxAndroid | 3.1.10 |
| Local DB | Room + RxJava3 | 2.7.1 |
| Images | Coil | 2.7.0 |
| Navigation | Navigation Compose | 2.9.0 |
| Lifecycle | ViewModel + Runtime Compose | 2.9.0 |
| Testing | JUnit4 + MockK + Truth + Turbine | 4.13.2 / 1.14.2 / 1.4.4 / 1.2.0 |

---

## Module Structure

```
RedeGlobo/
├── app/                        # Orchestrator: Application, MainActivity, NavGraph
│   ├── src/mobile/java/        # GloboAppContent — Material3 + BottomNav
│   └── src/tv/java/            # GloboAppContent — TV Surface + D-pad nav
│
├── core/
│   ├── core-common/            # Extensions and shared utilities
│   ├── core-domain/            # Models, Repository interfaces, Use Cases (pure Kotlin)
│   ├── core-network/           # ApolloClient, GraphQL queries, NetworkModule
│   ├── core-database/          # Room, DAOs, Entities, DatabaseModule
│   └── core-ui/                # Compose Design System, RedeGloboTheme, Typography
│
├── data/
│   └── data-content/           # RepositoryImpl, Remote/Local DataSources, Mappers
│
└── feature/
    ├── feature-home/           # HomeViewModel, HomeScreenMobile, HomeScreenTv
    ├── feature-detail/         # DetailViewModel, DetailScreen (episodes + watchlist)
    └── feature-search/         # SearchViewModel (debounce), SearchScreen
```

---

## Product Flavors — Mobile vs TV

A single codebase ships two distinct apps via Gradle product flavors:

```kotlin
flavorDimensions += "platform"

productFlavors {
    create("mobile") {
        applicationIdSuffix = ".mobile"
        buildConfigField("Boolean", "IS_TV", "false")
    }
    create("tv") {
        applicationIdSuffix = ".tv"
        buildConfigField("Boolean", "IS_TV", "true")
    }
}
```

| | Mobile | TV |
|---|---|---|
| **Application ID** | `…redeglobo.mobile` | `…redeglobo.tv` |
| **Navigation** | Bottom Navigation Bar | D-pad focus + TV Surface |
| **UI Library** | Material 3 Compose | `androidx.tv:tv-material` |
| **ViewModel** | Shared | Shared |
| **Use Cases** | Shared | Shared |

---

## Screens

### Home
Displays content rails from the GraphQL API: one rail **"Em Alta"** (top 10) and one **"Todos os Personagens"** (full catalog). Supports pagination via `loadNextPage()`.

### Detail
Full character/title detail page with episode list and watchlist toggle. Uses `SavedStateHandle` to receive `contentId` from navigation.

### Search
Real-time search with 300ms debounce. States: `Idle → Loading → Success / Empty / Error`. Pressing back or clearing the field returns to `Idle`.

---

## GraphQL Queries

Located at `core/core-network/src/main/graphql/`:

| File | Purpose |
|---|---|
| `HomeContentRailsQuery.graphql` | Paginated character list for content rails |
| `ContentDetailQuery.graphql` | Full character details + episodes |
| `SearchContentQuery.graphql` | Filtered search by name |
| `EpisodesQuery.graphql` | Episode listing with character appearances |

Apollo generates type-safe Kotlin models at compile time via KSP.

---

## Testing

```bash
./gradlew test
```

| Test class | Module | Coverage focus |
|---|---|---|
| `GetContentRailsUseCaseTest` | core-domain | Success, error propagation, pagination params |
| `SearchContentUseCaseTest` | core-domain | Query passthrough, empty results, error |
| `ContentRepositoryImplTest` | data-content | Cache-first, caching side-effect, fallback |
| `HomeViewModelTest` | feature-home | Loading→Success, error state, TestScheduler |
| `SearchViewModelTest` | feature-search | Debounce timing, Idle/Empty/Error states |

**Testing patterns:**
- `RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }` — replaces `AndroidSchedulers.mainThread()` in JVM tests
- `TestScheduler.triggerActions()` — controls emission timing deterministically
- `MockK` for interface mocking + `Truth` for readable assertions

---

## CI/CD

GitHub Actions pipeline (`.github/workflows/ci.yml`) runs on every push to `main`/`develop`:

```
Checkout → JDK 17 → assembleMobileDebug → assembleTvDebug → testMobileDebugUnitTest
```

Artifacts uploaded: `app-mobile-debug.apk`, `app-tv-debug.apk`, test results XML.

---

## Running Locally

**Requirements:** Android Studio Ladybug+, JDK 17, Android SDK 36

```bash
# Clone
git clone https://github.com/renan1820/RedeGlobo.git
cd RedeGlobo

# Build mobile APK
./gradlew assembleMobileDebug

# Build TV APK
./gradlew assembleTvDebug

# Run unit tests
./gradlew test

# Install on connected device/emulator
./gradlew installMobileDebug
```

> No API key needed — the Rick and Morty API is public and free.

---

## Project Highlights for Interview

| Topic | What to Show |
|---|---|
| **Offline-First** | `ContentRepositoryImpl` — `Observable.concat` + `distinctUntilChanged` |
| **Reactive ViewModel** | `HomeViewModel` — `BehaviorSubject` driving `StateFlow` |
| **Search UX** | `SearchViewModel` — `PublishSubject` debounce 300ms |
| **Platform Abstraction** | `AppContent.kt` — same route graph, different Composables per flavor |
| **Type-safe Navigation** | `NavigationDestination` sealed class with `createRoute()` |
| **Test Determinism** | `HomeViewModelTest` — `TestScheduler.triggerActions()` |
| **Apollo 4.x** | Coroutines-RxJava bridge via `kotlinx-coroutines-rx3` |

---

<div align="center">
  <sub>Built with ❤️ as a technical portfolio for Rede Globo — 2025</sub>
</div>
