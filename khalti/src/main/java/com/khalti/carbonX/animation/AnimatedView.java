package com.khalti.carbonX.animation;

import com.nineoldandroids.animation.Animator;

public interface AnimatedView {
    Animator getAnimator();

    AnimUtils.Style getOutAnimation();

    void setOutAnimation(AnimUtils.Style outAnim);

    AnimUtils.Style getInAnimation();

    void setInAnimation(AnimUtils.Style inAnim);

    void setVisibilityImmediate(int visibility);
}
