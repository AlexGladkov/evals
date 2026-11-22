package com.dev.nicehash.app.activities;

import android.content.Intent;
import android.os.Bundle;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.interfaces.RouterProvider;
import com.dev.nicehash.app.presenters.SettingsPresenter;
import com.dev.nicehash.app.views.SettingsView;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.enums.ScreenKeys;
import com.github.terrakok.cicerone.Back;
import com.github.terrakok.cicerone.Command;
import com.github.terrakok.cicerone.Navigator;
import com.github.terrakok.cicerone.NavigatorHolder;
import com.github.terrakok.cicerone.Replace;
import com.github.terrakok.cicerone.Router;

import javax.inject.Inject;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Activity for user settings
 */
public class SettingsActivity extends BaseActivity implements RouterProvider, SettingsView {
    private final String TAG = ChooseActivity.class.getSimpleName();

    // MARK: - Injects
    @Inject
    Router routerSettings;
    @Inject
    NavigatorHolder navigatorHolder;
    @Inject
    ConfigurationRepository configurationRepository;

    // MARK: - Presenter
    @InjectPresenter
    public SettingsPresenter settingsPresenter;

    @ProvidePresenter
    public SettingsPresenter provideSettingsPresenter() {
        return new SettingsPresenter(routerSettings, configurationRepository);
    }

    // MARK: - Navigation
    private final Navigator navigator = new Navigator() {
        @Override
        public void applyCommands(Command[] commands) {
            for (Command command : commands) {
                applyCommand(command);
            }
        }
    };

    private void applyCommand(Command command) {
        if (command instanceof Back) {
            finish();
        } else if (command instanceof Replace) {
            Replace replace = (Replace) command;
            String screenKey = replace.getScreen().getScreenKey();

            if (screenKey.equals(ScreenKeys.Notifications.getValue())) {
                startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (screenKey.equals(ScreenKeys.Themes.getValue())) {
                startActivity(new Intent(getApplicationContext(), ThemesActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (screenKey.equals(ScreenKeys.Tab.getValue())) {
//                        val popupMenu = PopupMenu(applicationContext, txtTabValue)
//                        val menuInflater = popupMenu.menuInflater
//
//                        popupMenu.setOnMenuItemClickListener { menuItem ->
//                            when (menuItem.itemId) {
//                                R.id.default_tab_income -> {
////                                    txtTabValue.text = getString(R.string.settings_income)
//                                    currentConfiguration.defaultTab = 0
//                                }
//                                R.id.default_tab_balance -> {
////                                    txtTabValue.text = getString(R.string.settings_balance)
//                                    currentConfiguration.defaultTab = 1
//                                }
//                                R.id.default_tab_payouts -> {
////                                    txtTabValue.text = getString(R.string.settings_payouts)
//                                    currentConfiguration.defaultTab = 2
//                                }
//                                R.id.default_tab_workers -> {
////                                    txtTabValue.text = getString(R.string.settings_workers)
//                                    currentConfiguration.defaultTab = 3
//                                }
//                            }
//
//                            settingsPresenter.updateConfiguration(configuration = currentConfiguration)
//                            false
//                        }
//
//                        menuInflater.inflate(R.menu.menu_default_tab, popupMenu.menu)
//                        popupMenu.show()
            } else if (screenKey.equals(ScreenKeys.Language.getValue())) {
//                        val popupMenu = PopupMenu(applicationContext, txtLanguageValue)
//                        val menuInflater = popupMenu.menuInflater

//                        popupMenu.setOnMenuItemClickListener { menuItem ->
//                            when (menuItem.itemId) {
//                                R.id.language_english -> {
////                                    txtLanguageValue.text = getString(R.string.settings_language_english)
//                                    currentConfiguration.defaultLanguage = EnumCollections.Language.English
//                                }
//                                R.id.language_chinese -> {
////                                    txtLanguageValue.text = getString(R.string.settings_language_chinese)
//                                    currentConfiguration.defaultLanguage = EnumCollections.Language.Chinese
//                                }
//                                R.id.language_russian -> {
////                                    txtLanguageValue.text = getString(R.string.settings_language_russian)
//                                    currentConfiguration.defaultLanguage = EnumCollections.Language.Russian
//                                }
//                            }
//
//                            settingsPresenter.updateConfiguration(configuration = currentConfiguration)
//                            false
//                        }
//
//                        menuInflater.inflate(R.menu.menu_language, popupMenu.menu)
//                        popupMenu.show()
            } else if (screenKey.equals(ScreenKeys.Currencies.getValue())) {
                startActivity(new Intent(getApplicationContext(), CurrenciesActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (screenKey.equals(ScreenKeys.Service.getValue())) {
                startActivity(new Intent(getApplicationContext(), ServiceStatusActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (screenKey.equals(ScreenKeys.Purchases.getValue())) {
                startActivity(new Intent(getApplicationContext(), PurchasesActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (screenKey.equals(ScreenKeys.About.getValue())) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    private Configuration currentConfiguration = Configuration.defaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        imgSettingsBack.setOnClickListener { settingsPresenter.onBackClick() }
//
//        btnSettingsNotifications.setOnClickListener { settingsPresenter.performClick(id = 0) }
//        btnSettingsThemes.setOnClickListener { settingsPresenter.performClick(id = 1) }
//        btnSettingsDefaultTab.setOnClickListener { settingsPresenter.performClick(id = 2) }
//        btnSettingsLanguage.setOnClickListener { settingsPresenter.performClick(id = 3) }
//        btnSettingsCurrencies.setOnClickListener { settingsPresenter.performClick(id = 4) }
//        btnSettingsService.setOnClickListener { settingsPresenter.performClick(id = 5) }
//        btnSettingsPurchases.setOnClickListener { settingsPresenter.performClick(id = 6) }
//        btnSettingsAbout.setOnClickListener { settingsPresenter.performClick(id = 7) }
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingsPresenter.fetchConfiguration();

        if (App.isSettingsNeedsToReload) {
            App.isSettingsNeedsToReload = false;
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    // MARK: - RouterProvider implementation
    @Override
    public Router getRouter() {
        return routerSettings;
    }

    // MARK: - View implementation
    @Override
    public void setupConfiguration(Configuration configuration) {
        this.currentConfiguration = configuration;
//        txtTabValue.text = when (configuration.defaultTab) {
//            1 -> getString(R.string.settings_balance)
//            2 -> getString(R.string.settings_payouts)
//            3 -> getString(R.string.settings_workers)
//            else -> getString(R.string.settings_income)
//        }

//        txtLanguageValue.text = getString(configuration.defaultLanguage.title)
    }
}
