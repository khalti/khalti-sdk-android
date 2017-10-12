package khalti.checkOut;

import android.content.Context;
import android.support.annotation.Keep;

import khalti.checkOut.api.Config;
import khalti.rxBus.RxBus;
import khalti.utils.ActivityUtil;
import khalti.utils.EmptyUtil;
import khalti.utils.Store;

@Keep
public class KhaltiCheckOut implements KhaltiCheckOutInterface {
    private Context context;

    public KhaltiCheckOut(Context context) {
        this.context = context;
    }

    public KhaltiCheckOut(Context context, Config config) {
        Store.setConfig(config);
        this.context = context;
    }

    @Override
    public void show() {
        if (EmptyUtil.isNull(Store.getConfig())) {
            throw new IllegalArgumentException("Config not set");
        }
        ActivityUtil.openActivity(CheckOutActivity.class, context, null, true);
    }

    @Override
    public void show(Config config) {
        Store.setConfig(config);
        if (EmptyUtil.isNull(Store.getConfig())) {
            throw new IllegalArgumentException("Config not set");
        }
        ActivityUtil.openActivity(CheckOutActivity.class, context, null, true);
    }

    @Override
    public void destroy() {
        RxBus.getInstance().post("close_check_out", null);
    }
}
