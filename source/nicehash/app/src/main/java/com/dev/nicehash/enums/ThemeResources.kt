package com.dev.nicehash.enums

import com.dev.nicehash.R

/**
 * Resource mappings for [Theme] (a2 single source of truth).
 * Lives in :app because :domain is a pure Kotlin module (a3) and cannot
 * reference the R class. Keeps the same call-site syntax as the former
 * members (App.theme.styleRes / App.theme.statusBarColorRes).
 */
val Theme.styleRes: Int
    get() = when (this) {
        Theme.LightBlue -> R.style.LightBlueTheme
        Theme.LightGreen -> R.style.LightGreenTheme
        Theme.LightOrange -> R.style.LightOrangeTheme
        Theme.LightRed -> R.style.LightRedTheme
        Theme.LightPurple -> R.style.LightPurpleTheme
        Theme.DarkBlue -> R.style.DarkBlueTheme
        Theme.DarkGreen -> R.style.DarkGreenTheme
        Theme.DarkOrange -> R.style.DarkOrangeTheme
        Theme.DarkRed -> R.style.DarkRedTheme
        Theme.DarkPurple -> R.style.DarkPurpleTheme
    }

val Theme.statusBarColorRes: Int
    get() = if (isDark) R.color.dark_status_bar_color else R.color.light_status_bar_color
