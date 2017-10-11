package khalti.checkOut;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import khalti.R;
import khalti.checkOut.EBanking.EBanking;
import khalti.checkOut.Wallet.Wallet;
import khalti.rxBus.Event;
import khalti.rxBus.RxBus;
import khalti.utils.EmptyUtil;
import khalti.utils.ResourceUtil;
import khalti.utils.UserInterfaceUtil;
import khalti.utils.ViewPagerAdapter;
import rx.subscriptions.CompositeSubscription;

public class CheckOutActivity extends AppCompatActivity implements CheckOutContract.View {

    private TabLayout tlTitle;
    private ViewPager vpContent;
    public CoordinatorLayout cdlMain;
    public Toolbar toolbar;

    private CheckOutContract.Listener listener;
    private List<TabLayout.Tab> tabs = new ArrayList<>();
    CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        tlTitle = findViewById(R.id.tlTitle);
        vpContent = findViewById(R.id.vpContent);
        cdlMain = findViewById(R.id.cdlMain);

        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(RxBus.getInstance().register(Event.class, event -> {
            if (event.getTag().equals("close_check_out")) {
                finish();
            }
        }));
        listener = new CheckOutPresenter(this);
        listener.setUpLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EmptyUtil.isNotNull(compositeSubscription) && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        listener.dismissAllDialogs();
    }

    @Override
    public void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new EBanking(), ResourceUtil.getString(this, R.string.eBanking));
        viewPagerAdapter.addFrag(new Wallet(), ResourceUtil.getString(this, R.string.wallet));
        vpContent.setAdapter(viewPagerAdapter);

        tlTitle.setupWithViewPager(vpContent);
    }

    @Override
    public void setUpTabLayout() {
        LinearLayout eBankingTab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false);
        AppCompatTextView tvETitle = eBankingTab.findViewById(R.id.tvTitle);
        ImageView ivEIcon = eBankingTab.findViewById(R.id.ivIcon);

        tvETitle.setText(ResourceUtil.getString(this, R.string.eBanking));
        tvETitle.setTextColor(ResourceUtil.getColor(this, R.color.khaltiPrimary));
        ivEIcon.setImageResource(R.drawable.ic_account_balance_black_48px);
        DrawableCompat.setTint(ivEIcon.getDrawable(), ResourceUtil.getColor(this, R.color.khaltiPrimary));
        tlTitle.getTabAt(0).setCustomView(eBankingTab);

        LinearLayout walletTab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false);
        AppCompatTextView tvWTitle = walletTab.findViewById(R.id.tvTitle);
        ImageView ivWIcon = walletTab.findViewById(R.id.ivIcon);

        tvWTitle.setText(ResourceUtil.getString(this, R.string.wallet));
        tvWTitle.setTextColor(ResourceUtil.getColor(this, R.color.khaltiAccentAlt));
        ivWIcon.setImageResource(R.drawable.ic_account_balance_wallet_black_48px);
        DrawableCompat.setTint(ivWIcon.getDrawable(), ResourceUtil.getColor(this, R.color.khaltiAccentAlt));
        tlTitle.getTabAt(1).setCustomView(walletTab);

        tabs.add(tlTitle.getTabAt(0));
        tabs.add(tlTitle.getTabAt(1));

        tlTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpContent.setCurrentItem(tab.getPosition());
                listener.toggleTab(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                listener.toggleTab(tab.getPosition(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24px);
        }
    }

    @Override
    public void toggleTab(int position, boolean selected) {
        LinearLayout ll = (LinearLayout) tabs.get(position).getCustomView();
        if (EmptyUtil.isNotNull(ll)) {
            AppCompatTextView tvTitle = ll.findViewById(R.id.tvTitle);
            ImageView ivIcon = ll.findViewById(R.id.ivIcon);

            if (selected) {
                tvTitle.setTextColor(ResourceUtil.getColor(this, R.color.khaltiAccentAlt));
                DrawableCompat.setTint(ivIcon.getDrawable(), ResourceUtil.getColor(this, R.color.khaltiAccentAlt));
            } else {
                tvTitle.setTextColor(ResourceUtil.getColor(this, R.color.khaltiPrimary));
                DrawableCompat.setTint(ivIcon.getDrawable(), ResourceUtil.getColor(this, R.color.khaltiPrimary));
            }
        }
    }

    @Override
    public void setStatusBarColor() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = window.getDecorView().getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(flags);
            window.setStatusBarColor(ResourceUtil.getColor(this, R.color.bg));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < 23) {
                window.setStatusBarColor(ResourceUtil.getColor(this, R.color.khaltiPrimaryDark));
            }
        }
    }

    @Override
    public void dismissAllDialogs() {
        UserInterfaceUtil.dismissAllDialogs();
    }

    @Override
    public void setListener(CheckOutContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
