package com.khalti.form;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.khalti.R;
import com.khalti.form.EBanking.EBanking;
import com.khalti.form.Wallet.Wallet;
import com.khalti.utils.ViewPagerAdapter;
import com.utila.ResourceUtil;

public class CheckOutActivity extends AppCompatActivity implements CheckOutContract.View {

    private TabLayout tlTitle;
    private ViewPager vpContent;

    private CheckOutContract.Listener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        tlTitle = (TabLayout) findViewById(R.id.tlTitle);
        vpContent = (ViewPager) findViewById(R.id.vpContent);

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
    public void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new EBanking(), ResourceUtil.getString(this, R.string.eBanking));
        viewPagerAdapter.addFrag(new Wallet(), ResourceUtil.getString(this, R.string.wallet));
        vpContent.setAdapter(viewPagerAdapter);
    }

    @Override
    public void attachListenerToTabLayout() {
        tlTitle.setupWithViewPager(vpContent);

        tlTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpContent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void setListener(CheckOutContract.Listener listener) {
        this.listener = listener;
    }
}
