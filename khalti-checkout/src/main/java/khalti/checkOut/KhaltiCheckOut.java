package khalti.checkOut;

import android.content.Context;
import android.support.annotation.Keep;

import java.util.HashMap;

import khalti.checkOut.api.Config;
import khalti.rxBus.RxBus;
import khalti.utils.ActivityUtil;
import khalti.utils.EmptyUtil;
import khalti.utils.LogUtil;

@Keep
public class KhaltiCheckOut implements KhaltiCheckOutInterface {
    private Context context;
    private Config config;

    public KhaltiCheckOut(Context context) {
        this.context = context;
    }

    public KhaltiCheckOut(Context context, Config config) {
        this.config = config;
        this.context = context;
    }

    @Override
    public void show() {
        LogUtil.checkpoint("show");
        if (EmptyUtil.isNull(config)) {
            throw new IllegalArgumentException("Config not set");
        }
        HashMap<String, Config> map = new HashMap<>();
        map.put("config", config);
        ActivityUtil.openActivity(CheckOutActivity.class, context, map, true);
    }

    @Override
    public void show(Config config) {
        this.config = config;
        if (EmptyUtil.isNull(config)) {
            throw new IllegalArgumentException("Config not set");
        }
        HashMap<String, Config> map = new HashMap<>();
        map.put("config", config);
        ActivityUtil.openActivity(CheckOutActivity.class, context, map, true);
    }

    @Override
    public void destroy() {
        RxBus.getInstance().post("close_check_out", null);
    }
}
