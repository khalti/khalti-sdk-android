package fontana;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 9/4/2015.
 */
public class SplashTextView extends TextView {
    public SplashTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    public SplashTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public SplashTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/tunga.ttf");
            setTypeface(roboto);
        }
    }
}
