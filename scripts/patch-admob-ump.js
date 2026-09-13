const fs = require('fs');
const path = require('path');
const podspec = path.join(
  __dirname,
  '..',
  'node_modules',
  '@capacitor-community',
  'admob',
  'CapacitorCommunityAdmob.podspec'
);
if (!fs.existsSync(podspec)) {
  console.warn('patch-admob-ump: podspec missing, skip');
  process.exit(0);
}
let t = fs.readFileSync(podspec, 'utf8');
if (t.includes("GoogleUserMessagingPlatform', '>= 2.7.0', '< 3.0.0'")) {
  console.log('patch-admob-ump: already pinned');
  process.exit(0);
}
const next = t.replace(
  /s\.dependency ['"]Google-Mobile-Ads-SDK['"].*$/m,
  (line) =>
    line +
    "\n  s.dependency 'GoogleUserMessagingPlatform', '>= 2.7.0', '< 3.0.0'"
);
if (next === t) {
  // try adding after Capacitor dependency
  const alt = t.replace(
    /s\.dependency ['"]Capacitor['"].*\n/,
    (m) => m + "  s.dependency 'GoogleUserMessagingPlatform', '>= 2.7.0', '< 3.0.0'\n"
  );
  fs.writeFileSync(podspec, alt);
} else {
  fs.writeFileSync(podspec, next);
}
console.log('patch-admob-ump: pinned GoogleUserMessagingPlatform < 3');
