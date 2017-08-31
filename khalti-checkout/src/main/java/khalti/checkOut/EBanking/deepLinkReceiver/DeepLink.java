package khalti.checkOut.EBanking.deepLinkReceiver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.utila.EmptyUtil;
import com.utila.JsonUtil;
import com.utila.StorageUtil;

import java.util.HashMap;

import khalti.R;
import khalti.checkOut.api.Config;

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
        return (Config) StorageUtil.readFromFile(this, "khalti_config");
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
