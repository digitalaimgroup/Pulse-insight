package com.digitalaimgroup.pulseinsight;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

/**
 * Small in-app native bridge for PULSE Insight (v1.4):
 *  - requestReview: Google Play In-App Review flow (rating without leaving the game)
 *  - share: Android share sheet for score / challenge text
 * Exposed to JS as window.Capacitor.Plugins.PulseNative
 */
@CapacitorPlugin(name = "PulseNative")
public class PulseNativePlugin extends Plugin {

    @PluginMethod
    public void requestReview(final PluginCall call) {
        if (getActivity() == null) {
            call.reject("no_activity");
            return;
        }
        final ReviewManager manager = ReviewManagerFactory.create(getContext());
        manager.requestReviewFlow().addOnCompleteListener(request -> {
            if (!request.isSuccessful()) {
                call.reject("review_unavailable");
                return;
            }
            ReviewInfo info = request.getResult();
            manager.launchReviewFlow(getActivity(), info).addOnCompleteListener(flow -> {
                JSObject ret = new JSObject();
                ret.put("shown", true);
                call.resolve(ret);
            });
        });
    }

    @PluginMethod
    public void share(PluginCall call) {
        String text = call.getString("text", "");
        String title = call.getString("title", "Share");
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_TEXT, text);
        send.putExtra(Intent.EXTRA_SUBJECT, title);
        Intent chooser = Intent.createChooser(send, title);
        try {
            getActivity().startActivity(chooser);
            call.resolve();
        } catch (ActivityNotFoundException e) {
            call.reject("no_share_target");
        }
    }
}
