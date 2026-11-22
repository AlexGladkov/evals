package com.dev.nicehash.app;

import android.app.Application;
import android.os.Trace;

import com.dev.nicehash.app.di.AppComponent;
import com.dev.nicehash.app.di.AppModule;
import com.dev.nicehash.app.di.DaggerAppComponent;
import com.dev.nicehash.app.di.LocalNavigationModule;
import com.dev.nicehash.app.di.RemoteModule;
import com.dev.nicehash.app.di.RoomModule;
import com.dev.nicehash.enums.Theme;

/**
 * Created by agladkov on 17.06.18.
 * Class for application
 */
public class App extends Application {

    public static Theme theme = Theme.DarkPurple;
    public static boolean isDark = false;

    public static boolean isSettingsNeedsToReload = false;
    public static boolean isChooseNeedsToReload = false;
    public static boolean isMainNeedsToReload = false;

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        FirebaseApp.initializeApp(this)
        Trace.beginSection("Dagger2");
        initializeDagger();
        Trace.endSection();
    }

    private void initializeDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule())
                .remoteModule(new RemoteModule())
                .localNavigationModule(new LocalNavigationModule())
                .build();
    }
}
