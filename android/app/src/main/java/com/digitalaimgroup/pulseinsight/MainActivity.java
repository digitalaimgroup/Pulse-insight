package com.digitalaimgroup.pulseinsight;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

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
            webView.addOnLayoutChangeListener(
                (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) ->
                    applySystemGestureExclusions(webView)
            );
        }
        webView.post(() -> applySystemGestureExclusions(webView));
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
