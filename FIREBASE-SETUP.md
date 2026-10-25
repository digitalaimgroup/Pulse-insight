# Firebase Analytics (PULSE Insight)

Firebase project: `pulse-insight-52c73`. Purpose: native `first_open` / `session_start` events so Google Ads
app campaigns can optimize for first opens instead of raw installs. Custom event: `game_start` (in `startGame()`).

Plugin: `@capacitor-firebase/analytics` 7.x (Capacitor 7). Android pulls `com.google.firebase:firebase-analytics`
through the plugin; the `com.google.gms.google-services` Gradle plugin is applied when `android/app/google-services.json` exists.

## Config files are NOT committed

| File | Local path | CI secret (base64 of the file) |
|---|---|---|
| Android | `android/app/google-services.json` | `GOOGLE_SERVICES_JSON_BASE64` |
| iOS | `ios/App/App/GoogleService-Info.plist` | `GOOGLE_SERVICE_INFO_PLIST_BASE64` |

Both are in `.gitignore`. Make the secret value with `base64 -w0 <file>` (Linux) or `base64 -i <file>` (macOS).

- `ios-testflight.yml` writes the plist from `GOOGLE_SERVICE_INFO_PLIST_BASE64` and **fails** if the secret is
  missing (Firebase calls `FirebaseApp.configure()` at launch, so a build without the plist would crash).
  The plist is already referenced in the Xcode project (App target, Copy Bundle Resources).
- Android release tasks (`bundleRelease`/`assembleRelease`) fail if `google-services.json` is missing, so a
  release never ships without analytics. Override with `-PallowNoFirebase`. Debug builds tolerate absence.
- Android CI step (if/when an Android workflow is added; keep `npx cap sync android`, never `cap add`):

```yaml
      - name: Write google-services.json from secret
        env:
          GOOGLE_SERVICES_JSON_BASE64: ${{ secrets.GOOGLE_SERVICES_JSON_BASE64 }}
        run: |
          set -euo pipefail
          test -n "${GOOGLE_SERVICES_JSON_BASE64:-}" || { echo "::error::Missing GOOGLE_SERVICES_JSON_BASE64"; exit 1; }
          echo "$GOOGLE_SERVICES_JSON_BASE64" | base64 --decode > android/app/google-services.json
```

## iOS / ATT / IDFA

The Podfile uses `CapacitorFirebaseAnalytics/AnalyticsWithoutAdIdSupport` (outside `capacitor_pods`, which
`cap sync` rewrites). Firebase never collects IDFA; the existing ATT-before-AdMob flow is unchanged.
App Store privacy labels: add "Product Interaction" + "Device ID" (app instance ID) under Analytics, not linked
to identity, not used for tracking. Play Data safety: App activity / App interactions + Device or other IDs
(Analytics), collected, not shared for tracking beyond Google as processor.
