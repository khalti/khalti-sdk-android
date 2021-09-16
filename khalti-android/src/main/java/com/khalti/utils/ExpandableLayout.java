package com.khalti.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.khalti.R;

@Keep
public class ExpandableLayout extends LinearLayout {
    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;
    private boolean mAttachedToWindow;
    private boolean mFirstLayout = true;
    private boolean mInLayout;
    private ObjectAnimator mExpandAnimator;
    private OnExpandListener mListener;

    public ExpandableLayout(Context context) {
        super(context);
        this.init(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableLayout(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    @TargetApi(21)
    public ExpandableLayout(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context);
    }

    private void init(Context c) {
        this.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
        View child = findExpandableView();
        if (child != null) {
            LayoutParams p = (LayoutParams) child.getLayoutParams();

            if (p.weight != 0) {
                throw new IllegalArgumentException(
                        "ExpandableView can't use weight");
            }

            if (!p.isExpanded && !p.isExpanding) {
                child.setVisibility(View.GONE);
            } else {
                child.setVisibility(View.VISIBLE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mInLayout = true;
        super.onLayout(changed, l, t, r, b);
        mInLayout = false;
        mFirstLayout = false;
    }

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
        View child = findExpandableView();
        if (mExpandAnimator != null && mExpandAnimator.isRunning()) {
            mExpandAnimator.end();
            mExpandAnimator = null;
        }
        if (child != null) {
            LayoutParams p = (LayoutParams) child.getLayoutParams();
            if (p.isExpanded) {
                p.height = p.originalHeight;
                child.setVisibility(View.VISIBLE);
            } else {
                p.height = p.originalHeight;
                child.setVisibility(View.GONE);
            }
            p.isExpanding = false;
        }
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    public View findExpandableView() {
        for (int i = 0; i < this.getChildCount(); i++) {
            LayoutParams p = (LayoutParams) this.getChildAt(i)
                    .getLayoutParams();
            if (p.canExpand) {
                return this.getChildAt(i);
            }
        }
        return null;
    }

    boolean checkExpandableView(View expandableView) {
        LayoutParams p = (LayoutParams) expandableView.getLayoutParams();
        return p.canExpand;
    }

    public boolean isExpanded() {
        View child = findExpandableView();
        if (child != null) {
            LayoutParams p = (LayoutParams) child.getLayoutParams();
            return p.isExpanded;
        }
        return false;
    }

    public void toggleExpansion() {
        this.setExpanded(!isExpanded(), true);
    }

    public void setExpanded(boolean isExpanded) {
        this.setExpanded(isExpanded, false);
    }

    public void setExpanded(boolean isExpanded, boolean shouldAnimate) {
        View child = findExpandableView();
        if (child != null) {
            if (isExpanded != this.isExpanded()) {
                if (isExpanded) {
                    expand(child, shouldAnimate);
                } else {
                    this.collapse(child, shouldAnimate);
                }
            }
        }
        this.requestLayout();
    }

    public void setOnExpandListener(OnExpandListener listener) {
        this.mListener = listener;
    }

    private boolean expand(View child, boolean shouldAnimate) {
        boolean result = false;
        if (!checkExpandableView(child)) {
            throw new IllegalArgumentException(
                    "expand(), View is not expandableView");
        }
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (mFirstLayout || !mAttachedToWindow || !shouldAnimate) {
            p.isExpanded = true;
            p.isExpanding = false;
            p.height = p.originalHeight;
            child.setVisibility(View.VISIBLE);
            result = true;
        } else {
            if (!p.isExpanded && !p.isExpanding) {
                this.playExpandAnimation(child);
                result = true;
            }
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void playExpandAnimation(final View child) {
        final LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p.isExpanding) {
            return;
        }
        child.setVisibility(View.VISIBLE);
        p.isExpanding = true;
        this.measure(mWidthMeasureSpec, mHeightMeasureSpec);
        final int measuredHeight = child.getMeasuredHeight();
        p.height = 0;

        mExpandAnimator = ObjectAnimator.ofInt(p, "height", 0, measuredHeight);
        mExpandAnimator.setDuration(getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime));
        mExpandAnimator.addUpdateListener(animation -> {
            dispatchOffset(child);
            child.requestLayout();
        });
        mExpandAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                performToggleState(child);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        mExpandAnimator.start();
    }

    /**
     * @param child
     * @param shouldAnimation
     * @return
     */
    private boolean collapse(View child, boolean shouldAnimation) {
        boolean result = false;
        if (!checkExpandableView(child)) {
            throw new IllegalArgumentException(
                    "collapse(), View is not expandableView");
        }
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (mFirstLayout || !mAttachedToWindow || !shouldAnimation) {
            p.isExpanded = false;
            p.isExpanding = false;
            p.height = p.originalHeight;
            child.setVisibility(View.GONE);
            result = true;
        } else {
            if (p.isExpanded && !p.isExpanding) {
                this.playCollapseAnimation(child);
                result = true;
            }
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void playCollapseAnimation(final View child) {
        final LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p.isExpanding) {
            return;
        }
        child.setVisibility(View.VISIBLE);
        p.isExpanding = true;
        this.measure(mWidthMeasureSpec, mHeightMeasureSpec);
        final int measuredHeight = child.getMeasuredHeight();

        mExpandAnimator = ObjectAnimator.ofInt(p, "height", measuredHeight, 0);
        mExpandAnimator.setDuration(getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime));
        mExpandAnimator.addUpdateListener(animation -> {
            dispatchOffset(child);
            child.requestLayout();
        });
        mExpandAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                performToggleState(child);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        mExpandAnimator.start();
    }

    public ObjectAnimator getAnimator() {
        return mExpandAnimator;
    }

    public boolean isRunningAnimation() {
        View child = findExpandableView();
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        return p.isExpanding;
    }

    private void dispatchOffset(View child) {
        if (mListener != null) {
            mListener.onExpandOffset(this, child, child.getHeight(),
                    !isExpanded());
        }
    }

    private void performToggleState(View child) {
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p.isExpanded) {
            p.isExpanded = false;
            if (mListener != null) {
                mListener.onToggle(this, child, false);
            }
            child.setVisibility(View.GONE);
            p.height = p.originalHeight;
        } else {
            p.isExpanded = true;
            if (mListener != null) {
                mListener.onToggle(this, child, true);
            }
        }
        p.isExpanding = false;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        if (isExpanded()) {
            ss.isExpanded = true;
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.isExpanded) {
            View child = findExpandableView();
            if (child != null) {
                setExpanded(true);
            }
        }
    }

    private static class SavedState extends BaseSavedState {

        boolean isExpanded;

        public SavedState(Parcel source) {
            super(source);
            isExpanded = source.readInt() == 1;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isExpanded ? 1 : 0);
        }

        @SuppressWarnings("unused")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(
            @NonNull ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && (p instanceof LayoutParams);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        private static final int NO_MEASURED_HEIGHT = -10;
        int originalHeight;
        boolean isExpanded;
        boolean canExpand;
        boolean isExpanding;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs,
                    R.styleable.ExpandableLayout);
            canExpand = a.getBoolean(R.styleable.ExpandableLayout_canExpand,
                    false);
            originalHeight = this.height;
            a.recycle();
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
            originalHeight = this.height;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            originalHeight = this.height;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            originalHeight = this.height;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(LinearLayout.LayoutParams source) {
            super(source);
            originalHeight = this.height;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            originalHeight = this.height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public interface OnExpandListener {
        void onToggle(ExpandableLayout view, View child, boolean isExpanded);

        void onExpandOffset(ExpandableLayout view, View child,
                            float offset, boolean isExpanding);
    }
}