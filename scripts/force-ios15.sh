#!/usr/bin/env bash
# Rewrite Capacitor iOS targets to 15.0 after `npx cap sync ios`.
set -euo pipefail
MIN=15.0
ROOT="${1:-.}"
POD="$ROOT/ios/App/Podfile"
PBX="$ROOT/ios/App/App.xcodeproj/project.pbxproj"
if [[ -f "$POD" ]]; then
  if [[ "$(uname)" == Darwin ]]; then
    sed -i '' "s/platform :ios, '[0-9.]*'/platform :ios, '${MIN}'/" "$POD"
  else
    sed -i "s/platform :ios, '[0-9.]*'/platform :ios, '${MIN}'/" "$POD"
  fi
fi
if [[ -f "$PBX" ]]; then
  if [[ "$(uname)" == Darwin ]]; then
    sed -i '' "s/IPHONEOS_DEPLOYMENT_TARGET = [0-9.]*;/IPHONEOS_DEPLOYMENT_TARGET = ${MIN};/g" "$PBX"
  else
    sed -i "s/IPHONEOS_DEPLOYMENT_TARGET = [0-9.]*;/IPHONEOS_DEPLOYMENT_TARGET = ${MIN};/g" "$PBX"
  fi
fi
"$ROOT/scripts/enforce-ios15.sh" "$ROOT"
