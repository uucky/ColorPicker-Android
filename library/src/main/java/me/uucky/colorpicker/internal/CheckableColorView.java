package me.uucky.colorpicker.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.widget.Checkable;

/**
 * Created by mariotaku on 15/3/18.
 */
public class CheckableColorView extends ColorView implements Checkable {

    private static final float BASE_ANGLE = -45;
    private static final float EXTRA_ANGLE = -10;

    private final Paint mPaint;
    private final RectF mTempRect;
    private final EffectViewHelper mEffectViewHelper;
    private float mCheckedProgress;
    private boolean mChecked;

    public CheckableColorView(Context context) {
        this(context, null);
    }

    public CheckableColorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mEffectViewHelper = new EffectViewHelper(this, new Property<View, Float>(Float.TYPE, null) {
            @Override
            public Float get(View object) {
                return ((CheckableColorView) object).getCheckedProgress();
            }

            @Override
            public boolean isReadOnly() {
                return false;
            }

            @Override
            public void set(View object, Float value) {
                ((CheckableColorView) object).setCheckedProgress(value);
            }
        });
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTempRect = new RectF();
    }

    private float getCheckedProgress() {
        return mCheckedProgress;
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public void setChecked(boolean checked, boolean animate) {
        if (mChecked == checked) return;
        mChecked = checked;
        if (animate) {
            mEffectViewHelper.setState(checked);
        } else {
            mEffectViewHelper.resetState(checked);
            setCheckedProgress(checked ? 1 : 0);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        mPaint.setColor(Utils.getContrastYIQ(color, 192, Color.BLACK, Color.WHITE));
    }

    @Override
    public void toggle() {
        setChecked(false);
    }

    public void setCheckedProgress(float percent) {
        mCheckedProgress = percent;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCheckMark(canvas);
    }

    private void drawCheckMark(Canvas canvas) {
        final int left = getPaddingLeft(), top = getPaddingTop(),
                right = getWidth() - getPaddingRight(), bottom = getHeight() - getPaddingBottom();
        final int longStrokeMaxLength = bottom - top, shortStrokeMaxLength = longStrokeMaxLength / 2;
        final int longStrokeLength = Math.round(longStrokeMaxLength * mCheckedProgress);
        final int shortStrokeLength = Math.round(longStrokeLength * 0.6f);
        final int strokeSize = longStrokeMaxLength / 5;
        final int shortStrokeBottom = bottom;
        final double shortStrokeWidth = shortStrokeMaxLength * Math.cos(Math.toRadians(90 - Math.abs(BASE_ANGLE + EXTRA_ANGLE)));
        final double longStrokeWidth = longStrokeMaxLength * Math.cos(Math.toRadians(90 - Math.abs(BASE_ANGLE)));
        final int checkMaxWidth = (int) Math.round(shortStrokeWidth + longStrokeWidth);

        mTempRect.left = left;

        final float strokeHideRatio = Math.min(1, shortStrokeLength / (float) strokeSize);
        float radius = (1 - strokeHideRatio) * (strokeSize / 2);

        final double shortStrokeLengthHeight = shortStrokeLength * Math.cos(Math.toRadians(BASE_ANGLE + EXTRA_ANGLE));
        final double shortStrokeSizeHeight = strokeSize * Math.sin(Math.toRadians(BASE_ANGLE + EXTRA_ANGLE));
        final float translateXNormal = (right - left) - checkMaxWidth / 2;
        final float translateXCenter = (right - left) / 2;
        final float translateX = translateXNormal + (translateXCenter - translateXNormal) * (1 - strokeHideRatio);
        final float translateYNormal = (int) -Math.round((bottom - top) / 2 - shortStrokeLengthHeight + shortStrokeSizeHeight / 2);
        final float translateYCenter = (float) -((bottom - top) / 2 - strokeSize / 2 * Math.sqrt(2));
        final float translateY = translateYNormal + (translateYCenter - translateYNormal) * (1 - strokeHideRatio);
        final float scale = strokeHideRatio * 0.25f + 0.75f;

        mPaint.setAlpha((int) (Math.min(strokeHideRatio, 0.5f) * 2 * 0xFF));

        canvas.save();
        canvas.scale(scale, scale, left + (right - left) / 2, top + (bottom - top) / 2);
        canvas.translate(translateX, translateY);
        canvas.rotate(BASE_ANGLE + EXTRA_ANGLE * strokeHideRatio, left, bottom);
        mTempRect.top = shortStrokeBottom - Math.max(strokeSize, shortStrokeLength);
        mTempRect.right = mTempRect.left + strokeSize;
        mTempRect.bottom = shortStrokeBottom;
        canvas.drawRoundRect(mTempRect, radius, radius, mPaint);
        canvas.restore();

        canvas.save();
        canvas.scale(scale, scale, left + (right - left) / 2, top + (bottom - top) / 2);
        canvas.translate(translateX, translateY);
        canvas.rotate(BASE_ANGLE, left, bottom);
        mTempRect.top = mTempRect.bottom - strokeSize;
        mTempRect.right = left + Math.max(strokeSize, longStrokeLength);
        mTempRect.bottom = bottom;
        canvas.drawRoundRect(mTempRect, radius, radius, mPaint);
        canvas.restore();
    }
}
