package fontana;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 9/4/2015.
 */
public class MaterialIconTextView extends TextView {
    public MaterialIconTextView(Context context, AttributeSet attributeSet, int defstyle) {
        super(context, attributeSet, defstyle);
        init();
    }

    public MaterialIconTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MaterialIconTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface materialIcon = Typeface.createFromAsset(getContext().getAssets(), "fonts/material.ttf");
            setTypeface(materialIcon);
        }
    }
}
