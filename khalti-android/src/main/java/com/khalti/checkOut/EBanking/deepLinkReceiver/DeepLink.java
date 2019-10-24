package com.khalti.checkOut.EBanking.deepLinkReceiver;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import com.khalti.R;
import com.khalti.checkOut.helper.Config;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.FileStorageUtil;
import com.khalti.utils.JsonUtil;

public class DeepLink extends AppCompatActivity implements DeepLinkContract.View {

    private DeepLinkContract.Listener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);

        listener = new DeepLinkPresenter(this);
        listener.receiveEBankingData();
    }

    @Override
    public HashMap<String, Object> receiveEBankingData() {
        Bundle bundle = getIntent().getExtras();
        if (EmptyUtil.isNotNull(bundle)) {
            return JsonUtil.getEBankingData(bundle.getString("data"));
        }
        return null;
    }

    @Override
    public Config getConfigFromFile() {
        return (Config) FileStorageUtil.readFromFile(this, "khalti_config");
    }

    @Override
    public void closeDeepLink() {
        finish();
    }

    @Override
    public void setListener(DeepLinkContract.Listener listener) {
        this.listener = listener;
    }
}
