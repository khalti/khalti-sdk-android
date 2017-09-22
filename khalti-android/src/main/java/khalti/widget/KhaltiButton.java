package khalti.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import khalti.R;
import khalti.carbonX.widget.FrameLayout;
import khalti.checkOut.KhaltiCheckOut;
import khalti.checkOut.api.Config;
import khalti.utils.EmptyUtil;

@Keep
public class KhaltiButton extends FrameLayout implements KhaltiButtonInterface {
    private Context context;
    private AttributeSet attrs;

    private Config config;

    private PayContract.Listener listener;
    private android.widget.FrameLayout flCustomView;
    private FrameLayout flStyle;
    private khalti.carbonX.widget.Button btnPay;
    private View customView;
    private int buttonStyle;

    public KhaltiButton(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public KhaltiButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;

        Pay pay = new Pay();
        listener = pay.getListener();
        init();
    }

    @Override
    public void setText(String text) {
        listener.setButtonText(text);
    }

    @Override
    public void setCheckOutConfig(Config config) {
        this.config = config;
        String message = listener.checkConfig(config);
        if (EmptyUtil.isNotNull(message)) {
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void setCustomView(View view) {
        this.customView = view;
        listener.setCustomButtonView();
        listener.setButtonClick();
    }

    @Override
    public void setButtonStyle(ButtonStyle buttonStyle) {
        this.buttonStyle = buttonStyle.getId();
        listener.setButtonStyle(this.buttonStyle);
        listener.setButtonClick();
    }

    @Override
    public void destroyCheckOut() {
        listener.destroyCheckOut();
    }

    private void init() {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.khalti, 0, 0);
        String buttonText = a.getString(R.styleable.khalti_text);
        buttonStyle = a.getInt(R.styleable.khalti_button_style, -1);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainView = inflater.inflate(R.layout.component_button, this, true);

        btnPay = mainView.findViewById(R.id.btnPay);
        flCustomView = mainView.findViewById(R.id.flCustomView);
        flStyle = mainView.findViewById(R.id.flStyle);

        listener.setButtonText(buttonText);
        listener.setButtonStyle(buttonStyle);
        listener.setButtonClick();
    }

    private class Pay implements PayContract.View {
        private PayContract.Listener listener;
        private KhaltiCheckOut khaltiCheckOut;

        Pay() {
            listener = new PayPresenter(this);
        }

        @Override
        public void setCustomButtonView() {
            btnPay.setVisibility(View.GONE);
            flCustomView.addView(customView);
        }

        @Override
        public void setButtonStyle(int id) {
            int imageId = -1;
            switch (id) {
                case 0:
                    imageId = R.mipmap.ebanking_dark;
                    break;
                case 1:
                    imageId = R.mipmap.ebanking_light;
                    break;
            }
            if (imageId != -1) {
                btnPay.setVisibility(GONE);
                flCustomView.setVisibility(View.GONE);
                flStyle.setBackgroundResource(imageId);
            }
        }

        @Override
        public void setButtonText(String text) {
            btnPay.setText(text);
        }

        @Override
        public void setButtonClick() {
            if (EmptyUtil.isNotNull(customView)) {
                flCustomView.getChildAt(0).setOnClickListener(view -> listener.openForm());
            } else if (buttonStyle != -1) {
                flStyle.setOnClickListener(view -> listener.openForm());
            } else {
                btnPay.setOnClickListener(view -> listener.openForm());
            }
        }

        @Override
        public void openForm() {
            khaltiCheckOut = new KhaltiCheckOut(context, config);
            khaltiCheckOut.show();
        }

        @Override
        public void destroyCheckOut() {
            if (EmptyUtil.isNull(khaltiCheckOut)) {
                throw new IllegalArgumentException("CheckOut cannot be destroyed before it is shown.");
            }
            khaltiCheckOut.destroy();
        }

        PayContract.Listener getListener() {
            return listener;
        }

        @Override
        public void setListener(PayContract.Listener listener) {
            this.listener = listener;
        }
    }
}
