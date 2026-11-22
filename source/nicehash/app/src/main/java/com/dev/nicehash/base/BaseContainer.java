package com.dev.nicehash.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.dev.nicehash.app.App;
import com.dev.nicehash.app.interfaces.BackButtonListener;
import com.dev.nicehash.app.interfaces.RouterProvider;
import com.dev.nicehash.app.navigation.LocalCiceroneHolder;
import com.dev.nicehash.enums.Keys;

import javax.inject.Inject;

import moxy.MvpAppCompatFragment;

/**
 * Created by agladkov on 11.01.18.
 * Use this for tabs fragments (ex. ServiceContainer, ProfileContainer, etc)
 */
public abstract class BaseContainer extends MvpAppCompatFragment implements RouterProvider, BackButtonListener {
    private final String TAG = BaseContainer.class.getSimpleName();
    private final String containerName = getArguments() != null
            ? getArguments().getString(Keys.Name.getValue()) : null;

    @Inject
    LocalCiceroneHolder ciceroneHolder;

    public abstract void doubleTap();
    public abstract void updateContainer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getCicerone().getNavigatorHolder().setNavigator(getNavigator());
    }

    @Override
    public void onPause() {
//        getCicerone().getNavigatorHolder().removeNavigator();
        super.onPause();
    }

    private com.github.terrakok.cicerone.Cicerone<com.github.terrakok.cicerone.Router> getCicerone() {
        return ciceroneHolder.getCicerone(String.valueOf(containerName));
    }

    public com.github.terrakok.cicerone.Navigator getNavigator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    // MARK: - BackButtonListener implementation
    @Override
    public boolean onBackPressed() {
        if (getActivity() != null) {
            ((RouterProvider) getActivity()).getRouter().exit();
        }
        return true;
    }

    // MARK: - RouterProvider
    @Override
    public com.github.terrakok.cicerone.Router getRouter() {
        return getCicerone().getRouter();
    }
}
