package fontana;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 9/4/2015.
 */
public class RobotoMediumTextView extends TextView {
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
