package com.dev.nicehash.app.presenters;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.servers.ExchangeServerImpl;
import com.dev.nicehash.app.views.PayoutsView;
import com.dev.nicehash.domain.models.Payout;
import com.dev.nicehash.domain.repositories.MinerRepository;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Presenter for payouts tab
 */
@InjectViewState
public class PayoutsPresenter extends MvpPresenter<PayoutsView> {
    private final String TAG = PayoutsPresenter.class.getSimpleName();

    private final MinerRepository minerRepository;
    private final ExchangeServer exchangeServer;

    public PayoutsPresenter(MinerRepository minerRepository, ExchangeServer exchangeServer) {
        this.minerRepository = minerRepository;
        this.exchangeServer = exchangeServer;
    }

    @SuppressLint("CheckResult")
    public void fetchPayouts(String miner, int id, boolean isRefresh) {
        if (id < 0) {
            ArrayList<Payout> array = new ArrayList<>();
            // Payout ctor declared order: (dateTime, fee, payBtc, feeBtc)
            array.add(new Payout("7/03 • 13:37", 2f, 0.000323, 0.00000727));
            array.add(new Payout("6/03 • 13:02", 2f, 0.000341, 0.00003425));
            array.add(new Payout("5/03 • 12:52", 2f, 0.000552, 0.00006123));
            getViewState().setupPayouts(array);
        } else {
            if (isRefresh) getViewState().startLoading();
            minerRepository.fetchMinerPayments(miner)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        getViewState().endLoading();
                        if (response.isEmpty()) {
                            getViewState().setupNoItems();
                        } else {
                            getViewState().setupPayouts(response);
                            Handler handler = new Handler();
                            new Thread(() -> {
                                double sumCalc = 0.0;
                                for (Payout payout : response) {
                                    sumCalc += payout.getPayBtc();
                                }

                                final double sum = sumCalc;
                                float usd = exchangeServer.convertBtcFor(
                                        ExchangeServerImpl.ExchangeCode.US.getValue(), sum);

                                handler.post(() -> getViewState().setupBarInfo(sum, usd));
                            }).start();
                        }
                    }, error -> {
                        getViewState().endLoading();
                        getViewState().showError(error.getLocalizedMessage());
                    });
        }
    }
}
