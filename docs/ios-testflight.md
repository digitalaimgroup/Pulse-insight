# PULSE Insight — iOS TestFlight (no Mac required)

## Automated path (GitHub Actions)

Push to `main` (or run **Actions → iOS TestFlight → Run workflow**).

The macOS runner:
1. `npm ci` + `npx cap sync ios` + CocoaPods
2. Fastlane Match (certs in private `pulseinsight-certs` repo)
3. Archive + upload to TestFlight via App Store Connect API key

### Required GitHub secrets
- `APPLE_KEY_ID`, `APPLE_ISSUER_ID`, `APPLE_KEY_CONTENT` (base64 `.p8`)
- `MATCH_PASSWORD`, `MATCH_GIT_URL`, `MATCH_GIT_BASIC_AUTHORIZATION`

### App Store Connect
- App: PULSE Insight (Apple ID `6811510320`)
- Bundle: `com.digitalaimgroup.pulseinsight`
- Team: `MBVD3UJLM2`

After processing finishes in App Store Connect → TestFlight, add yourself as an Internal Tester and install via the TestFlight app.

AdMob iOS currently uses Google’s **test** App ID in `Info.plist` until a real iOS AdMob app ID is set.
