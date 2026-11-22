package com.dev.nicehash.enums

import com.dev.nicehash.R

/**
 * Created by agladkov on 17.06.18.
 * Contains themes for application, combined with dark and light
 */
enum class Theme(val value: Int, val color: Int) {
    DarkOrange(0, 2), LightOrange(1, 2), DarkBlue(2, 0), LightBlue(3, 0), DarkGreen(4, 1), LightGreen(5, 1),
    DarkRed(6, 3), LightRed(7, 3), DarkPurple(8, 4), LightPurple(9, 4);

    /**
     * Single source of truth for dark/light split, style resource and status bar color.
     * Replaces duplicated when-chains in activities (DRY/OCP).
     */
    val isDark: Boolean
        get() = this == DarkBlue || this == DarkGreen || this == DarkRed || this == DarkOrange || this == DarkPurple

    val styleRes: Int
        get() = when (this) {
            LightBlue -> R.style.LightBlueTheme
            LightGreen -> R.style.LightGreenTheme
            LightOrange -> R.style.LightOrangeTheme
            LightRed -> R.style.LightRedTheme
            LightPurple -> R.style.LightPurpleTheme
            DarkBlue -> R.style.DarkBlueTheme
            DarkGreen -> R.style.DarkGreenTheme
            DarkOrange -> R.style.DarkOrangeTheme
            DarkRed -> R.style.DarkRedTheme
            DarkPurple -> R.style.DarkPurpleTheme
        }

    val statusBarColorRes: Int
        get() = if (isDark) R.color.dark_status_bar_color else R.color.light_status_bar_color
}
