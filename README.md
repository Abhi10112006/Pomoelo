# PomoPal 🍅 — Immersive, Gamified Pomodoro Companion

PomoPal is a highly polished, premium Android Pomodoro companion designed to turn deep focus from a chore into a delightful, rewarding ritual. Built using **modern Jetpack Compose** and **Material Design 3**, PomoPal combines a cozy aesthetic with physical gamification, bringing premium UI smoothness and innovative sensor bindings to your productivity routine.

---

## ✨ Outstanding Features & Mechanics

### 1. Adaptive, Multi-Device Intelligent Scaling
No more stretched layouts or microscopic text. PomoPal utilizes a custom-engineered responsive scaling engine:
- **`scaledSp` Text Extension**: Dynamically adapts font sizes relative to the device width, capping the range so headings and body elements look proportional on every screen from narrow compact phones up to wide tablets and foldables.
- **`scaledDp` Density modifier**: Calibrates layout spacings, paddings, and card dimensions on-the-fly, ensuring that structural proportions remain aesthetically consistent regardless of the window size class.

### 2. Physical Gamification: Squat-to-Disarm (Beta Sensor API) 🏋️
Say goodbye to mindlessly clicking "Dismiss" on your break alarms. PomoPal introduces a physical checkpoint:
- **Core Accelerometer Integration**: Leverages `SquatSensorService` which reads live 3D sensor data via the phone's accelerometer hardware. By tracking specific vertical acceleration differentials (crossing standard down/up gravity thresholds), the application counts physical squats in real-time.
- **Fitness Lock Loop**: Choose your target squat count (0 to 50 physical repetitions). The persistent focus completion alarm *only* silences when you complete your movement, encouraging healthy body activation during break blocks!

### 3. Premium Font Suite (Google Fonts Cloud Integration)
PomoPal overrides standard, generic system fonts by fetching highly curated typographies asynchronously:
- **Comic Neue Theme** (`CursiveFontFamily`): Infuses the visual interface with a friendly, welcoming warmth.
- **Special Elite Monospace** (`MonospaceFontFamily`): Emulates a retro, mechanical typewriter feel for system logs, status text fields, and time countdowns.
- **Poppins** (`AppFontFamily`): A modern, clean geometric typeface that keeps standard navigation, task lists, and settings highly readable.

### 4. Organic UI Mechanics & Spring Physics
Every transition in PomoPal is fine-tuned to feel extremely fluid and tangible to the touch:
- **Spring Physics**: Swapped out rigid linear animations for custom Jetpack Compose springs (`Spring.DampingRatioMediumBouncy` and `StiffnessLow`), introducing elegant bounciness on sliding timer numbers, settings overlays, and immersive state switches.
- **Floating Bottom Pill Navigation**: A gorgeous, haptic-enhanced floating bottom sheet with tab containers that expand dynamically depending on navigation states.
- **Breathing Pulse FX**: The active quote card features a slow, rhythmic sine-wave opacity and scaling cycle, guiding you into a calm diaphragmatic breathing tempo.

### 5. Immersive "Serious Mode" Screen Overlay
Struggling with focus leaks? Trigger the full-screen Lockdown Overlay:
- Requires **75% of the focus block to elapse** before the exit authorization button is unlocked.
- Features a real-time progress tracker showing visual feedback and a dynamic ambient dark background cycle to completely block out distracting UI details.

---

## 🛠️ Tech Stack & Architecture

- **UI Framework**: Modern declarative Jetpack Compose
- **Design Language**: Material Design 3 (M3)
- **Local Database**: SQLite persistent tracking powered by Jetpack Room
- **Motion System**: Physics-Based Jetpack Compose Animations and transition specs
- **Service Stack**: Persistent background tracking via Android `ForegroundService`, ensuring timers run accurately even when screen utility is dimmed
- **Core APIs**: `SensorEventListener` bound with `Sensor.TYPE_ACCELEROMETER` for hardware-driven squat tracking
- **Dynamic Fonts**: `androidx.compose.ui.text.googlefonts` loaded dynamically with custom GMS certificates

---

## ⚡ Future Updates & Roadmap

We are continuously evolving PomoPal. Here is what is on the immediate horizon:
- [ ] **Squat History Integration**: Save and graph your completed squats over time directly in the History tab.
- [ ] **Adaptive Smart Break Adjuster**: Algorithms that adjust subsequent break durations based on physical squat counts under heavy cognitive loads.
- [ ] **Dual-Sensor Calibration**: Merging Gyroscope data with Accelerometer signals to filter out accidental steps or false-positives while squatting.
- [ ] **Soundscape Synth**: Directly synthesize calming brown noise and bilateral beats on-device.

---

*Stay present, keep moving, and let PomoPal guide your focus.* 🍅
