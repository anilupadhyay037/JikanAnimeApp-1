# Jikan Anime App 🎌

An offline-first Android application built using the **Jikan API** to display popular anime series.  
The app follows **MVVM architecture** and uses modern Android libraries such as **Retrofit, Room, WorkManager, and Glide**.

---

## 📱 Features Implemented

### Anime List (Home Screen)
- Fetches **Top Anime** from Jikan API
- Displays:
  - Title
  - Number of Episodes
  - Rating (MyAnimeList score)
  - Poster Image
- RecyclerView backed by **Room database**
- Cached data shown instantly

### Anime Detail Screen
- Displays:
  - Title
  - Synopsis
  - Genres
  - Episodes count
  - Rating
- Poster image reused from cached list data
- Trailer handling:
  - YouTube trailers open externally (YouTube app / browser)
  - Poster image shown when trailer is unavailable

### Offline Support
- App works **without internet**
- Room acts as the **single source of truth**
- Cached list and details remain accessible

### Data Syncing
- Uses **WorkManager** with network constraints
- Automatically syncs data when device comes online
- Background-safe and battery-efficient

### Error Handling
- API failures fall back to cached data
- Graceful handling for:
  - Missing trailers
  - Null episodes / ratings
  - Network failures
- No crashes due to null or missing fields

---

## Architecture & Libraries

### Architecture
- MVVM (Model-View-ViewModel)
- Repository pattern
- Offline-first approach

### Libraries Used
- **Retrofit** – API communication
- **Room** – Local database
- **WorkManager** – Background data sync
- **Glide** – Image loading
- **StateFlow / LiveData** – Reactive UI updates
- **Material Components** – UI elements (Chips, etc.)

---

##  Assumptions Made

- Jikan API responses may include nullable fields
- YouTube trailers cannot be played directly via ExoPlayer
- Poster images from list API are sufficient for detail screen
- App prioritizes data availability over real-time freshness
- No authentication or user personalization required

---

## Known Limitations

- No in-app video playback for trailers (external YouTube only)
- Pagination not implemented
- No search or filtering
- XML-based UI only (no Jetpack Compose)
- No automated tests included

---

## 🔗 API References

- Top Anime  
  https://api.jikan.moe/v4/top/anime

- Anime Details  
  https://api.jikan.moe/v4/anime/{id}

---

## How to Run

1. Clone the repository
2. Open in **Android Studio**
3. Sync Gradle
4. Run on emulator or physical device (API 21+)

---

## Technical Highlights

- Centralized constants to avoid hard-coding
- Network connectivity check before API calls
- Background sync independent of UI lifecycle
- Null-safe Kotlin code throughout
- Clean separation of concerns

---

## Future Improvements

- Paging support
- Search and filtering by genre
- Jetpack Compose migration
- Unit & UI tests
- Sync status indicators
- Better error UI states

---

## License

This project uses the public **Jikan API** and is intended for demonstration purposes.
