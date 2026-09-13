# PULSE Insight — iOS TestFlight checklist

Bundle ID: `com.digitalaimgroup.pulseinsight`  
App name: **PULSE Insight**

## You need
1. Active **Apple Developer Program** membership ($99/year) — enroll in the Apple Developer app
2. A **Mac** with Xcode 15+ (App Store)
3. This repo cloned locally

## 1. After Apple approves your membership
1. Open [App Store Connect](https://appstoreconnect.apple.com) → **My Apps** → **+** → **New App**
2. Platforms: **iOS**
3. Name: `PULSE Insight`
4. Bundle ID: register `com.digitalaimgroup.pulseinsight` in [Certificates, Identifiers & Profiles](https://developer.apple.com/account/resources/identifiers/list) if it isn’t listed, then select it
5. SKU: e.g. `pulseinsight-ios`
6. User Access: Full Access

## 2. AdMob for iOS (do before shipping ads)
Android already has an AdMob app ID. Create an **iOS** app in AdMob for PULSE Insight, then:
1. Put the iOS App ID (`ca-app-pub-xxxx~yyyy`) into `ios/App/App/Info.plist` → `GADApplicationIdentifier` (replace `REPLACE_WITH_IOS_ADMOB_APP_ID`)
2. Create iOS rewarded + interstitial units and update the ad unit IDs in `www/index.html` if they differ from Android
3. Run `npx cap sync ios`

Until then, the game can still use Google **test** ad unit IDs on device.

## 3. Build on your Mac
```bash
git clone https://github.com/digitalaimgroup/Pulse-insight.git
cd Pulse-insight
npm install
npx cap sync ios
cd ios/App
pod install
open App.xcworkspace
```

In Xcode:
1. Select the **App** target → **Signing & Capabilities**
2. Team: your Apple Developer team (Automatic Signing ON)
3. Bundle Identifier must stay `com.digitalaimgroup.pulseinsight`
4. Destination: **Any iOS Device (arm64)**
5. Product → **Archive**
6. Organizer → **Distribute App** → **App Store Connect** → Upload

## 4. TestFlight
1. App Store Connect → your app → **TestFlight**
2. Wait for processing
3. Answer export compliance if asked (this app sets `ITSAppUsesNonExemptEncryption` = NO for standard HTTPS)
4. **Internal Testing**: add yourself / up to 100 App Store Connect users — no Beta App Review needed
5. Testers install the free **TestFlight** app and accept the invite

## 5. External testers (optional)
First external build may need Beta App Review. Then invite by email or public link (up to 10,000).

## Notes
- Phone UI is **portrait**; iPad still allows multiple orientations in Info.plist
- Cursor Cloud Agents / Mac pools are not available on the current plan — archive must be done on a Mac with Xcode
