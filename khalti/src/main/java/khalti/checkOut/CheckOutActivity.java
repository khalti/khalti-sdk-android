package khalti.checkOut;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.khalti.R;
import com.rxbus.Event;
import com.rxbus.RxBus;
import com.utila.EmptyUtil;
import com.utila.LogUtil;
import com.utila.ResourceUtil;
import com.utila.UserInterfaceUtil;

import java.util.ArrayList;
import java.util.List;

import fontana.RobotoMediumTextView;
import khalti.checkOut.EBanking.EBanking;
import khalti.checkOut.Wallet.Wallet;
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

        tlTitle = (TabLayout) findViewById(R.id.tlTitle);
        vpContent = (ViewPager) findViewById(R.id.vpContent);
        cdlMain = (CoordinatorLayout) findViewById(R.id.cdlMain);

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
            LogUtil.checkpoint("UnSubscribe");
            compositeSubscription.unsubscribe();
        }
        listener.dismissAllDialogs();
    }

    @Override
    public void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new Wallet(), ResourceUtil.getString(this, R.string.wallet));
        viewPagerAdapter.addFrag(new EBanking(), ResourceUtil.getString(this, R.string.eBanking));
        vpContent.setAdapter(viewPagerAdapter);

        tlTitle.setupWithViewPager(vpContent);
    }

    @Override
    public void setUpTabLayout() {
        LinearLayout walletTab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false);
        RobotoMediumTextView tvWTitle = walletTab.findViewById(R.id.tvTitle);
        ImageView ivWIcon = walletTab.findViewById(R.id.ivIcon);

        tvWTitle.setText(ResourceUtil.getString(this, R.string.wallet));
        ivWIcon.setImageResource(R.drawable.ic_account_balance_wallet_black_48px);
        tlTitle.getTabAt(0).setCustomView(walletTab);

        LinearLayout eBankingTab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false);
        RobotoMediumTextView tvETitle = eBankingTab.findViewById(R.id.tvTitle);
        ImageView ivEIcon = eBankingTab.findViewById(R.id.ivIcon);

        tvETitle.setText(ResourceUtil.getString(this, R.string.eBanking));
        tvETitle.setTextColor(ResourceUtil.getColor(this, R.color.primaryText));
        ivEIcon.setImageResource(R.drawable.ic_account_balance_black_48px);
        ivEIcon.setAlpha(0.6f);
        tlTitle.getTabAt(1).setCustomView(eBankingTab);

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
            RobotoMediumTextView tvTitle = (RobotoMediumTextView) ll.findViewById(R.id.tvTitle);
            ImageView ivIcon = (ImageView) ll.findViewById(R.id.ivIcon);

            if (selected) {
                tvTitle.setTextColor(ResourceUtil.getColor(this, R.color.black));
                ivIcon.setAlpha(1f);
            } else {
                tvTitle.setTextColor(ResourceUtil.getColor(this, R.color.primaryText));
                ivIcon.setAlpha(0.6f);
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
