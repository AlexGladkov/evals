package com.dev.nicehash.helpers

import com.dev.nicehash.R

/**
 * Maps the pure domain enums (EnumCollections.Currency / Language) to Android resources.
 *
 * Lives in :app because :domain must stay Android-free — the domain enums carry only
 * stable ids, resource lookups are a UI concern. Values mirror the pre-split
 * EnumCollections entries exactly, so displayed strings/drawables are unchanged.
 */
object CurrencyResources {

    fun titleRes(currency: EnumCollections.Currency): Int = when (currency) {
        EnumCollections.Currency.Russian -> R.string.currency_rub
        EnumCollections.Currency.Euro -> R.string.currency_eur
        EnumCollections.Currency.BritainPound -> R.string.currency_eng
        EnumCollections.Currency.USD -> R.string.currency_usd
        EnumCollections.Currency.Australian -> R.string.currency_aud
        EnumCollections.Currency.Belgium -> R.string.currency_bgn
        EnumCollections.Currency.Canada -> R.string.currency_cad
        EnumCollections.Currency.CHF -> R.string.currency_chf
        EnumCollections.Currency.China -> R.string.currency_cny
        EnumCollections.Currency.Czech -> R.string.currency_czk
        EnumCollections.Currency.Dania -> R.string.currency_dkk
    }

    fun flagRes(currency: EnumCollections.Currency): Int = when (currency) {
        EnumCollections.Currency.Russian -> R.drawable.ic_rub
        EnumCollections.Currency.Euro -> R.drawable.ic_eur
        EnumCollections.Currency.BritainPound -> R.drawable.ic_gbp
        EnumCollections.Currency.USD -> R.drawable.ic_usd
        EnumCollections.Currency.Australian -> 0
        EnumCollections.Currency.Belgium -> R.drawable.ic_bgn
        EnumCollections.Currency.Canada -> R.drawable.ic_cad
        EnumCollections.Currency.CHF -> R.drawable.ic_chf
        EnumCollections.Currency.China -> R.drawable.ic_cny
        EnumCollections.Currency.Czech -> R.drawable.ic_czk
        EnumCollections.Currency.Dania -> R.drawable.ic_dkk
    }
}
