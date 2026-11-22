package com.dev.nicehash.app.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.presenters.DetailPresenter;
import com.dev.nicehash.app.views.DetailView;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.databinding.ActivityDetailBinding;
import com.dev.nicehash.domain.models.Balance;
import com.dev.nicehash.enums.Keys;
import com.dev.nicehash.enums.Theme;
import com.dev.nicehash.helpers.PaintUtils;
import com.dev.nicehash.helpers.WindowUtils;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

import moxy.presenter.InjectPresenter;

/**
 * Created by Alex Gladkov on 30.07.18
 * Activity for balance detail view
 */
public class DetailActivity extends BaseActivity implements DetailView {
    private final String TAG = DetailActivity.class.getSimpleName();

    @InjectPresenter
    public DetailPresenter detailPresenter;

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Balance balance = getIntent().getExtras() != null
                ? (Balance) getIntent().getExtras().get(Keys.Balance.getValue()) : null;
        if (balance == null) onBackPressed();

        binding.txtDetailTitle.setText(balance != null && balance.getTitle() != null ? balance.getTitle() : "");
        binding.btnDetailBack.setOnClickListener(v -> onBackPressed());

        tuneChart();
        detailPresenter.onChartTimeClick(0);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    private void tuneChart() {
        binding.chartDetail.fitScreen();
        binding.chartDetail.getDescription().setEnabled(false);
        binding.chartDetail.getLegend().setEnabled(false);
        binding.chartDetail.setViewPortOffsets(0f, 0f, 0f, 0f);
        binding.chartDetail.setDoubleTapToZoomEnabled(false);

        XAxis xAxis = binding.chartDetail.getXAxis();
        xAxis.setDrawLimitLinesBehindData(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.disableGridDashedLine();
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(Color.argb(204, 255, 255, 255));
        xAxis.setAxisLineColor(Color.TRANSPARENT);
        xAxis.setAvoidFirstLastClipping(true);

        LimitLine averageLine = new LimitLine(0.0004f);
        averageLine.setLineColor(Color.WHITE);
        averageLine.setLineWidth(4f);
        averageLine.enableDashedLine(WindowUtils.convertDpToPixel(4f, getApplicationContext()),
                WindowUtils.convertDpToPixel(2f, getApplicationContext()), 2f);
        averageLine.setLineWidth(2f);

        binding.chartDetail.getAxisRight().setEnabled(false);
        binding.chartDetail.getAxisRight().setSpaceBottom(0f);
        binding.chartDetail.getXAxis().setEnabled(false);

        binding.chartDetail.getAxisLeft().setEnabled(true);
        binding.chartDetail.getAxisLeft().addLimitLine(averageLine);
        binding.chartDetail.getAxisLeft().setLabelCount(9, true);
        binding.chartDetail.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        binding.chartDetail.getAxisLeft().setGridColor(App.isDark
                ? Color.parseColor("#1AFFFFFF")
                : Color.parseColor("#0D000000"));
        binding.chartDetail.getAxisLeft().setGridLineWidth(0.5f);
        binding.chartDetail.getAxisLeft().setTextSize(10f);
        binding.chartDetail.getAxisLeft().setDrawTopYLabelEntry(false);
        binding.chartDetail.getAxisLeft().setDrawZeroLine(false);
        binding.chartDetail.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, com.github.mikephil.charting.components.AxisBase axisBase) {
                if (value < 0.00001f) {
                    return "";
                }

                return String.format("%.05f", value);
            }
        });
        binding.chartDetail.getAxisLeft().setAxisLineColor(ContextCompat.getColor(getApplicationContext(),
                App.isDark ? R.color.dark_background_secondary : R.color.light_background_secondary));

        binding.chartDetail.getAxisLeft().setTextColor(ContextCompat.getColor(getApplicationContext(),
                App.isDark ? R.color.dark_text_color_primary : R.color.light_text_color_primary));
    }

    // MARK: - View implementation
    @Override
    public void setupChartView(int position) {
        PaintUtils.setInactiveChartBackground(binding.flDetailOneHour);
        PaintUtils.setInactiveChartBackground(binding.flDetailSixHours);
        PaintUtils.setInactiveChartBackground(binding.flDetailTwelveHours);
        PaintUtils.setInactiveChartBackground(binding.flDetailOneDay);
        PaintUtils.setInactiveChartBackground(binding.flDetailThreeDays);
        PaintUtils.setInactiveChartBackground(binding.flDetailAll);

        binding.txtDetailOneHour.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtDetailSixHours.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtDetailTwelveHours.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtDetailOneDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtDetailThreeDays.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtDetailAll.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));

        switch (position) {
            case 0: {
                PaintUtils.setChartBackground(binding.flDetailOneHour);
                binding.txtDetailOneHour.setTextColor(Color.WHITE);
                break;
            }
            case 1: {
                PaintUtils.setChartBackground(binding.flDetailSixHours);
                binding.txtDetailSixHours.setTextColor(Color.WHITE);
                break;
            }
            case 2: {
                PaintUtils.setChartBackground(binding.flDetailTwelveHours);
                binding.txtDetailTwelveHours.setTextColor(Color.WHITE);
                break;
            }
            case 3: {
                PaintUtils.setChartBackground(binding.flDetailOneDay);
                binding.txtDetailOneDay.setTextColor(Color.WHITE);
                break;
            }
            case 4: {
                PaintUtils.setChartBackground(binding.flDetailThreeDays);
                binding.txtDetailThreeDays.setTextColor(Color.WHITE);
                break;
            }
            case 5: {
                PaintUtils.setChartBackground(binding.flDetailAll);
                binding.txtDetailAll.setTextColor(Color.WHITE);
                break;
            }
        }
    }

    @Override
    public void setupChartData(List<Entry> data) {
        XAxis xAxis = binding.chartDetail.getXAxis();
        LineDataSet dataSet = new LineDataSet(data, "Template Data");
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setHighLightColor(ContextCompat.getColor(getApplicationContext(),
                App.isDark ? R.color.dark_chart_selection_line : R.color.light_chart_selection_line));

        int gradientRes;
        switch (App.theme) {
            case LightBlue:
                gradientRes = R.drawable.light_chart_gradient_blue;
                break;
            case LightGreen:
                gradientRes = R.drawable.light_chart_gradient_green;
                break;
            case LightOrange:
                gradientRes = R.drawable.light_chart_gradient_orange;
                break;
            case LightRed:
                gradientRes = R.drawable.light_chart_gradient_red;
                break;
            case LightPurple:
                gradientRes = R.drawable.light_chart_gradient_purple;
                break;
            case DarkPurple:
                gradientRes = R.drawable.dark_chart_gradient_purple;
                break;
            case DarkRed:
                gradientRes = R.drawable.dark_chart_gradient_red;
                break;
            case DarkOrange:
                gradientRes = R.drawable.dark_chart_gradient_orange;
                break;
            case DarkGreen:
                gradientRes = R.drawable.dark_chart_gradient_green;
                break;
            case DarkBlue:
                gradientRes = R.drawable.dark_chart_gradient_blue;
                break;
            default:
                gradientRes = 0;
        }
        dataSet.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), gradientRes));

        dataSet.setColor(ContextCompat.getColor(getApplicationContext(), PaintUtils.getColorByTheme()));

        LineData lineData = new LineData(dataSet);

        binding.chartDetail.setData(lineData);
        binding.chartDetail.invalidate();

        float xMax = binding.chartDetail.getData().getDataSetByIndex(0).getXMax();
        float xMin = 0f;
        xAxis.setAxisMaximum(xMax);
        xAxis.setAxisMinimum(xMin);
        binding.chartDetail.invalidate();
    }
}
