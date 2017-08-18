package fontana;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RobotoBoldTextView extends android.support.v7.widget.AppCompatTextView {
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
