package com.khalti.widget.basic;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.khalti.R;
import com.khalti.carbonX.widget.Button;
import com.khalti.carbonX.widget.FrameLayout;
import com.khalti.form.CheckOutActivity;
import com.utila.ActivityUtil;

public class Pay extends FrameLayout {
    private Context context;
    private AttributeSet attrs;
    private LayoutInflater inflater;

    private PayContract.Listener listener;
    private Button btnPay;

    public Pay(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public Pay(@NonNull Context context, @Nullable AttributeSet attrs) {
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

    public void setAmount(Double amount) {
        listener.setAmount(amount);
    }

    private void init() {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.khalti, 0, 0);
        String buttonText = a.getString(R.styleable.khalti_text);
        a.recycle();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainView = inflater.inflate(R.layout.component_button, this, true);

        btnPay = mainView.findViewById(R.id.btnPay);
        listener.setButtonText(buttonText);

        btnPay.setOnClickListener(v -> listener.openForm());
    }

    private class BasicPay implements PayContract.View {
        private PayContract.Listener listener;

        BasicPay() {
            listener = new PayPresenter(this);
        }

        @Override
        public void setButtonText(String text) {
            btnPay.setText(text);
        }

        @Override
        public void openForm() {
            ActivityUtil.openActivity(CheckOutActivity.class, context, false, null, true);
        }

        @Override
        public void setListener(PayContract.Listener listener) {
            this.listener = listener;
        }

        PayContract.Listener getListener() {
            return listener;
        }
    }
}
