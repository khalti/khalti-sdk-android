package com.khalti.form;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.khalti.R;
import com.khalti.carbonX.widget.FrameLayout;
import com.khalti.form.EBanking.EBanking;
import com.khalti.form.Wallet.Wallet;
import com.khalti.utils.ViewPagerAdapter;
import com.utila.EmptyUtil;
import com.utila.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import fontana.RobotoMediumTextView;

public class CheckOutActivity extends AppCompatActivity implements CheckOutContract.View {

    private TabLayout tlTitle;
    private ViewPager vpContent;

    public CoordinatorLayout cdlMain;

    private CheckOutContract.Listener listener;
    private List<TabLayout.Tab> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        tlTitle = (TabLayout) findViewById(R.id.tlTitle);
        vpContent = (ViewPager) findViewById(R.id.vpContent);
        FrameLayout flClose = (FrameLayout) findViewById(R.id.flClose);
        cdlMain = (CoordinatorLayout) findViewById(R.id.cdlMain);

        listener = new CheckOutPresenter(this);
        listener.setUpLayout();
        flClose.setOnClickListener(view -> listener.closeForm());
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
        RobotoMediumTextView tvETitle = (RobotoMediumTextView) eBankingTab.findViewById(R.id.tvTitle);
        ImageView ivEIcon = (ImageView) eBankingTab.findViewById(R.id.ivIcon);

        tvETitle.setText(ResourceUtil.getString(this, R.string.eBanking));
        ivEIcon.setImageResource(R.drawable.ic_account_balance_black_48px);
        tlTitle.getTabAt(0).setCustomView(eBankingTab);

        LinearLayout walletTab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false);
        RobotoMediumTextView tvWTitle = (RobotoMediumTextView) walletTab.findViewById(R.id.tvTitle);
        ImageView ivWIcon = (ImageView) walletTab.findViewById(R.id.ivIcon);

        tvWTitle.setText(ResourceUtil.getString(this, R.string.wallet));
        tvWTitle.setTextColor(ResourceUtil.getColor(this, R.color.primaryText));
        ivWIcon.setImageResource(R.drawable.ic_account_balance_wallet_black_48px);
        ivWIcon.setAlpha(0.6f);
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
    public void closeForm() {
        finish();
    }

    @Override
    public void setListener(CheckOutContract.Listener listener) {
        this.listener = listener;
    }
}
