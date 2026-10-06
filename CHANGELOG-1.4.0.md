# PULSE Insight 1.4.0: tester feedback update

Version: Android versionCode 9 / versionName 1.4.0 · iOS MARKETING_VERSION 1.4.0 (fastlane sets the build number).

## Tester requests, all done
| Request | What shipped |
|---|---|
| ASO description | store/STORE-LISTING.md: names, short/full descriptions, subtitle, keywords, promo text, What's New (all within store limits) |
| Better store screenshots | store/screenshots/: 7 captioned images each for Google Play (1080x1920), iPhone 6.9" (1320x2868), iPad 13" (2064x2752) |
| "Rate your app" button | Settings > Support > Rate PULSE Insight (opens the store review page) |
| Rate prompt | "Having fun?" card on returning to the title after a 5th game or a new best level of 4+. At most 3 asks, 4+ days apart, never after rating. Same prompt for everyone (no review gating) |
| In-app review API | Android: Google Play In-App Review via the new PulseNativePlugin. iOS: opens the App Store write-review page |
| Daily challenges | 3 new seeded challenges every day (easy/medium/hard, 10 challenge types), progress bars, reset timer |
| Rewards | +1 Boost per challenge, bonus Boost for all 3. A Boost starts a game with +15 Pulse and a free Surge (toggle in Daily) |
| Leaderboard | Daily bests board: today / 7 days / all-time / games today + 7-day bar chart (saved on the device) |
| Tutorial | Already included; new 5th step explains Daily challenges |
| Social sharing | Share button on Game Over, Daily screen and Settings (Android share sheet, iOS share sheet, clipboard fallback) |
| Seasonal updates | Themes: Auto (seasonal), Classic, Neon Night, Spooky. Auto turns on Halloween every October. Add more in SEASONS in index.html |
| Feedback mechanism | Settings > Support > Send feedback: Bug/Idea/Other form that opens email to digitalaimgroup@gmail.com with version + device info attached |
| Accessibility | Colorblind symbols on every block color, Reduce Motion toggle (also follows the phone's setting), visible keyboard focus |

## Files changed
- www/index.html (all game-side features), www/sw.js (cache v9), www/screenshot-phone-*.png
- android/app/src/main/java/com/digitalaimgroup/pulseinsight/PulseNativePlugin.java (new)
- android/app/src/main/java/com/digitalaimgroup/pulseinsight/MainActivity.java (registers the plugin)
- android/app/build.gradle (version + com.google.android.play:review:2.0.2)
- android/app/src/main/assets/public/* (synced web build)
- ios/App/App.xcodeproj/project.pbxproj (version 1.4.0)
- store/ (listing text + screenshots)

No new npm packages, so package.json / package-lock.json are unchanged and `npm ci` in CI still works.

## Global leaderboard (not included)
The leaderboard is per-device. A worldwide board needs Game Center (iOS) and Google Play Games (Android) set up in both developer consoles first. It can be added in a later update.

## Ship it
    git add -A && git commit -m "v1.4.0: daily challenges, rating, sharing, feedback, themes, accessibility"
    git push origin main          # GitHub Action uploads the iOS build to TestFlight
    npx cap sync android          # then build the signed AAB in Android Studio and upload to Play Console
Then paste the store text from store/STORE-LISTING.md and upload store/screenshots/ in both consoles.
