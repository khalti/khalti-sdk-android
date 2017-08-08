package com.khalti.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.khalti.R;
import com.khalti.carbonX.widget.FrameLayout;

public class Khalti extends FrameLayout {
    private Context context;
    private AttributeSet attrs;
    private LayoutInflater inflater;

    public Khalti(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public Khalti(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainView = inflater.inflate(R.layout.component_button, this, true);
    }
}
