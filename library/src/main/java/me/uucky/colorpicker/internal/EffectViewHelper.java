package me.uucky.colorpicker.internal;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.View;

/**
 * Created by mariotaku on 14-7-30.
 */
public class EffectViewHelper implements Animator.AnimatorListener {

    private final View mView;
    private final Property<View, Float> mProperty;
    private final long mDuration;

    private Animator mCurrentAnimator;
    private AnimatorRunnable mAnimatorRunnable;
    private boolean mState;

    public EffectViewHelper(View view, Property<View, Float> property, long duration) {
        mView = view;
        mProperty = property;
        mDuration = duration;
    }


    public View getView() {
        return mView;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mCurrentAnimator = animation;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mCurrentAnimator = null;
        final AnimatorRunnable runnable = mAnimatorRunnable;
        if (runnable != null) {
            runnable.run();
        }
        mAnimatorRunnable = null;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mCurrentAnimator = null;
        final AnimatorRunnable runnable = mAnimatorRunnable;
        if (runnable != null) {
            runnable.run();
        }
        mAnimatorRunnable = null;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public void resetState(boolean state) {
        mState = state;
        mAnimatorRunnable = null;
        mCurrentAnimator = null;
    }

    public void setState(boolean state) {
        if (state == mState) return;
        mState = state;
        final AnimatorRunnable runnable = new AnimatorRunnable(this, state);
        if (mCurrentAnimator != null) {
            mAnimatorRunnable = runnable;
            mCurrentAnimator = null;
            return;
        }
        runnable.run();
        mAnimatorRunnable = null;
    }

    private long getDuration() {
        return mDuration;
    }

    private Property<View, Float> getProperty() {
        return mProperty;
    }

    private static class AnimatorRunnable implements Runnable {

        private final EffectViewHelper helper;
        private final Property<View, Float> property;
        private final View target;
        private final boolean state;
        private final long duration;

        AnimatorRunnable(EffectViewHelper helper, boolean state) {
            this.helper = helper;
            this.property = helper.getProperty();
            this.state = state;
            this.target = helper.getView();
            this.duration = helper.getDuration();
        }

        @Override
        public void run() {
            final float from = property.get(target);
            final float to = state ? 1 : 0;
            final ObjectAnimator animator = ObjectAnimator.ofFloat(target, property, from, to);
            animator.setDuration(duration);
            animator.addListener(helper);
            animator.start();
        }
    }
}
