package khalti.checkOut;

import android.content.Context;
import android.support.annotation.Keep;

import com.rxbus.RxBus;
import com.utila.ActivityUtil;
import com.utila.EmptyUtil;

import khalti.checkOut.api.Config;
import khalti.utils.DataHolder;

@Keep
public class KhaltiCheckOut implements KhaltiCheckOutInterface {
    private Context context;

    public KhaltiCheckOut(Context context) {
        this.context = context;
    }

    public KhaltiCheckOut(Context context, Config config) {
        DataHolder.setConfig(config);
        this.context = context;
    }

    @Override
    public void show() {
        if (EmptyUtil.isNull(DataHolder.getConfig())) {
            throw new IllegalArgumentException("Config not set");
        }
        ActivityUtil.openActivity(CheckOutActivity.class, context, null, true);
    }

    @Override
    public void show(Config config) {
        DataHolder.setConfig(config);
        if (EmptyUtil.isNull(DataHolder.getConfig())) {
            throw new IllegalArgumentException("Config not set");
        }
        ActivityUtil.openActivity(CheckOutActivity.class, context, null, true);
    }

    @Override
    public void destroy() {
        RxBus.getInstance().post("close_check_out", null);
    }
}
