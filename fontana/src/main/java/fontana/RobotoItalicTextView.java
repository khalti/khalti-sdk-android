package fontana;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 9/4/2015.
 */
public class RobotoItalicTextView extends TextView {
    public RobotoItalicTextView(Context context, AttributeSet attributeSet, int defstyle) {
        super(context, attributeSet, defstyle);
        init();
    }

    public RobotoItalicTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RobotoItalicTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface robotoItalic = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto-italic.ttf");
            setTypeface(robotoItalic);
        }
    }
}
