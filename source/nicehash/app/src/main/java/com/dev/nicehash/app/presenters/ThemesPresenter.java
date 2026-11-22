package com.dev.nicehash.app.presenters;

import android.annotation.SuppressLint;

import com.dev.nicehash.app.App;
import com.dev.nicehash.app.views.ThemesView;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.enums.Theme;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 21.07.18.
 * Presenters for themes activity
 */
@InjectViewState
public class ThemesPresenter extends MvpPresenter<ThemesView> {
    private boolean isLight = false;
    private int color = 0;

    private final ConfigurationRepository configurationRepository;

    public ThemesPresenter(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public void onTabClick(int position) {
        getViewState().setupTab(position);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setLight(boolean isLight) {
        this.isLight = isLight;
    }

    public void setupTheme(boolean isLight) {
        App.isMainNeedsToReload = true;
        App.isChooseNeedsToReload = true;
        App.isSettingsNeedsToReload = true;
        getViewState().switchTheme(isLight, color);
    }

    public void setupView() {
        saveSettings();
        getViewState().setupView(isLight, color);
    }

    public void onPaletteClick(int position) {
        App.isMainNeedsToReload = true;
        App.isChooseNeedsToReload = true;
        App.isSettingsNeedsToReload = true;
        getViewState().switchTheme(isLight, position);
    }

    @SuppressLint("CheckResult")
    private void saveSettings() {
        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(config -> {
                    if (isLight) {
                        switch (color) {
                            case 0: {
                                App.theme = Theme.LightBlue;
                                config.setTheme(Theme.LightBlue);
                                break;
                            }
                            case 1: {
                                App.theme = Theme.LightGreen;
                                config.setTheme(Theme.LightGreen);
                                break;
                            }
                            case 2: {
                                App.theme = Theme.LightOrange;
                                config.setTheme(Theme.LightOrange);
                                break;
                            }
                            case 3: {
                                App.theme = Theme.LightRed;
                                config.setTheme(Theme.LightRed);
                                break;
                            }
                            case 4: {
                                App.theme = Theme.LightPurple;
                                config.setTheme(Theme.LightPurple);
                                break;
                            }
                        }
                    } else {
                        switch (color) {
                            case 0: {
                                App.theme = Theme.DarkBlue;
                                config.setTheme(Theme.DarkBlue);
                                break;
                            }
                            case 1: {
                                App.theme = Theme.DarkGreen;
                                config.setTheme(Theme.DarkGreen);
                                break;
                            }
                            case 2: {
                                App.theme = Theme.DarkOrange;
                                config.setTheme(Theme.DarkOrange);
                                break;
                            }
                            case 3: {
                                App.theme = Theme.DarkRed;
                                config.setTheme(Theme.DarkRed);
                                break;
                            }
                            case 4: {
                                App.theme = Theme.DarkPurple;
                                config.setTheme(Theme.DarkPurple);
                                break;
                            }
                        }
                    }

                    configurationRepository.updateConfiguration(config)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aBoolean -> {
                            }, throwable -> {
                            });
                });
    }

    public void setupAppTheme() {
        isLight = !App.isDark;
        color = App.theme.getColor();
        switch (App.theme) {
            case DarkOrange:
                getViewState().setupView(false, 2);
                break;
            case LightOrange:
                getViewState().setupView(true, 2);
                break;
            case DarkBlue:
                getViewState().setupView(false, 0);
                break;
            case LightBlue:
                getViewState().setupView(true, 0);
                break;
            case DarkGreen:
                getViewState().setupView(false, 1);
                break;
            case LightGreen:
                getViewState().setupView(true, 1);
                break;
            case DarkRed:
                getViewState().setupView(false, 3);
                break;
            case LightRed:
                getViewState().setupView(true, 3);
                break;
            case DarkPurple:
                getViewState().setupView(false, 4);
                break;
            case LightPurple:
                getViewState().setupView(true, 4);
                break;
        }
    }
}
