package fontana;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 9/4/2015.
 */
public class KhaltiTextView extends TextView {
    public KhaltiTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    public KhaltiTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public KhaltiTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/museo500.otf");
            setTypeface(roboto);
        }
    }
}
