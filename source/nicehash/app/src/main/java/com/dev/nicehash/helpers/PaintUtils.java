package com.dev.nicehash.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.enums.Theme;

/**
 * Created by Alex Gladkov on 21.07.18.
 * Paint view by theme
 */
public class PaintUtils {

    public static void setChartBackground(View view) {
        int res;
        switch (App.theme) {
            case DarkOrange:
                res = R.drawable.dark_orange_chart_time_background;
                break;
            case LightOrange:
                res = R.drawable.light_orange_chart_time_background;
                break;
            case DarkBlue:
                res = R.drawable.dark_blue_chart_time_background;
                break;
            case LightBlue:
                res = R.drawable.light_blue_chart_time_background;
                break;
            case DarkGreen:
                res = R.drawable.dark_green_chart_time_background;
                break;
            case LightGreen:
                res = R.drawable.light_green_chart_time_background;
                break;
            case DarkRed:
                res = R.drawable.dark_red_chart_time_background;
                break;
            case LightRed:
                res = R.drawable.light_red_chart_time_background;
                break;
            case DarkPurple:
                res = R.drawable.dark_purple_chart_time_background;
                break;
            case LightPurple:
                res = R.drawable.light_purple_chart_time_background;
                break;
            default:
                res = 0;
        }
        view.setBackground(ContextCompat.getDrawable(view.getContext(), res));
    }

    public static void setInactiveChartBackground(View view) {
        view.setBackground(ContextCompat.getDrawable(view.getContext(), App.isDark
                ? R.drawable.dark_chart_time_background
                : R.drawable.light_chart_time_background));
    }

    public static int getChartGradient() {
        switch (App.theme) {
            case LightBlue:
                return R.drawable.light_chart_gradient_blue;
            case LightGreen:
                return R.drawable.light_chart_gradient_green;
            case LightOrange:
                return R.drawable.light_chart_gradient_orange;
            case LightRed:
                return R.drawable.light_chart_gradient_red;
            case LightPurple:
                return R.drawable.light_chart_gradient_purple;
            case DarkPurple:
                return R.drawable.dark_chart_gradient_purple;
            case DarkRed:
                return R.drawable.dark_chart_gradient_red;
            case DarkOrange:
                return R.drawable.dark_chart_gradient_orange;
            case DarkGreen:
                return R.drawable.dark_chart_gradient_green;
            case DarkBlue:
                return R.drawable.dark_chart_gradient_blue;
            default:
                return 0;
        }
    }

    public static int getColorByTheme() {
        switch (App.theme) {
            case DarkOrange:
                return R.color.dark_orange_color_primary;
            case LightOrange:
                return R.color.light_orange_color_primary;
            case DarkBlue:
                return R.color.dark_blue_color_primary;
            case LightBlue:
                return R.color.light_blue_color_primary;
            case DarkGreen:
                return R.color.dark_green_color_primary;
            case LightGreen:
                return R.color.light_green_color_primary;
            case DarkRed:
                return R.color.dark_red_color_primary;
            case LightRed:
                return R.color.light_red_color_primary;
            case DarkPurple:
                return R.color.dark_purple_color_primary;
            case LightPurple:
                return R.color.light_purple_color_primary;
            default:
                return 0;
        }
    }
}
