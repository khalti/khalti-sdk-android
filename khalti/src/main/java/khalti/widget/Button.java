package khalti.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.khalti.R;
import com.utila.ActivityUtil;
import com.utila.EmptyUtil;

import java.util.HashMap;

import khalti.carbonX.widget.FrameLayout;
import khalti.form.CheckOutActivity;
import khalti.form.api.Config;
import khalti.utils.DataHolder;

public class Button extends FrameLayout {
    private Context context;
    private AttributeSet attrs;

    private ButtonContract.Listener listener;
    private android.widget.FrameLayout flCustomView;
    private FrameLayout flStyle;
    private ImageView ivButtoStyle;
    private khalti.carbonX.widget.Button btnPay;
    private View customView;
    private int buttonStyle;

    public Button(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public Button(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;

        BasicPay basicPay = new BasicPay();
        listener = basicPay.getListener();
        init();
    }

    public void setText(String text) {
        listener.setButtonText(text);
    }

    public void setConfig(Config config) {
        listener.setConfig(config);
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        DataHolder.setOnSuccessListener(onSuccessListener);
    }

    public void setCustomView(View view) {
        this.customView = view;
        listener.setCustomButtonView();
        listener.setButtonClick();
    }

    public void setButtonStyle(ButtonStyle buttonStyle) {
        this.buttonStyle = buttonStyle.getId();
        listener.setButtonStyle(this.buttonStyle);
        listener.setButtonClick();
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

    public interface OnSuccessListener {
        void onSuccess(HashMap<String, Object> data);
    }

    private class BasicPay implements ButtonContract.View {
        private ButtonContract.Listener listener;

        BasicPay() {
            listener = new ButtonPresenter(this);
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
                    imageId = R.mipmap.full_button;
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
            if (EmptyUtil.isNull(DataHolder.getConfig())) {
                throw new IllegalArgumentException("Config not set");
            }
            if (EmptyUtil.isNull(DataHolder.getOnSuccessListener())) {
                throw new IllegalArgumentException("OnSuccessListener not set");
            }
            ActivityUtil.openActivity(CheckOutActivity.class, context, false, null, true);
        }

        ButtonContract.Listener getListener() {
            return listener;
        }

        @Override
        public void setListener(ButtonContract.Listener listener) {
            this.listener = listener;
        }
    }
}
