package fontana;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 9/4/2015.
 */
public class RobotoBoldTextView extends TextView {
    public RobotoBoldTextView(Context context, AttributeSet attributeSet, int defstyle) {
        super(context, attributeSet, defstyle);
        init();
    }

    public RobotoBoldTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RobotoBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface robotoBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto-bold.ttf");
            setTypeface(robotoBold);
        }
    }
}
