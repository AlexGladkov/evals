package com.dev.nicehash.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.databinding.ActivityAddBinding;
import com.dev.nicehash.domain.models.Miner;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.enums.Keys;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Activity for adding miners
 */
public class AddActivity extends BaseActivity {
    private final String TAG = AddActivity.class.getSimpleName();
    private boolean isEditing = false;
    private ActivityAddBinding binding;

    // MARK: - Repositories
    @Inject
    MinerRepository minerRepository;
    @Inject
    ConfigurationRepository configurationRepository;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent() != null ? getIntent().getExtras() : null;
        if (extras != null && extras.get(Keys.Miner.getValue()) instanceof Bundle) {
            Bundle bundle = (Bundle) extras.get(Keys.Miner.getValue());
            Miner miner = bundle != null ? (Miner) bundle.get(Keys.Miner.getValue()) : null;
            binding.txtAddName.setText(miner != null && miner.getName() != null ? miner.getName() : "");
            binding.txtAddHash.setText(miner != null && miner.getHash() != null ? miner.getHash() : "");
            binding.txtAddTitle.setText(getString(R.string.edit_miner_title));
            isEditing = true;
        }

        binding.btnAddApply.setOnClickListener(v -> {
            if (isEditing) {
                Bundle applyExtras = getIntent() != null ? getIntent().getExtras() : null;
                Bundle applyBundle = applyExtras != null
                        && applyExtras.get(Keys.Miner.getValue()) instanceof Bundle
                        ? (Bundle) applyExtras.get(Keys.Miner.getValue()) : null;
                Miner applyMiner = applyBundle != null
                        ? (Miner) applyBundle.get(Keys.Miner.getValue()) : null;

                minerRepository.updateMiner(applyMiner, binding.txtAddName.getText().toString(),
                        binding.txtAddHash.getText().toString())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aBoolean -> {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_saved), Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }, error -> {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        });
            } else {
                configurationRepository.fetchConfiguration()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(configuration -> {
                            minerRepository.addMiner(new Miner(-1, binding.txtAddName.getText().toString(),
                                    binding.txtAddHash.getText().toString(), false))
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(minerId -> {
                                        configuration.setMinerId(minerId.intValue());
                                        configurationRepository.updateConfiguration(configuration)
                                                .subscribeOn(Schedulers.computation())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(aBoolean -> {
                                                    Intent splashIntent = new Intent(getApplicationContext(), SplashActivity.class);
                                                    splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(splashIntent);
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                    finishAffinity();
                                                }, throwable -> {
                                                    Toast.makeText(getApplicationContext(), TAG + ", Error update configuration",
                                                            Toast.LENGTH_LONG).show();
                                                });
                                    }, throwable -> {
                                        Toast.makeText(getApplicationContext(), TAG + ", Error add miner",
                                                Toast.LENGTH_LONG).show();
                                    });
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), TAG + ", Error load configuration",
                                    Toast.LENGTH_LONG).show();
                        });
            }
        });
    }
}
