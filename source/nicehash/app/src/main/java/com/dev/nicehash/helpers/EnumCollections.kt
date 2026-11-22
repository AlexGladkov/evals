package com.dev.nicehash.helpers

import com.dev.nicehash.R

/**
 * Created by Alex Gladkov on 16.07.18.
 * Holder for small enum collections
 */
class EnumCollections {

    enum class Language(val title: Int, val intCode: Int, val countryCode: String) {
        Russian(title = R.string.settings_language_russian, intCode = 0, countryCode = "ru"),
        English(title = R.string.settings_language_english, intCode = 1, countryCode = "en"),
        Chinese(title = R.string.settings_language_chinese, intCode = 2, countryCode = "cn")
    }

    enum class Status {
        Green, Yellow, Red
    }

    enum class Currency(val id: Int, val titleRes: Int, val flagRes: Int) {
        Russian(id = 0, titleRes = R.string.currency_rub, flagRes = R.drawable.ic_rub),
        Euro(id = 1, titleRes = R.string.currency_eur, flagRes = R.drawable.ic_eur),
        BritainPound(id = 2, titleRes = R.string.currency_eng, flagRes = R.drawable.ic_gbp),
        USD(id = 3, titleRes = R.string.currency_usd, flagRes = R.drawable.ic_usd),
        Australian(id = 4, titleRes = R.string.currency_aud, flagRes = 0),
        Belgium(id = 5, titleRes = R.string.currency_bgn, flagRes = R.drawable.ic_bgn),
        Canada(id = 6, titleRes = R.string.currency_cad, flagRes = R.drawable.ic_cad),
        CHF(id = 7, titleRes = R.string.currency_chf, flagRes = R.drawable.ic_chf),
        China(id = 8, titleRes = R.string.currency_cny, flagRes = R.drawable.ic_cny),
        Czech(id = 9, titleRes = R.string.currency_czk, flagRes = R.drawable.ic_czk),
        Dania(id = 10, titleRes = R.string.currency_dkk, flagRes = R.drawable.ic_dkk)
    }
}