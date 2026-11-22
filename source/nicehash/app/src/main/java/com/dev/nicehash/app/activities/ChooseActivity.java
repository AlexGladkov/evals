package com.dev.nicehash.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.adapters.MinerAdapter;
import com.dev.nicehash.app.interfaces.RouterProvider;
import com.dev.nicehash.app.presenters.ChoosePresenter;
import com.dev.nicehash.app.views.ChooseView;
import com.dev.nicehash.base.BaseAdapterCallback;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.models.Miner;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.enums.Keys;
import com.dev.nicehash.enums.ScreenKeys;
import com.dev.nicehash.enums.Theme;
import com.github.terrakok.cicerone.Back;
import com.github.terrakok.cicerone.Command;
import com.github.terrakok.cicerone.Navigator;
import com.github.terrakok.cicerone.NavigatorHolder;
import com.github.terrakok.cicerone.Replace;
import com.github.terrakok.cicerone.Router;

import java.util.List;

import javax.inject.Inject;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Activity for multi accounting
 */
public class ChooseActivity extends MvpAppCompatActivity implements RouterProvider, ChooseView {
    private final String TAG = ChooseActivity.class.getSimpleName();

    // MARK: - Injects
    @Inject
    Router routerChoose;
    @Inject
    NavigatorHolder navigatorHolder;
    @Inject
    MinerRepository minerRepository;
    @Inject
    ConfigurationRepository configurationRepository;

    @InjectPresenter
    public ChoosePresenter choosePresenter;

    @ProvidePresenter
    public ChoosePresenter provideChoosePresenter() {
        return new ChoosePresenter(routerChoose, minerRepository, configurationRepository);
    }

    // MARK: - UI Setup
    private final MinerAdapter mAdapter = new MinerAdapter(new MinerAdapter.MinerClick() {
        @Override
        public void onEditClick(Miner miner) {
            choosePresenter.editClick(miner);
        }

        @Override
        public void onDeleteClick(Miner miner) {
            choosePresenter.deleteClick(miner);
        }
    });

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
            if (screenKey.equals(ScreenKeys.Settings.getValue())) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                settingsIntent.putExtra(Keys.Configuration.getValue(), currentConfiguration);
                startActivity(settingsIntent);
            } else if (screenKey.equals(ScreenKeys.Splash.getValue())) {
                Intent splashIntent = new Intent(getApplicationContext(), SplashActivity.class);
                splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(splashIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else if (screenKey.equals(ScreenKeys.Add.getValue())) {
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
            } else if (screenKey.equals(ScreenKeys.Edit.getValue())) {
//                        (command.transitionData as? Bundle)?.let {
//                            val editIntent = Intent(applicationContext, AddActivity::class.java)
//                            editIntent.putExtra(Keys.Miner.value, it)
//                            startActivity(editIntent)
//                        }
            }
        }
    }

    private Configuration currentConfiguration = Configuration.defaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        if (App.isDark) {
            setTheme(R.style.DarkTransparent);
        } else {
            setTheme(R.style.LightTransparent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setStatusBarColor(App.theme == Theme.DarkPurple
                ? R.color.dark_status_bar_color
                : App.theme == Theme.DarkBlue
                ? R.color.dark_status_bar_color
                : App.theme == Theme.DarkRed
                ? R.color.dark_status_bar_color
                : App.theme == Theme.DarkOrange
                ? R.color.dark_status_bar_color
                : App.theme == Theme.DarkGreen
                ? R.color.dark_status_bar_color
                : R.color.light_status_bar_color);

        choosePresenter.handleConfiguration(getIntent() != null ? getIntent().getExtras() : null);

        mAdapter.attachCallback(new BaseAdapterCallback<Miner>() {
            @Override
            public void onItemClick(Miner model, View view) {
                choosePresenter.onMinerClick(model);
            }

            @Override
            public boolean onLongClick(Miner model, View view) {
                return false;
            }
        });

        switch (App.theme) {
            case DarkGreen: {
//                tbChoose.background = ContextCompat.getDrawable(applicationContext, R.color.dark_background_primary)
//                btnChooseSettings.setColorFilter(ContextCompat.getColor(applicationContext, R.color.dark_tint_color))
//                imgChooseAdd.setColorFilter(ContextCompat.getColor(applicationContext, R.color.dark_tint_color))
                break;
            }
            case DarkOrange: {
                break;
            }
            case DarkRed: {
                break;
            }
            case DarkPurple: {
                break;
            }
            case DarkBlue: {
                break;
            }
            default: {
                break;
            }
        }

//        flChoose.setOnClickListener { onBackPressed() }
//        imgChooseAdd.setOnClickListener { choosePresenter.onAddClick() }
//
//        val listConfig = ListConfig.Builder(mAdapter)
//                .setHasFixedSize(true)
//                .setHasNestedScroll(false)
//                .build(applicationContext)
//        listConfig.applyConfig(applicationContext, recyclerMiners)
//
//        btnChooseSettings.setOnClickListener { choosePresenter.onGearClick() }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        choosePresenter.loadMiners();
        if (App.isChooseNeedsToReload) {
            App.isChooseNeedsToReload = false;
            finish();
            overridePendingTransition(0, 0);
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

    // MARK: - RouterProvider implementation
    @Override
    public Router getRouter() {
        return routerChoose;
    }

    // MARK: - View implementation
    @Override
    public void setupMiners(List<Miner> list) {
        if (mAdapter.hasItems()) {
            mAdapter.updateItems(list);
        } else {
            mAdapter.setList(list);
        }
    }

    @Override
    public void setupConfiguration(Configuration configuration) {
        this.currentConfiguration = configuration;
    }
}
