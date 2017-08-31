package khalti.carbonX.animation;

import com.nineoldandroids.animation.Animator;

public interface AnimatedView {
    Animator getAnimator();

    khalti.carbonX.animation.AnimUtils.Style getOutAnimation();

    void setOutAnimation(khalti.carbonX.animation.AnimUtils.Style outAnim);

    khalti.carbonX.animation.AnimUtils.Style getInAnimation();

    void setInAnimation(khalti.carbonX.animation.AnimUtils.Style inAnim);

    void setVisibilityImmediate(int visibility);
}
