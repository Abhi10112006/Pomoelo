# PomoPal 🍅✨

**A Night-Owl Friendly Android Focus Companion with an Interactive 3D Pal, Live Island Timers, Distraction Shield & Deep Screen-Time Analytics.**

Built with **Kotlin**, **Jetpack Compose**, **Material Design 3**, and **Google Filament 3D**, PomoPal is crafted especially for students, developers, creatives, and late-night learners who do their best deep work after midnight.

---

## 🌙 Built for Night Owls — The 4:00 AM to 4:00 AM Day Cycle

Most productivity apps punish late-night workers by resetting stats at midnight—splitting a 11:30 PM – 2:30 AM study block across two different days.

**PomoPal runs on a unified 4:00 AM → 4:00 AM logical day cycle** across the entire app:
- **Focus Tasks:** Per-task daily completion counters roll over cleanly at 4:00 AM.
- **History Dashboard:** Every late-night session before 4:00 AM stays grouped with your current study night.
- **App Activity & Daily Limits:** Screen-time analytics, 24-hour rhythm waves, daily app quotas, and grace passes all reset together at 4:00 AM.

---

## 🐰 Meet Judy — Your Interactive 3D Study Pal

Say hello to **Judy**, PomoPal's real-time 3D companion (`judy.glb`) who studies alongside you:
- **Native PBR 3D Rendering:** Powered by **Google Filament** (`filament-android` & `gltfio`) with custom Studio Image-Based Lighting (`JudyStudioIbl`) and tone-mapped color grading.
- **Interactive Physics & Presence:** Driven by a dedicated `JudyPhysicsEngine` with responsive motion, tactile reactions, and smart lifecycle/render throttling (`FULL`, `THROTTLED_30FPS`, `PAUSED`) so battery usage stays light while visuals stay silky smooth.

---

## ✨ Core Features

### ⏱️ Expressive Pomodoro Timer & Android 16+ Live Island Support
- **Fluid Focus & Break Cycles:** Tactile spring-animated timer transitions, customizable durations, custom sounds, and an immersive focus flow.
- **Android 16+ Native Live Updates / Island Pill:** Supports promoted ongoing live notifications (`POST_PROMOTED_NOTIFICATIONS`) for native status-bar pills and Dynamic Island-style surfaces across Android 16+ OEMs (Samsung Now Bar, OriginOS 6, Pixel Live Updates, etc.).
- **Smart Android 15 & Below Fallback:** Automatically degrades to a rich, Material 3 ongoing chronometer notification with live progress and quick controls on older Android versions.

### 🛡️ Distraction Shield (App Blocker)
- **Mindful Interception:** Accessibility-powered blocking during active focus sessions with a calm 5-second pause screen designed to break compulsive app-opening loops.
- **Strict Mode & Tactile Awareness:** Optionally keep blocking active even when the timer is paused, paired with subtle haptic awareness feedback.
- **One-Tap Presets:** Instantly protect against popular social media and gaming apps, or filter by blocked apps.

### 📊 App Activity Details & Daily App Time Limits
- **Real Screen-Time Telemetry:** Powered by Android `UsageStatsManager` & `UsageEvents` strictly aligned to the 4:00 AM cycle.
- **Modern Minimal Analytics Suite:**
  - **Hero Bento Summary** with Today vs. 7-Day Week toggle.
  - **Interactive 7-Day Capsule Bar Chart** & **24-Hour Bezier Rhythm Wave**.
  - **Top 5 Most Used Apps** with live usage share breakdown.
  - **Smart Focus Tip** suggesting realistic daily limits for your biggest unguarded distraction.
- **Per-App Daily Limits & Proactive Nudges:**
  - Interactive M3 Bottom Sheet with a **live 7-day threshold preview graph** as you drag the limit slider.
  - Automatic notifications at **50% limit used**, **5 minutes remaining**, and **1 minute remaining**.
  - Dedicated **Daily Limit Reached** lock screen featuring **"Go Study • Open PomoPal"** and a **"Just 5 mins more"** grace pass option.

### ✅ Focus Tasks & 📈 History Dashboard
- **Focus Tasks:** Editorial dashboard with an **Active Task Spotlight**, Work/Break & Category filter chips, 4:00 AM daily completion counters, smooth `animateItem` spring insertions, undoable snackbar deletions, and a **scroll-reactive bottom-left morphing navigation pill** with a subtle bottom scrim.
- **History Analytics:** Track daily focus streaks, category distributions, and session timelines aligned with your 4:00 AM night-owl schedule.

### 🔍 Flagship Pinned Search Mode & 120fps List Performance
- **Unified Pinned Top Search Mode** across **App Activity**, **Distraction Shield**, and **Focus Tasks**:
  - Anchors the search capsule at the top of the screen with `WindowInsets.navigationBars.union(WindowInsets.ime)` so the software keyboard **never hides matching results**.
  - Prefix-first smart ranking, auto-scroll to top on every keystroke, and automatic keyboard dismissal the instant you drag the results list.
- **Zero-Lag Installed App Lists:** Shared in-memory **LRU App Icon Cache**, `@Immutable` data models, and pre-indexed lowercase search keys for buttery scrolling across 100+ installed apps.

### 🎨 Material Design 3 Customization & Motion
- **Unified `PomoMotion` Physics:** Expressive spatial springs and critically-damped effects springs, predictive back gestures, and smooth drill-down transitions.
- **Custom Themes, Paper Textures & Typography:** Handcrafted light & dark themes, optional paper grain overlay, adaptive `scaledSp` / `scaledDp` responsive sizing, and curated bundled fonts (`Poppins`, `Comic Neue`, `Special Elite`, `Balsamiq Sans`, `Patrick Hand`, `Sniglet`, `Short Stack`, `Chewy`).

---

## 🗺️ Upcoming Features & Roadmap

We're actively building the next wave of delightful productivity rituals in PomoPal:

- [ ] 🎉 **Daily Goal Completion Celebration:** A joyful full-screen milestone celebration with animated 3D Judy reactions whenever you hit your daily focus target.
- [ ] 🥚 **Overachiever Easter Egg:** A secret unlockable surprise & special reaction crafted for night owls and dedicated learners who study *beyond* their daily goal!
- [ ] 🪪 **Post-Timer Work Summary & Premium Share Cards:** A clean end-of-session breakdown after each completed work timer—complete with a gorgeous, exportable summary card designed to share your focus wins with friends, study groups, and colleagues.
- [ ] 🎵 **Built-in Ambient Soundscapes:** On-device focus audio (rain, brown noise, cozy café) synced automatically with active work timers.
- [ ] 📱 **Home Screen & Lock Screen Widgets:** Glanceable 4:00 AM daily progress and one-tap timer launch right from your launcher.

---

## 🛠️ Tech Stack & Architecture

| Layer | Technologies |
| :--- | :--- |
| **Language & UI** | Kotlin, Jetpack Compose, Material Design 3, Edge-to-Edge Window Insets |
| **3D Companion Engine** | Google Filament (`filament-android`, `gltfio-android`, `filament-utils`), glTF 2.0 (`.glb`), custom Studio IBL & Physics Engine |
| **Persistence & Sync** | Jetpack Room (SQLite + KSP), Firebase Firestore / Realtime Database / Analytics, Google Sign-In |
| **Background & System APIs** | Foreground `TimerService`, `AccessibilityService` (`AppBlockerService`), `UsageStatsManager` / `UsageEvents`, Android 16 Promoted Live Updates (`LiveTimerNotificationManager`) |
| **Navigation & Motion** | Navigation Compose, Predictive Back (`PredictiveBackHandler`), Physics-Based Compose Springs (`PomoMotion`) |
| **SDK Targets** | `minSdk 24` · `targetSdk 36` · `compileSdk 36` · Java 11 |

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Meerkat / Narwhal or newer with Android 16 / API 36 SDK)
- **JDK 11+**
- Optional: A Firebase project with `google-services.json` placed in `app/` if you want cloud sync and Google Sign-In enabled.

### Build & Run
```bash
# Clone the repository
git clone https://github.com/<your-username>/PomoPal.git
cd PomoPal

# Build debug APK (fonts are fetched automatically during preBuild)
./gradlew :app:assembleDebug
```

### Recommended Permissions for Full Experience
- **Notifications (`POST_NOTIFICATIONS` / `POST_PROMOTED_NOTIFICATIONS`)**: Enables the live timer island pill and daily app-limit warning nudges.
- **Usage Access (`PACKAGE_USAGE_STATS`)**: Powers the 7-day graphs, 24-hour rhythm wave, Top 5 apps, and daily time limits.
- **Accessibility Service (`Distraction Shield`)**: Enables mindful app blocking during focus sessions and automatic locking when daily app limits are reached.

---

<p align="center">
  <i>Built for the late-night dreamers, deep thinkers, and 2 AM builders.</i> 🌙🍅✨
</p>
