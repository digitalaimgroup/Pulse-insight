#!/usr/bin/env bash
# Fail if iOS deployment target is below 15.0 (ITMS-90068).
set -euo pipefail
MIN=15.0
ROOT="${1:-.}"
PBX="$ROOT/ios/App/App.xcodeproj/project.pbxproj"
POD="$ROOT/ios/App/Podfile"
fail=0
if [[ -f "$PBX" ]]; then
  if grep -E "IPHONEOS_DEPLOYMENT_TARGET = (1[0-4]|[0-9])\." "$PBX"; then
    echo "ERROR: $PBX has IPHONEOS_DEPLOYMENT_TARGET below 15.0 (ITMS-90068)" >&2
    fail=1
  fi
fi
if [[ -f "$POD" ]]; then
  if grep -E "platform :ios, '(1[0-4]|[0-9])\." "$POD"; then
    echo "ERROR: $POD platform below 15.0 (ITMS-90068)" >&2
    fail=1
  fi
fi
if [[ $fail -ne 0 ]]; then
  exit 1
fi
echo "OK: iOS deployment target >= 15.0"

