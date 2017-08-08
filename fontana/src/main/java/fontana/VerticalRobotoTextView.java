package fontana;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class VerticalRobotoTextView extends TextView {

    final boolean topDown;

    public VerticalRobotoTextView(Context context) {
        super(context);

        final int gravity = getGravity();
        if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.BOTTOM);
            topDown = false;
        } else {
            topDown = true;
        }
        init();
    }

    public VerticalRobotoTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        final int gravity = getGravity();
        if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.BOTTOM);
            topDown = false;
        } else {
            topDown = true;
        }
        init();
    }

    public VerticalRobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final int gravity = getGravity();
        if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.BOTTOM);
            topDown = false;
        } else {
            topDown = true;
        }
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if (topDown) {
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        } else {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
        }


        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

        getLayout().draw(canvas);
        canvas.restore();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto-regular.ttf");
            setTypeface(roboto);
        }
    }
}
