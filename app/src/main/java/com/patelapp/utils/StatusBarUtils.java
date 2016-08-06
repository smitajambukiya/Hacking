package com.patelapp.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by AndroidDevloper on 3/17/2016.
 */
public class StatusBarUtils {

    public static void hide(Activity activity) {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
