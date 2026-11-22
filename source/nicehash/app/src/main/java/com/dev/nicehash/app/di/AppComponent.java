package com.dev.nicehash.app.di;

import com.dev.nicehash.app.activities.AddActivity;
import com.dev.nicehash.app.activities.ChooseActivity;
import com.dev.nicehash.app.activities.CurrenciesActivity;
import com.dev.nicehash.app.activities.MainActivity;
import com.dev.nicehash.app.activities.NotificationsActivity;
import com.dev.nicehash.app.activities.PurchasesActivity;
import com.dev.nicehash.app.activities.ServiceStatusActivity;
import com.dev.nicehash.app.activities.SettingsActivity;
import com.dev.nicehash.app.activities.SplashActivity;
import com.dev.nicehash.app.activities.ThemesActivity;
import com.dev.nicehash.app.fragments.BalanceFragment;
import com.dev.nicehash.app.fragments.IncomeFragment;
import com.dev.nicehash.app.fragments.PayoutsFragment;
import com.dev.nicehash.app.fragments.WorkersFragment;
import com.dev.nicehash.base.BaseContainer;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by Alex Gladkov on 21.06.18.
 * Dagger app component
 */
@Component(
    modules = {AppModule.class, RoomModule.class, ProviderModule.class, NavigationModule.class,
        FirebaseModule.class, RemoteModule.class, LocalNavigationModule.class, RepositoryModule.class,
        ConverterModule.class}
)
@Singleton
public interface AppComponent {

    // Activities
    void inject(MainActivity activity);
    void inject(ChooseActivity activity);
    void inject(SettingsActivity activity);
    void inject(AddActivity activity);
    void inject(SplashActivity activity);
    void inject(NotificationsActivity activity);
    void inject(ThemesActivity activity);
    void inject(ServiceStatusActivity activity);
    void inject(CurrenciesActivity activity);
    void inject(PurchasesActivity activity);

    // Fragments
    void inject(BaseContainer fragment);
    void inject(PayoutsFragment fragment);
    void inject(WorkersFragment fragment);
    void inject(BalanceFragment fragment);
    void inject(IncomeFragment fragment);
}
