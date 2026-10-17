package com.digitalaimgroup.pulseinsight;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.getcapacitor.BridgeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Harden the Capacitor WebView against edge swipe / overscroll history gestures
 * that conflict with in-game piece selection near the left/right screen edges.
 */
public class MainActivity extends BridgeActivity {
    private static final int EDGE_EXCLUSION_DP = 56;
    private boolean gestureHooksAttached = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(PulseNativePlugin.class);
        // 1.5.5: no androidx.activity EdgeToEdge.enable(). Its backport code calls
        // Window.setStatusBarColor / setNavigationBarColor and sets
        // LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, which Play flags as deprecated
        // edge-to-edge APIs on Android 15. With targetSdk 36 the OS itself draws the
        // app edge-to-edge on Android 15+ (no opt-out); on Android 14 and older the
        // app uses standard (opaque) system bars, so content is laid out between them.
        // On 15+ the inset listener below feeds the bar/cutout insets to the CSS.
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        configureWebViewGestures();
    }

    @Override
    public void onResume() {
        super.onResume();
        configureWebViewGestures();
    }

    private void configureWebViewGestures() {
        if (getBridge() == null) {
            return;
        }
        final WebView webView = getBridge().getWebView();
        if (webView == null) {
            return;
        }

        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        if (!gestureHooksAttached) {
            gestureHooksAttached = true;
            ViewCompat.setOnApplyWindowInsetsListener(webView, (v, insets) -> {
                Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
                lastInsets = bars;
                pushSafeAreaToWeb(webView);
                return insets;
            });
            ViewCompat.requestApplyInsets(webView);
            webView.addOnLayoutChangeListener(
                (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) ->
                    applySystemGestureExclusions(webView)
            );
        }
        webView.post(() -> applySystemGestureExclusions(webView));
        pushSafeAreaToWeb(webView);
    }

    private Insets lastInsets = Insets.NONE;

    /** Edge-to-edge: expose system bar / cutout insets to CSS as --sat/--sab/--sal/--sar (CSS px). */
    private void pushSafeAreaToWeb(WebView webView) {
        if (webView == null) return;
        float d = getResources().getDisplayMetrics().density;
        Insets i = lastInsets;
        String js = "(function(){var s=document.documentElement.style;"
            + "s.setProperty('--sat','" + Math.round(i.top / d) + "px');"
            + "s.setProperty('--sab','" + Math.round(i.bottom / d) + "px');"
            + "s.setProperty('--sal','" + Math.round(i.left / d) + "px');"
            + "s.setProperty('--sar','" + Math.round(i.right / d) + "px');"
            + "try{window.dispatchEvent(new Event('resize'))}catch(e){}})();";
        webView.post(() -> webView.evaluateJavascript(js, null));
    }

    private void applySystemGestureExclusions(WebView webView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || webView == null) {
            return;
        }
        int width = webView.getWidth();
        int height = webView.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        float density = getResources().getDisplayMetrics().density;
        int edge = Math.max(1, Math.round(EDGE_EXCLUSION_DP * density));

        // Prefer full-height edge strips so tray/board taps near corners aren't stolen.
        // The OS may clamp exclusion size; still request the playable edges every layout.
        List<Rect> rects = new ArrayList<>(2);
        rects.add(new Rect(0, 0, Math.min(edge, width), height));
        rects.add(new Rect(Math.max(0, width - edge), 0, width, height));
        webView.setSystemGestureExclusionRects(rects);
    }
}
