package fontana;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RobotoMediumTextView extends android.support.v7.widget.AppCompatTextView {
    public RobotoMediumTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    public RobotoMediumTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RobotoMediumTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto-medium.ttf");
            setTypeface(roboto);
        }
    }
}
