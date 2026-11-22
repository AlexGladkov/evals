package com.dev.nicehash.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.PagerAdapter;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.fragments.BalanceFragment;
import com.dev.nicehash.app.fragments.DummyFragment;
import com.dev.nicehash.app.fragments.IncomeFragment;
import com.dev.nicehash.app.fragments.PayoutsFragment;
import com.dev.nicehash.app.fragments.WorkersFragment;
import com.dev.nicehash.app.interfaces.RouterProvider;
import com.dev.nicehash.app.presenters.MainPresenter;
import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.views.MainView;
import com.dev.nicehash.base.BaseActivity;
import com.dev.nicehash.databinding.ActivityMainBinding;
import com.dev.nicehash.domain.models.Balance;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.enums.Keys;
import com.dev.nicehash.enums.ScreenKeys;
import com.dev.nicehash.helpers.PaintUtils;
import com.dev.nicehash.helpers.SizeContract;
import com.dev.nicehash.helpers.WindowUtils;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.terrakok.cicerone.Back;
import com.github.terrakok.cicerone.Command;
import com.github.terrakok.cicerone.Navigator;
import com.github.terrakok.cicerone.NavigatorHolder;
import com.github.terrakok.cicerone.Replace;
import com.github.terrakok.cicerone.Router;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class MainActivity extends BaseActivity implements MainView, RouterProvider, SmartTabLayout.TabProvider {
    private final String TAG = MainActivity.class.getSimpleName();

    // MARK: - Injects
    @Inject
    Router routerMain;
    @Inject
    NavigatorHolder navigatorHolder;
    @Inject
    MinerRepository minerRepository;
    @Inject
    ExchangeServer exchangeServer;

    // MARK: - Presenter setup
    @InjectPresenter
    public MainPresenter mainPresenter;

    private ActivityMainBinding binding;

    // MARK: - Fragments setup
    private IncomeFragment incomeFragment = null;
    private BalanceFragment balanceFragment = null;
    private PayoutsFragment payoutsFragment = null;
    private WorkersFragment workersFragment = null;

    // MARK: - Variables
    private int currentPosition = 0;

    @ProvidePresenter
    public MainPresenter providePresenter() {
        return new MainPresenter(routerMain, minerRepository);
    }

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
            if (screenKey.equals(ScreenKeys.Choose.getValue())) {
                Intent chooseIntent = new Intent(getApplicationContext(), ChooseActivity.class);
                chooseIntent.putExtra(Keys.Configuration.getValue(), currentConfiguration);
                startActivity(chooseIntent);
                overridePendingTransition(0, 0);
            } else if (screenKey.equals(ScreenKeys.Detail.getValue())) {
//                        val balance = (command.transitionData as? Balance)
//                        balance.let {
//                            val detailIntent = Intent(applicationContext, DetailActivity::class.java)
//                            detailIntent.putExtra(Keys.Balance.value, it)
//                            startActivity(detailIntent)
//                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                        }
            } else if (screenKey.equals(ScreenKeys.Income.getValue())) {
//                        getSupportFragmentManager().beginTransaction()
//                                .detach(balanceFragment)
//                                .detach(payoutsFragment)
//                                .detach(workersFragment)
//                                .attach(incomeFragment)
//                                .commitNow()
            } else if (screenKey.equals(ScreenKeys.Balance.getValue())) {
//                        getSupportFragmentManager().beginTransaction()
//                                .detach(incomeFragment)
//                                .detach(payoutsFragment)
//                                .detach(workersFragment)
//                                .attach(balanceFragment)
//                                .commitNow()
            } else if (screenKey.equals(ScreenKeys.Payouts.getValue())) {
//                        getSupportFragmentManager().beginTransaction()
//                                .detach(balanceFragment)
//                                .detach(incomeFragment)
//                                .detach(workersFragment)
//                                .attach(payoutsFragment)
//                                .commitNow()
            } else if (screenKey.equals(ScreenKeys.Workers.getValue())) {
//                        getSupportFragmentManager().beginTransaction()
//                                .detach(balanceFragment)
//                                .detach(payoutsFragment)
//                                .detach(incomeFragment)
//                                .attach(workersFragment)
//                                .commitNow()
            }
        }
    }

    private Configuration currentConfiguration = Configuration.defaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainPresenter.handleConfiguration(getIntent() != null ? getIntent().getExtras() : null);

        binding.imgMainDevice.setOnClickListener(v -> mainPresenter.onDeviceClick());

        initFragments();
        tuneChart();

        binding.nsvMain.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
                mainPresenter.handleScrolling(scrollY,
                        WindowUtils.convertDpToPixel(SizeContract.daysBarHeight, getApplicationContext()),
                        WindowUtils.convertDpToPixel(SizeContract.introBarHeight, getApplicationContext())));

        Bundle extras = getIntent() != null ? getIntent().getExtras() : null;
        binding.txtMainTitle.setText(extras != null && extras.getString(Keys.MinerTitle.getValue()) != null
                ? extras.getString(Keys.MinerTitle.getValue()) : "");

        binding.flMainOneHour.setOnClickListener(v -> mainPresenter.onChartTimeClick(0,
                extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1,
                extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : ""));
        binding.flMainSixHours.setOnClickListener(v -> mainPresenter.onChartTimeClick(1,
                extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1,
                extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : ""));
        binding.flMainTwelveHours.setOnClickListener(v -> mainPresenter.onChartTimeClick(2,
                extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1,
                extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : ""));
        binding.flMainOneDay.setOnClickListener(v -> mainPresenter.onChartTimeClick(3,
                extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1,
                extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : ""));
        binding.flMainThreeDays.setOnClickListener(v -> mainPresenter.onChartTimeClick(4,
                extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1,
                extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : ""));
        binding.flMainAll.setOnClickListener(v -> mainPresenter.onChartTimeClick(5,
                extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1,
                extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : ""));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("", DummyFragment.class)
                .add("", DummyFragment.class)
                .add("", DummyFragment.class)
                .add("", DummyFragment.class)
                .create());

        binding.stbMain.setCustomTabView((SmartTabLayout.TabProvider) this);
        binding.vpMain.setAdapter(adapter);
        binding.stbMain.setViewPager(binding.vpMain);
        binding.stbMain.setOnPageChangeListener(new androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View oldTab = binding.stbMain.getTabAt(currentPosition);
                View currentTab = binding.stbMain.getTabAt(position);

                RelativeLayout oldView = oldTab.findViewById(R.id.tab_layout) instanceof RelativeLayout
                        ? (RelativeLayout) oldTab.findViewById(R.id.tab_layout) : null;
                RelativeLayout currentView = currentTab.findViewById(R.id.tab_layout) instanceof RelativeLayout
                        ? (RelativeLayout) currentTab.findViewById(R.id.tab_layout) : null;

                TextView textView = currentView != null && currentView.findViewById(R.id.tab_txt) instanceof TextView
                        ? (TextView) currentView.findViewById(R.id.tab_txt) : null;
                TextView oldTextView = oldView != null && oldView.findViewById(R.id.tab_txt) instanceof TextView
                        ? (TextView) oldView.findViewById(R.id.tab_txt) : null;

                switch (position) {
                    case 0:
                        textView.setText(getString(R.string.tab_income));
                        break;
                    case 1:
                        textView.setText(getString(R.string.tab_balance));
                        break;
                    case 2:
                        textView.setText(getString(R.string.tab_payouts));
                        break;
                    case 3:
                        textView.setText(getString(R.string.tab_workers));
                        break;
                }

                oldView.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.tab_shape_transparent));
                oldTextView.setAlpha(0.3f);

                currentView.setBackground(ContextCompat.getDrawable(getApplicationContext(), App.isDark
                        ? R.drawable.tab_shape_dark
                        : R.drawable.tab_shape_light));

                oldView.setBackground(currentPosition == position
                        ? ContextCompat.getDrawable(getApplicationContext(), App.isDark ? R.drawable.tab_shape_dark
                        : R.drawable.tab_shape_light)
                        : ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_shape_transparent));

                textView.setAlpha(1f);

                currentPosition = position;
                mainPresenter.onTabClick(position);
            }
        });

        mainPresenter.onTabClick(0);
    }

    private void tuneChart() {
        binding.chartMain.fitScreen();
        binding.chartMain.getDescription().setEnabled(false);
        binding.chartMain.getLegend().setEnabled(false);
        binding.chartMain.setViewPortOffsets(0f, 0f, 0f, 0f);
        binding.chartMain.setDoubleTapToZoomEnabled(false);

        XAxis xAxis = binding.chartMain.getXAxis();
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

        binding.chartMain.getAxisRight().setEnabled(false);
        binding.chartMain.getAxisRight().setSpaceBottom(0f);
        binding.chartMain.getXAxis().setEnabled(false);

        binding.chartMain.getAxisLeft().setEnabled(true);
        binding.chartMain.getAxisLeft().addLimitLine(averageLine);
        binding.chartMain.getAxisLeft().setLabelCount(9, true);
        binding.chartMain.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        binding.chartMain.getAxisLeft().setGridColor(App.isDark
                ? Color.parseColor("#1AFFFFFF")
                : Color.parseColor("#0D000000"));
        binding.chartMain.getAxisLeft().setGridLineWidth(0.5f);
        binding.chartMain.getAxisLeft().setTextSize(10f);
        binding.chartMain.getAxisLeft().setDrawTopYLabelEntry(false);
        binding.chartMain.getAxisLeft().setDrawZeroLine(false);
        binding.chartMain.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, com.github.mikephil.charting.components.AxisBase axisBase) {
                if (value < 0.0000001f) {
                    return "";
                }

                return String.format("%.05f", value);
            }
        });
        binding.chartMain.getAxisLeft().setAxisLineColor(ContextCompat.getColor(getApplicationContext(),
                App.isDark ? R.color.dark_background_secondary : R.color.light_background_secondary));

        binding.chartMain.getAxisLeft().setTextColor(ContextCompat.getColor(getApplicationContext(),
                App.isDark ? R.color.dark_text_color_primary : R.color.light_text_color_primary));
    }

    private void initFragments() {
        incomeFragment = (IncomeFragment) (getSupportFragmentManager().findFragmentByTag(ScreenKeys.Income.getValue()) instanceof IncomeFragment
                ? getSupportFragmentManager().findFragmentByTag(ScreenKeys.Income.getValue()) : null);
        if (incomeFragment == null) {
            Bundle extras = getIntent() != null ? getIntent().getExtras() : null;
            incomeFragment = IncomeFragment.getNewInstance(ScreenKeys.Income.getValue(),
                    extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : "",
                    extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flMain, incomeFragment, ScreenKeys.Income.getValue())
                    .detach(incomeFragment)
                    .commitNow();
        }

        balanceFragment = (BalanceFragment) (getSupportFragmentManager().findFragmentByTag(ScreenKeys.Balance.getValue()) instanceof BalanceFragment
                ? getSupportFragmentManager().findFragmentByTag(ScreenKeys.Balance.getValue()) : null);
        if (balanceFragment == null) {
            Bundle extras = getIntent() != null ? getIntent().getExtras() : null;
            balanceFragment = BalanceFragment.getNewInstance(ScreenKeys.Balance.getValue(),
                    extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : "",
                    extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flMain, balanceFragment, ScreenKeys.Balance.getValue())
                    .detach(balanceFragment)
                    .commitNow();
        }

        payoutsFragment = (PayoutsFragment) (getSupportFragmentManager().findFragmentByTag(ScreenKeys.Payouts.getValue()) instanceof PayoutsFragment
                ? getSupportFragmentManager().findFragmentByTag(ScreenKeys.Payouts.getValue()) : null);
        if (payoutsFragment == null) {
            Bundle extras = getIntent() != null ? getIntent().getExtras() : null;
            payoutsFragment = PayoutsFragment.getNewInstance(ScreenKeys.Payouts.getValue(),
                    extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : "",
                    extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flMain, payoutsFragment, ScreenKeys.Payouts.getValue())
                    .detach(payoutsFragment)
                    .commitNow();
        }

        workersFragment = (WorkersFragment) (getSupportFragmentManager().findFragmentByTag(ScreenKeys.Workers.getValue()) instanceof WorkersFragment
                ? getSupportFragmentManager().findFragmentByTag(ScreenKeys.Workers.getValue()) : null);
        if (workersFragment == null) {
            Bundle extras = getIntent() != null ? getIntent().getExtras() : null;
            workersFragment = WorkersFragment.getNewInstance(ScreenKeys.Workers.getValue(),
                    extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : "",
                    extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flMain, workersFragment, ScreenKeys.Workers.getValue())
                    .detach(workersFragment)
                    .commitNow();
        }
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.isMainNeedsToReload) {
            App.isMainNeedsToReload = false;
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            Bundle extras = getIntent() != null ? getIntent().getExtras() : null;
            mainIntent.putExtra(Keys.Configuration.getValue(),
                    extras != null && extras.get(Keys.Configuration.getValue()) instanceof Configuration
                            ? (Configuration) extras.get(Keys.Configuration.getValue()) : null);
            mainIntent.putExtra(Keys.Miner.getValue(),
                    extras != null && extras.get(Keys.Miner.getValue()) instanceof String
                            ? (String) extras.get(Keys.Miner.getValue()) : null);
            mainIntent.putExtra(Keys.MinerTitle.getValue(),
                    extras != null && extras.get(Keys.MinerTitle.getValue()) instanceof String
                            ? (String) extras.get(Keys.MinerTitle.getValue()) : null);
            startActivity(mainIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    // MARK: - RouterProvider implementation
    @Override
    public Router getRouter() {
        return routerMain;
    }

    // MARK: - Smart layout implementation
    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View tab = inflater.inflate(R.layout.tab_item, container, false);
        TextView textView = (TextView) tab.findViewById(R.id.tab_txt);
        RelativeLayout layoutView = (RelativeLayout) tab.findViewById(R.id.tab_layout);

        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), App.isDark
                ? R.color.dark_text_color_primary
                : R.color.light_text_color_primary));

        layoutView.setBackground(currentPosition == position
                ? ContextCompat.getDrawable(getApplicationContext(), App.isDark ? R.drawable.tab_shape_dark
                : R.drawable.tab_shape_light)
                : ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_shape_transparent));

        switch (position) {
            case 0:
                textView.setText(getString(R.string.tab_income));
                break;
            case 1:
                textView.setText(getString(R.string.tab_balance));
                break;
            case 2:
                textView.setText(getString(R.string.tab_payouts));
                break;
            case 3:
                textView.setText(getString(R.string.tab_workers));
                break;
        }

        if (currentPosition != position) textView.setAlpha(0.3f);

        return tab;
    }

    // MARK: - View implementation
    @Override
    public void displayNavigationInfo(String leftValue, String rightValue) {
        binding.txtMainNavLeftTitle.setText(leftValue);
        binding.txtMainNavRightTitle.setText(rightValue);
    }

    @Override
    public void setupNavigation(int position) {
        switch (position) {
            case 0: {
                binding.txtMainNavLeftSubtitle.setText(getString(R.string.navigation_bar_income_left));
                binding.txtMainNavRightSubtitle.setText("RUB/Day");
                break;
            }
            case 1: {
                binding.txtMainNavLeftSubtitle.setText(getString(R.string.navigation_bar_balance_left));
                binding.txtMainNavRightSubtitle.setText(getString(R.string.navigation_bar_balance_right));
                break;
            }
            case 2: {
                binding.txtMainNavLeftSubtitle.setText(getString(R.string.navigation_bar_payout_left));
                binding.txtMainNavRightSubtitle.setText(getString(R.string.navigation_bar_payout_right));
                break;
            }
            case 3: {
                binding.txtMainNavLeftSubtitle.setText(getString(R.string.navigation_bar_workers_left));
                binding.txtMainNavRightSubtitle.setText(getString(R.string.navigation_bar_workers_right));
                break;
            }
        }
    }

    @Override
    public void setupConfiguration(Configuration configuration) {
        this.currentConfiguration = configuration;
        binding.vpMain.setCurrentItem(configuration.getDefaultTab());
        int screenHeight = WindowUtils.getScreenHeight(this);

        switch (configuration.getDefaultTab()) {
            case 0:
                mainPresenter.measureChartHeight((float) screenHeight,
                        WindowUtils.convertDpToPixel(80f + 80f + 40f + 3f, getApplicationContext()),
                        WindowUtils.convertDpToPixel(72f + 40f + 80f + 40f, getApplicationContext()));
                break;
            case 1:
                mainPresenter.measureChartHeight((float) screenHeight,
                        WindowUtils.convertDpToPixel(113f + 113f + 40f, getApplicationContext()),
                        WindowUtils.convertDpToPixel(72f + 40f + 80f, getApplicationContext()));
                break;
            case 2:
                mainPresenter.measureChartHeight((float) screenHeight,
                        WindowUtils.convertDpToPixel(63f + 63f + 40f, getApplicationContext()),
                        WindowUtils.convertDpToPixel(72f + 40f + 80f, getApplicationContext()));
                break;
            case 3:
                mainPresenter.measureChartHeight((float) screenHeight, 0f, 0f);
                break;
        }

        Bundle extras = getIntent() != null ? getIntent().getExtras() : null;
        mainPresenter.onChartTimeClick(0,
                extras != null ? extras.getInt(Keys.MinerID.getValue(), -1) : -1,
                extras != null && extras.getString(Keys.Miner.getValue()) != null ? extras.getString(Keys.Miner.getValue()) : "");
    }

    @Override
    public void setupChartHeight(float value) {
        ViewGroup.LayoutParams layoutParams = binding.flChart.getLayoutParams();
        layoutParams.height = (int) value;
        binding.flChart.setLayoutParams(layoutParams);
    }

    @Override
    public void setupChartView(int position) {
        PaintUtils.setInactiveChartBackground(binding.flMainOneHour);
        PaintUtils.setInactiveChartBackground(binding.flMainSixHours);
        PaintUtils.setInactiveChartBackground(binding.flMainTwelveHours);
        PaintUtils.setInactiveChartBackground(binding.flMainOneDay);
        PaintUtils.setInactiveChartBackground(binding.flMainThreeDays);
        PaintUtils.setInactiveChartBackground(binding.flMainAll);

        binding.txtMainOneHour.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtMainSixHours.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtMainTwelveHours.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtMainOneDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtMainThreeDays.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));
        binding.txtMainAll.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_hint));

        switch (position) {
            case 0: {
                PaintUtils.setChartBackground(binding.flMainOneHour);
                binding.txtMainOneHour.setTextColor(Color.WHITE);
                break;
            }
            case 1: {
                PaintUtils.setChartBackground(binding.flMainSixHours);
                binding.txtMainSixHours.setTextColor(Color.WHITE);
                break;
            }
            case 2: {
                PaintUtils.setChartBackground(binding.flMainTwelveHours);
                binding.txtMainTwelveHours.setTextColor(Color.WHITE);
                break;
            }
            case 3: {
                PaintUtils.setChartBackground(binding.flMainOneDay);
                binding.txtMainOneDay.setTextColor(Color.WHITE);
                break;
            }
            case 4: {
                PaintUtils.setChartBackground(binding.flMainThreeDays);
                binding.txtMainThreeDays.setTextColor(Color.WHITE);
                break;
            }
            case 5: {
                PaintUtils.setChartBackground(binding.flMainAll);
                binding.txtMainAll.setTextColor(Color.WHITE);
                break;
            }
        }
    }

    @Override
    public void setupIntroBar(float introBarPosition) {
        binding.llMainTopBar.setTranslationY(introBarPosition);
    }

    @Override
    public void setupTabBar(float tabBarPosition) {
        binding.stbMain.setTranslationY(tabBarPosition);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(int message) {
        Toast.makeText(getApplicationContext(), getString(message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setupChartData(List<Entry> data) {
        XAxis xAxis = binding.chartMain.getXAxis();
        LineDataSet dataSet = new LineDataSet(data, "Template Data");
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setHighLightColor(ContextCompat.getColor(getApplicationContext(),
                App.isDark ? R.color.dark_chart_selection_line : R.color.light_chart_selection_line));

        dataSet.setFillDrawable(ContextCompat.getDrawable(getApplicationContext(), PaintUtils.getChartGradient()));
        dataSet.setColor(ContextCompat.getColor(getApplicationContext(), PaintUtils.getColorByTheme()));

        LineData lineData = new LineData(dataSet);

        binding.chartMain.setData(lineData);
        binding.chartMain.invalidate();

        float xMax = binding.chartMain.getData().getDataSetByIndex(0).getXMax();
        float xMin = 0f;
        xAxis.setAxisMaximum(xMax);
        xAxis.setAxisMinimum(xMin);
        binding.chartMain.invalidate();
    }
}
