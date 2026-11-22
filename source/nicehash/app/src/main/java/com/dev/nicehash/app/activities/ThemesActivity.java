package com.dev.nicehash.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.presenters.ThemesPresenter;
import com.dev.nicehash.app.views.ThemesView;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.enums.Keys;
import com.dev.nicehash.enums.Theme;
import com.github.terrakok.cicerone.NavigatorHolder;
import com.github.terrakok.cicerone.Router;

import javax.inject.Inject;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for theme settings
 */
public class ThemesActivity extends MvpAppCompatActivity implements ThemesView {
    private final String TAG = ThemesActivity.class.getSimpleName();

    @Inject
    ConfigurationRepository configurationRepository;
    @Inject
    Router routerTheme;
    @Inject
    NavigatorHolder navigatorHolder;

    @InjectPresenter
    public ThemesPresenter themesPresenter;

    @ProvidePresenter
    public ThemesPresenter provideThemesPresenter() {
        return new ThemesPresenter(configurationRepository);
    }

    // MARK: - Navigation
//    private val navigator = object: AppNavigator(this@ThemesActivity, supportFragmentManager, 0) {
//        override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? {
//            return null
//        }
//
//        override fun createFragment(screenKey: String?, data: Any?): Fragment? {
//            return null
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
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
        } else {
//            val isLight = intent.extras.get(Keys.Light.value) as? Boolean ?: true
//            val color = intent.extras.get(Keys.Color.value) as? Int ?: 0
//            when (isLight) {
//                true -> {
//                    when (color) {
//                        0 -> setTheme(R.style.LightBlueTheme)
//                        1 -> setTheme(R.style.LightGreenTheme)
//                        2 -> setTheme(R.style.LightOrangeTheme)
//                        3 -> setTheme(R.style.LightRedTheme)
//                        4 -> setTheme(R.style.LightPurpleTheme)
//                    }
//                }
//                false -> {
//                    when (color) {
//                        0 -> setTheme(R.style.DarkBlueTheme)
//                        1 -> setTheme(R.style.DarkGreenTheme)
//                        2 -> setTheme(R.style.DarkOrangeTheme)
//                        3 -> setTheme(R.style.DarkRedTheme)
//                        4 -> setTheme(R.style.DarkPurpleTheme)
//                    }
//                }
//            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (getIntent() == null || getIntent().getExtras() == null) {
            themesPresenter.setupAppTheme();
        } else {
            Bundle extras = getIntent().getExtras();
            themesPresenter.setColor(extras.get(Keys.Color.getValue()) instanceof Integer
                    ? (Integer) extras.get(Keys.Color.getValue()) : 0);
            themesPresenter.setLight(extras.get(Keys.Light.getValue()) instanceof Boolean
                    ? (Boolean) extras.get(Keys.Light.getValue()) : true);
            themesPresenter.setupView();
        }

//        imgThemesBack.setOnClickListener { onBackPressed() }
//        flLightTab.setOnClickListener { themesPresenter.onTabClick(position = 0) }
//        flDarkTab.setOnClickListener { themesPresenter.onTabClick(position = 1) }
//        flThemesBluePalette.setOnClickListener { themesPresenter.onPaletteClick(position = 0) }
//        flThemesGreenPalette.setOnClickListener { themesPresenter.onPaletteClick(position = 1) }
//        flThemesOrangePalette.setOnClickListener { themesPresenter.onPaletteClick(position = 2) }
//        flThemesRedPalette.setOnClickListener { themesPresenter.onPaletteClick(position = 3) }
//        flThemesPurplePalette.setOnClickListener { themesPresenter.onPaletteClick(position = 4) }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
//        navigatorHolder.setNavigator(navigator)
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    private void setStatusBarColor(int color) {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(getResources().getColor(color));
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

    // MARK: - View implementation
    @Override
    public void setupTab(int position) {
        themesPresenter.setupTheme(position == 0);
    }

    @Override
    public void setupView(boolean isLight, int color) {
        // The whole view implementation is commented out in the original Kotlin source
    }

    @Override
    public void switchTheme(boolean isLight, int color) {
        Intent intent = new Intent(getApplicationContext(), ThemesActivity.class);
        intent.putExtra(Keys.Light.getValue(), isLight);
        intent.putExtra(Keys.Color.getValue(), color);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
