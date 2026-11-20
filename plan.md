# PomoPal — Material 3 Expressive Fluid Motion Plan

A phase-by-phase blueprint to upgrade PomoPal's entire UI to the **Material 3 (M3) Expressive Motion Physics** standard (`https://m3.material.io/`), making every navigation transition, timer state change, list reveal, and interactive control physical, interruptible, and cohesive.

---

## Confirmed Design Choices

- **Motion Personality**: **M3 Expressive Scheme** — physics-based Spatial springs with subtle, natural bounce (`dampingRatio = 0.62–0.80`) paired with critically-damped Effects springs (`DampingRatioNoBouncy`) for opacity, color, and elevation.
- **Navigation & Overlays**: **Container Morph (`SharedBounds`) + Shared Axis (X / Y / Z)** — directional slides for peer destinations and tabs, paired with container morphs from originating controls into expanded overlays.
- **Micro-Interactions**:
  - Morphing Play/Pause action shapes and animated expandable pill indicators.
  - Staggered spring reveals for Task lists, History logs, and Settings cards.
  - Real-time `PredictiveBackHandler` gesture scaling, corner rounding, and translation on overlays.
  - Expressive sinusoidal wavy circular progress track during active Focus/Break sessions.

---

## Phase Roadmap

### Phase 1 — Core M3 Motion Engine & Tactile Controls *(Completed)*
- Upgraded `PomoMotion` (`MotionTokens.kt`) to M3 Expressive **Spatial** (`fastSpatial`, `defaultSpatial`, `slowSpatial`, `expressiveSpatial`) and critically-damped **Effects** (`fastEffects`, `defaultEffects`, `slowEffects`) springs with typed `IntOffset`, `IntSize`, and `Dp` visibility thresholds.
- Upgraded `horizontalSlideTransition`, `verticalCrossfadeTransition`, `expandSection`, `collapseSection`, and added `sharedAxisZTransition` and `fadeThroughTransition`.
- Upgraded `pomoBouncyClick`, `PomoButton`, and `PomoIconButton` (`PomoUI.kt`) with GPU-accelerated `graphicsLayer` press-scale springs, elevation compression, and smooth enabled/disabled alpha transitions.

### Phase 2 — App Shell, Navigation & Overlays *(Completed)*
- Enable Android Predictive Back (`android:enableOnBackInvokedCallback="true"` in `AndroidManifest.xml`).
- Upgrade `NavHost` transitions (`signin → home` Shared Axis Z depth reveal; `home ↔ history` Shared Axis X with spring `IntOffset` specs and predictive pop transitions).
- Upgrade the Floating Bottom Pill navigation bar: spring-driven enter/exit, spring `animateContentSize`, spring `expandHorizontally`/`shrinkHorizontally` active pill labels, and tactile press-squish on tab pills.
- Add `SharedTransitionLayout` container morph (`sharedBounds`) bridging the Home Settings gear button to `SettingsOverlay`.
- Add `PredictiveBackHandler` inside `SettingsOverlay` with live gesture progress scaling, rounded corner morphing, directional shift, and spring cancel recovery.

### Phase 3 — Home Screen, Focus Timer & Tasks *(Completed)*
- Upgrade `TimerDisplay` circular progress track to an M3 Expressive wavy polar arc whose wave amplitude smoothly animates in when `RUNNING` and settles flat when `PAUSED`/`STOPPED`.
- Morph `AppControls` Play/Pause button corner radius (`Circle` ↔ rounded squircle) and icon transition with Expressive springs.
- Upgrade `SlidingTimer` digit rolls to M3 Shared Axis Y springs.
- Add staggered spring reveals and selection physics to `TaskItemRow`, plus container morph & predictive back-safe lock overlay to `SeriousFullscreenOverlay`.

### Phase 4 — Settings, Customizer, App Blocker & System Protection *(Completed)*
- Replaced `Completion Sound` and `Appearance` segmented controls with sliding primary pill indicators driven by `PomoMotion.expressiveSpatial(Dp.VisibilityThreshold)`, press-squish feedback, and critically-damped text color transitions.
- Added `SharedTransitionLayout` container morph (`sharedBounds`) from the App Blocker Settings card into `BlockedAppsScreen`, paired with `PredictiveBackHandler` gesture scaling/rounding (plus search-clear back gesture) and staggered app row spring reveals.
- Upgraded `ThemeFontCustomizer` and `AppCustomizerScreen` transitions to M3 Shared Axis X springs with `PredictiveBackHandler`, animated selected-font checkmarks, and `PomoMotion` color swatch springs.
- Added staggered horizontal-safe card reveals and animated status transitions to `SystemProtectionScreen`, and unified `AboutScreen` dots, pat reactions, mood pills, and crossfades on `PomoMotion` tokens.

### Phase 5 — History Analytics, Sign-In & Modals *(Completed)*
- Upgraded `HistoryScreen` dashboard entrance, critically-damped summary counters (`PomoMotion.slowEffects()`), staggered spring reveals, `LazyColumn` `animateItem` spatial placement physics, press-squish & expressive expand chevrons on session/day/archive cards, and Shared Axis Z deletion dialogs.
- Upgraded `SignInScreen` onboarding choreography, `Interactive3DTile` tilt/press springs, Judy greeting toast, animated focus sparkle, expandable feedback banners, and `fadeThroughTransition` CTA button to `PomoMotion` tokens.
- Upgraded `AddTaskCard`, `JustInTimeSetup`, `UpdateAvailable` `AlertDialog`, and `ViewAllTasksSheet` items in `MainActivity.kt` to M3 Shared Axis Z spring entrances, expressive category pill pops, and staggered `animateItem` sheet transitions.

### Phase 6 — Edge-Grade Entry, Asymmetric Transitions & 120Hz Offloading *(Completed)*
- Added AndroidX `SplashScreen` (`Theme.PomoPal.Starting` DayNight backdrop + adaptive icon) with Room/settings readiness hold (`setKeepOnScreenCondition`) and critically damped Hold & Dissolve exit (`1.0f → 1.08f` icon scale + fade-out) synchronized with Home root Content Rise-In (`alpha: 0f → 1f`, `scale: 0.96f → 1.0f`).
- Implemented Asymmetric Fade-Through (`PomoMotion.asymmetricFadeThroughTransition()`, `asymmetricFadeThroughEnter()`, `asymmetricFadeThroughExit()`) with fast critically damped clear (`1.0f → 0.96f`) and delayed pure-depth settle (`0.95f → 1.0f`) across `Home ↔ History`, `SettingsOverlay` enter/exit, and all 5 peer `Settings` tabs, keeping `SharedAxis X` strictly for sequential drill-downs (`Sign-In`, `Make It Yours`, `Blocked Apps`).
- Enabled 120Hz GPU hardware alpha modulation (`CompositingStrategy.Offscreen` for dense overlapping surfaces and `ModulateAlpha` for flat containers) across `MainActivity`, `SettingsOverlay`, `HistoryScreen`, `BlockedAppsScreen`, `ThemeFontCustomizer`, `SystemProtectionScreen`, and `SignInScreen`.

### Phase 7 — 120fps Performance Engineering, 3D Cadence Control & Baseline Profiles *(Completed)*
- Audited and annotated all-`val` Room/UI models (`TaskItem`, `TimerSession`, `AppItem`) with `@Immutable` alongside Kotlin 2.2 Strong Skipping, and deferred `TimerDisplay` wavy phase/amplitude/color and `PredictiveBackHandler` progress reads strictly into draw-phase `Canvas`, `drawBehind`, and `graphicsLayer` lambdas.
- Cached a single remembered `Path` (`wavePath.reset()`) in `TimerDisplay` with `2.5°` angular stepping to eliminate per-frame GC allocations at 120Hz.
- Added non-static `LocalMascotRenderMode` (`compositionLocalOf`) to `PomoPalMascot` keeping `TextureView` swapchain alive: unregisters `Choreographer` cleanly on `PAUSED` (zero 120Hz wakeups), resets `lastRenderNanos = 0L` and frame delta anchoring on resume, and gates `THROTTLED_30FPS` via refresh-rate-independent nanosecond intervals (`>= 33_333_333L` ns) during Predictive Back drags.
- Memoized `HistoryScreen` and `BlockedAppsScreen` list groupings/filters on strictly narrow raw-data keys and declared explicit `key` + `contentType` across all `LazyColumn` sections and rows.
- Bundled `androidx.profileinstaller:profileinstaller:1.4.1` with ART wildcard rules in `app/src/main/baseline-prof.txt` for automatic AGP AOT packaging (`assets/dexopt/baseline.prof`).


