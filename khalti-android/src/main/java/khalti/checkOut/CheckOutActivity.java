package khalti.checkOut;

import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import khalti.R;
import khalti.utils.EmptyUtil;
import khalti.utils.MerchantUtil;
import khalti.utils.NetworkUtil;
import khalti.utils.ResourceUtil;
import khalti.utils.UserInterfaceUtil;
import khalti.utils.ViewPagerAdapter;
import rx.Observable;

public class CheckOutActivity extends AppCompatActivity implements CheckOutContract.View {

    private TabLayout tlTitle;
    private ViewPager vpContent;
    private AppBarLayout alTab;
    private FrameLayout flContainer;
    private FrameLayout flLoad;
    public CoordinatorLayout cdlMain;
    public Toolbar toolbar;
    private LinearLayout llIndented;
    private AppCompatTextView tvMessage;
    private MaterialButton btnTryAgain;

    private CheckOutContract.Presenter presenter;
    private List<TabLayout.Tab> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        tlTitle = findViewById(R.id.tlTitle);
        alTab = findViewById(R.id.alTab);
        flContainer = findViewById(R.id.flContainer);
        flLoad = findViewById(R.id.flLoad);
        vpContent = findViewById(R.id.vpContent);
        cdlMain = findViewById(R.id.cdlMain);
        llIndented = findViewById(R.id.llIndented);
        tvMessage = findViewById(R.id.tvMessage);
        btnTryAgain = findViewById(R.id.btnTryAgain);

        presenter = new CheckOutPresenter(this);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void toggleIndented(boolean show) {
        llIndented.setVisibility(show ? View.VISIBLE : View.GONE);
        flLoad.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        alTab.setVisibility(!show ? View.VISIBLE : View.GONE);
        vpContent.setVisibility(!show ? View.VISIBLE : View.GONE);
        tvMessage.setVisibility(!show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showIndentedNetworkError() {
        alTab.setVisibility(View.GONE);
        llIndented.setVisibility(View.VISIBLE);
        flLoad.setVisibility(View.INVISIBLE);
        btnTryAgain.setVisibility(View.INVISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(ResourceUtil.getString(this, R.string.network_error_body));
    }

    @Override
    public void showIndentedError(String error) {
        alTab.setVisibility(View.GONE);
        llIndented.setVisibility(View.VISIBLE);
        flLoad.setVisibility(View.INVISIBLE);
        btnTryAgain.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(error);
    }

    @Override
    public Observable<Void> setTryAgainClickListener() {
        return RxView.clicks(btnTryAgain);
    }

    @Override
    public void setupViewPager(List<String> types) {
        alTab.setVisibility(View.VISIBLE);
        flContainer.setVisibility(View.VISIBLE);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (String s : types) {
            HashMap<String, Object> map = MerchantUtil.getTab(s.toLowerCase());
            if (EmptyUtil.isNotNull(map)) {
                viewPagerAdapter.addFrag((Fragment) map.get("fragment"), map.get("title") + "");
            }
        }

        vpContent.setAdapter(viewPagerAdapter);
        vpContent.setOffscreenPageLimit(viewPagerAdapter.getCount());
        tlTitle.setupWithViewPager(vpContent);
        if (viewPagerAdapter.getCount() > 2) {
            tlTitle.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tlTitle.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    @Override
    public void setUpTabLayout(List<String> types) {
        alTab.setVisibility(View.VISIBLE);
        int position = 0;
        for (String s : types) {
            int color;
            HashMap<String, Object> map = MerchantUtil.getTab(s.toLowerCase());
            if (EmptyUtil.isNotNull(map)) {
                if (position == 0) {
                    color = ResourceUtil.getColor(this, R.color.khaltiAccentAlt);
                } else {
                    color = ResourceUtil.getColor(this, R.color.khaltiPrimary);
                }

                LinearLayout llTab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false);
                AppCompatTextView tvETitle = llTab.findViewById(R.id.tvTitle);
                ImageView ivEIcon = llTab.findViewById(R.id.ivIcon);

                tvETitle.setText((String) map.get("title"));
                tvETitle.setTextColor(color);
                ivEIcon.setImageResource((Integer) map.get("icon"));
                DrawableCompat.setTint(ivEIcon.getDrawable(), color);
                TabLayout.Tab tab = tlTitle.getTabAt(position);
                if (EmptyUtil.isNotNull(tab)) {
                    tab.setCustomView(llTab);
                }
                tabs.add(tab);

                position++;
            }
        }
    }

    @Override
    public void setTabListener() {
        tlTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpContent.setCurrentItem(tab.getPosition());
                presenter.onTabSelected(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                presenter.onTabSelected(tab.getPosition(), false);
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
    public boolean hasNetwork() {
        return NetworkUtil.isNetworkAvailable(this);
    }

    @Override
    public void closeCheckOut() {
        finish();
    }

    @Override
    public void setPresenter(CheckOutContract.Presenter presenter) {
        this.presenter = presenter;
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
