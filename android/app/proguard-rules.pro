# PULSE Insight R8 rules (release: minifyEnabled + shrinkResources).

# Keep line numbers for readable crash traces (pair with mapping.txt on Play).
-keepattributes SourceFile,LineNumberTable,Signature,InnerClasses,EnclosingMethod,Exceptions
-keepattributes *Annotation*,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-renamesourcefileattribute SourceFile

# ---- Capacitor core + plugins (loaded by name / annotations via reflection) ----
-keep class com.getcapacitor.** { *; }
-keep interface com.getcapacitor.** { *; }
-keep @com.getcapacitor.annotation.CapacitorPlugin class * { *; }
-keep @com.getcapacitor.NativePlugin class * { *; }
-keep class * extends com.getcapacitor.Plugin { *; }
-keepclassmembers class * {
    @com.getcapacitor.PluginMethod public *;
    @com.getcapacitor.annotation.PermissionCallback *;
    @com.getcapacitor.annotation.ActivityCallback *;
    @android.webkit.JavascriptInterface <methods>;
}
-keep class com.capacitorjs.plugins.** { *; }
-keep class com.getcapacitor.community.** { *; }
-keep class com.digitalaimgroup.pulseinsight.** { *; }
-keep class org.apache.cordova.** { *; }

# Capacitor community in-app-review + share
-keep class com.getcapacitor.community.inappreview.** { *; }
-keep class com.capacitorjs.plugins.share.** { *; }

# ---- Google Play In-App Review ----
-keep class com.google.android.play.core.review.** { *; }
-keep class com.google.android.play.core.common.** { *; }
-dontwarn com.google.android.play.core.**

# ---- Google Mobile Ads / UMP (SDKs ship consumer rules; these are belt-and-braces) ----
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.ump.** { *; }
-dontwarn com.google.android.gms.ads.**

# ---- Meta Audience Network mediation adapter ----
-keep class com.facebook.ads.** { *; }
-keep class com.google.ads.mediation.facebook.** { *; }
-dontwarn com.facebook.ads.**
-dontwarn com.facebook.infer.annotation.**

# AndroidX WebKit
-keep class androidx.webkit.** { *; }
-dontwarn org.jetbrains.annotations.**
