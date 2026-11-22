package com.dev.nicehash.app.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dev.nicehash.R;
import com.dev.nicehash.base.BaseActivity;

import timber.log.Timber;

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for about information.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String versionName = "—";
        String versionCode = "—";
        try {
            android.content.pm.PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName != null ? pInfo.versionName : "—";
            versionCode = String.valueOf(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Failed to get package info");
        }

        TextView txtAboutVersion = findViewById(R.id.txtAboutVersion);
        if (txtAboutVersion != null) {
            String appVersion = getString(R.string.app_version);
            String appBuild = getString(R.string.app_build).toLowerCase();
            txtAboutVersion.setText(appVersion + " " + versionName + ", " + appBuild + " " + versionCode);
        }

        View websiteRow = findViewById(R.id.ll_about_website);
        if (websiteRow != null) {
            websiteRow.setOnClickListener(v -> openUrl("https://getsignal.co"));
        }

        View twitterRow = findViewById(R.id.ll_about_twitter);
        if (twitterRow != null) {
            twitterRow.setOnClickListener(v -> openUrl("https://twitter.com/getsignal"));
        }

        View authorRow = findViewById(R.id.ll_about_author);
        if (authorRow != null) {
            authorRow.setOnClickListener(v -> openUrl("https://getsignal.co"));
        }

        View developerRow = findViewById(R.id.ll_about_developer);
        if (developerRow != null) {
            developerRow.setOnClickListener(v -> openUrl("https://getsignal.co"));
        }

        View saverRow = findViewById(R.id.ll_about_saver);
        if (saverRow != null) {
            saverRow.setOnClickListener(v -> openUrl("https://getsignal.co"));
        }

        View back = findViewById(R.id.imgAboutBack);
        if (back != null) {
            back.setOnClickListener(v -> onBackPressed());
        }
    }

    private void openUrl(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Timber.e(e, "Failed to open URL: %s", url);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }
}
