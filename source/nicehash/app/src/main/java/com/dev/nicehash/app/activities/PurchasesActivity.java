package com.dev.nicehash.app.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.presenters.PurchasePresenter;
import com.dev.nicehash.app.views.PurchaseView;
import com.dev.nicehash.base.BaseActivity;

import moxy.presenter.InjectPresenter;

/**
 * Created by Alex Gladkov on 24.06.18.
 * Activity for IAP
 */
public class PurchasesActivity extends BaseActivity implements PurchaseView {

    // MARK: - Presenter setup
    @InjectPresenter
    public PurchasePresenter purchasePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

//        imgPurchasesBack.setOnClickListener { onBackPressed() }
//
//        btnPurchaseThemes.setOnClickListener { purchasePresenter.performBuy(index = 0) }
//        btnPurchaseAddMiner.setOnClickListener { purchasePresenter.performBuy(index = 1) }
//        btnPurchaseRemoveAds.setOnClickListener { purchasePresenter.performBuy(index = 2) }
//        btnPurchaseSilver.setOnClickListener { purchasePresenter.performBuy(index = 3) }
//        btnPurchaseGold.setOnClickListener { purchasePresenter.performBuy(index = 4) }

        purchasePresenter.checkAvailability(BillingProcessor.isIabServiceAvailable(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    // MARK: - View implementation
    @Override
    public void showError(int message) {
        Toast.makeText(getApplicationContext(), getString(message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeTestBuy() {

    }
}
