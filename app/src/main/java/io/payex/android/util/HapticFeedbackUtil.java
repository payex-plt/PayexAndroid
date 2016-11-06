package io.payex.android.util;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class HapticFeedbackUtil {

    public static void vibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (v.hasVibrator()) {
            v.cancel();
            v.vibrate(150);
        }
    }

    public static void shake(View view) {
        YoYo.with(Techniques.Shake)
                .duration(350)
                .delay(0)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(view);
    }

}
