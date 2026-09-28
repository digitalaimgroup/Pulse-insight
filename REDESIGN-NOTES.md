# PULSE Insight — Visual Redesign

Gameplay rules, scoring, Pulse drain, level tuning, powers, AdMob units and music controls are unchanged.

## What's new
- **Splash screens:** animated boot splash (block-heart logo drops in, ECG line draws, loader), new title screen with floating blocks, first-run 4-step "How to play" tutorial, READY?/GO! countdown, full-screen LEVEL UP celebration, redesigned Pause/Settings and Game Over cards (score count-up, stats, NEW BEST confetti).
- **Candy blocks:** 8 glossy beveled colors; each piece gets its own color.
- **Juice:** particles on every clear, line-sweep flashes, praise text (GREAT! / EXCELLENT! / AMAZING! / UNBELIEVABLE!), combo flame chip, floating +points, screen shake, placement pop, board grey-out on game over, red danger vignette at low Pulse.
- **Smarter placing:** dragged pieces lift above your finger; lines that will clear glow in the piece's color before you drop; invalid drops fly back to the tray; tray pieces that can't fit turn grey.
- **Sound FX + haptics:** procedural SFX (place, clear arpeggios rising with combo, boom, level fanfare, flatline beep); new music loop that speeds up with Level; native Capacitor Haptics. Settings toggles for Music, Sound FX, Vibration.
- **New app icon + native splash images** (Android all densities, adaptive icon, iOS).
- Fonts bundled offline: Lilita One + Nunito (SIL OFL, licences in www/fonts).

## Small fixes
- Level-up no longer checks for "no moves" while cleared lines are still on the board.
- "Watch ad · continue" after an OUT OF MOVES now hands you small pieces that fit, so the revive is usable.
- Android back button: closes menus, pauses mid-game.
- Leaving the app mid-game opens the Pause menu.

## To ship
    npm ci
    npx cap sync          # copies www into android + ios
    # Android: build in Android Studio / gradle as before
    # iOS: push to main -> GitHub Action uploads to TestFlight
Old builds in /artifacts and /docs/builds are the previous version.
Tip: add `?debug` to the URL in a browser to expose a test hook (window.__pulse); it's inert otherwise.
