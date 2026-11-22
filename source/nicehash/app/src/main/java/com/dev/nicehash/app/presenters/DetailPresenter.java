package com.dev.nicehash.app.presenters;

import com.dev.nicehash.app.views.DetailView;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 30.07.18.
 * Presenter for Detail Activity
 */
@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {

    public void onChartTimeClick(int position) {
        getViewState().setupChartView(position);
        loadChartData(position);
    }

    private void loadChartData(int position) {
        ArrayList<Entry> emptyData = new ArrayList<>();

        switch (position) {
            case 0: {
                emptyData.add(new Entry(0f, 0.0001f));
                emptyData.add(new Entry(1f, 0.0007f));
                emptyData.add(new Entry(2f, 0.0005f));
                emptyData.add(new Entry(3f, 0.0010f));
                emptyData.add(new Entry(4f, 0.0006f));
                emptyData.add(new Entry(5f, 0.0007f));
                emptyData.add(new Entry(6f, 0.0011f));
                break;
            }

            case 3: {
                emptyData.add(new Entry(0f, 0.0022f));
                emptyData.add(new Entry(1f, 0.0007f));
                emptyData.add(new Entry(2f, 0.0012f));
                emptyData.add(new Entry(3f, 0.0015f));
                emptyData.add(new Entry(4f, 0.0008f));
                emptyData.add(new Entry(5f, 0.0001f));
                emptyData.add(new Entry(6f, 0.0020f));
                break;
            }

            default: {
                emptyData.add(new Entry(0f, 0.0011f));
                emptyData.add(new Entry(1f, 0.0004f));
                emptyData.add(new Entry(2f, 0.0009f));
                emptyData.add(new Entry(3f, 0.0060f));
                emptyData.add(new Entry(4f, 0.0052f));
                emptyData.add(new Entry(5f, 0.0023f));
                emptyData.add(new Entry(6f, 0.0042f));
                break;
            }
        }

        getViewState().setupChartData(emptyData);
    }
}
