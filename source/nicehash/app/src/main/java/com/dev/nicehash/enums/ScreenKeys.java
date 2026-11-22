package com.dev.nicehash.enums;

/**
 * Created by Alex Gladkov on 23.06.18.
 * This is collection of navigation screen keys
 */
public enum ScreenKeys {
    Choose("ChooseKey"), Settings("SettingsKey"), Notifications("NotificationsKey"), Themes("ThemesKey"),
    Tab("TabKey"), Language("LanguageKey"), Currencies("CurrenciesKey"), Service("ServiceKey"),
    Purchases("PurchasesKey"), About("AboutKey"), Income("IncomeKey"), Workers("WorkersKey"),
    Balance("BalanceKey"), Payouts("PayoutsKey"), Add("AddKey"), Edit("EditKey"), Detail("DetailKey"),
    Splash("SplashKey");

    private final String value;

    ScreenKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
