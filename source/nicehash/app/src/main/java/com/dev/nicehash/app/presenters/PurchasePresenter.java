package com.dev.nicehash.app.presenters;

import com.dev.nicehash.R;
import com.dev.nicehash.app.views.PurchaseView;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class PurchasePresenter extends MvpPresenter<PurchaseView> {

    public void performBuy(int index) {
        getViewState().makeTestBuy();
    }

    public void checkAvailability(boolean isAvailable) {
        if (!isAvailable) {
            getViewState().showError(R.string.purchase_unavailable);
        }
    }
}
