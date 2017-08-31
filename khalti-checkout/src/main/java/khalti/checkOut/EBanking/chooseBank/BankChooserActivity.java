package khalti.checkOut.EBanking.chooseBank;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.rxbus.Event;
import com.rxbus.RxBus;
import com.utila.EmptyUtil;
import com.utila.ResourceUtil;

import java.util.HashMap;
import java.util.List;

import khalti.R;
import rx.subscriptions.CompositeSubscription;

public class BankChooserActivity extends AppCompatActivity implements BankChooserContract.View, BankChooserAdapter.BankControls {

    private RecyclerView rvList;
    private Toolbar toolbar;

    private BankChooserContract.Listener listener;
    private BankChooserAdapter bankChooserAdapter;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_list);

        rvList = (RecyclerView) findViewById(R.id.rvList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        listener = new BankChooserPresenter(this);

        listener.setUpLayout();

        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(RxBus.getInstance().register(Event.class, event -> {
            if (event.getTag().equals("close_check_out")) {
                finish();
            }
        }));
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
        if (EmptyUtil.isNotNull(compositeSubscription) && !compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.secondaryText));
        searchEditText.setHint("Search Bank...");

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bankChooserAdapter.setFilter(newText);
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menu.findItem(R.id.action_search).setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public HashMap<?, ?> receiveArgument() {
        Bundle bundle = getIntent().getExtras();
        if (EmptyUtil.isNotNull(bundle)) {
            return (HashMap<?, ?>) bundle.getSerializable("map");
        }
        return null;
    }

    @Override
    public void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void setUpList(List<BankPojo> bankList) {
        bankChooserAdapter = new BankChooserAdapter(this, bankList, this);
        rvList.setAdapter(bankChooserAdapter);
        rvList.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
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
    public void setListener(BankChooserContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void chooseBank(String bankName, String bankId) {
        Intent intent = new Intent();
        intent.putExtra("name", bankName);
        intent.putExtra("id", bankId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
