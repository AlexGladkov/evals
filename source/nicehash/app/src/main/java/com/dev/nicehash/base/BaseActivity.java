package com.dev.nicehash.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.enums.Theme;

import moxy.MvpAppCompatActivity;

/**
 * Created by Alex Gladkov on 17.06.18.
 * Base activity for extension with theme support
 */
public abstract class BaseActivity extends MvpAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        switch (App.theme) {
            case LightBlue:
                setTheme(R.style.LightBlueTheme);
                break;
            case LightGreen:
                setTheme(R.style.LightGreenTheme);
                break;
            case LightOrange:
                setTheme(R.style.LightOrangeTheme);
                break;
            case LightRed:
                setTheme(R.style.LightRedTheme);
                break;
            case LightPurple:
                setTheme(R.style.LightPurpleTheme);
                break;
            case DarkBlue:
                setTheme(R.style.DarkBlueTheme);
                break;
            case DarkGreen:
                setTheme(R.style.DarkGreenTheme);
                break;
            case DarkOrange:
                setTheme(R.style.DarkOrangeTheme);
                break;
            case DarkRed:
                setTheme(R.style.DarkRedTheme);
                break;
            case DarkPurple:
                setTheme(R.style.DarkPurpleTheme);
                break;
        }

        App.isDark = App.theme == Theme.DarkBlue || App.theme == Theme.DarkPurple
                || App.theme == Theme.DarkRed || App.theme == Theme.DarkOrange || App.theme == Theme.DarkGreen;

        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setStatusBarColor(App.theme == Theme.DarkPurple
                ? R.color.dark_status_bar_color
                : App.theme == Theme.DarkBlue
                ? R.color.dark_status_bar_color
                : App.theme == Theme.DarkRed
                ? R.color.dark_status_bar_color
                : App.theme == Theme.DarkOrange
                ? R.color.dark_status_bar_color
                : App.theme == Theme.DarkGreen
                ? R.color.dark_status_bar_color
                : R.color.light_status_bar_color);
    }

    private void setStatusBarColor(int color) {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.BLACK);
    }

    private void setWindowFlag(Activity activity, int bits, boolean on) {
        WindowManager.LayoutParams winParams = activity.getWindow().getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        activity.getWindow().setAttributes(winParams);
    }
}
