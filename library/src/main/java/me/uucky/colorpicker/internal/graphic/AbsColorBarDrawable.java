package me.uucky.colorpicker.internal.graphic;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by mariotaku on 15/2/15.
 */
public abstract class AbsColorBarDrawable extends Drawable {
    private final Paint mBackgroundPaint, mForegroundPaint;
    private final Resources mResources;
    private RectF mBounds;

    protected Resources getResources() {
        return mResources;
    }

    public AbsColorBarDrawable(Resources resources) {
        mResources = resources;
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final float density = resources.getDisplayMetrics().density;
        mBackgroundPaint.setStrokeCap(Cap.ROUND);
        mForegroundPaint.setStrokeCap(Cap.ROUND);
        mBackgroundPaint.setStrokeWidth(density * 3);
        mForegroundPaint.setStrokeWidth(density * 3);
        mBounds = new RectF();
    }

    @Override
    public void draw(Canvas canvas) {
        if (hasBackgroundPaint()) {
            canvas.drawLine(mBounds.left, mBounds.centerY(), mBounds.right, mBounds.centerY(), mBackgroundPaint);
        }
        if (hasForegroundPaint()) {
            canvas.drawLine(mBounds.left, mBounds.centerY(), mBounds.right, mBounds.centerY(), mForegroundPaint);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.set(bounds);
        setupPaints();
    }

    protected void setupPaints() {
        setupForegroundPaint(mForegroundPaint);
        setupBackgroundPaint(mBackgroundPaint);
    }


    protected void updatePaints() {
        updateForegroundPaint(mForegroundPaint);
        updateBackgroundPaint(mBackgroundPaint);
    }

    protected void setupForegroundPaint(Paint paint) {

    }

    protected void setupBackgroundPaint(Paint paint) {

    }

    protected void updateForegroundPaint(Paint paint) {

    }

    protected void updateBackgroundPaint(Paint paint) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    protected boolean hasBackgroundPaint() {
        return false;
    }

    protected boolean hasForegroundPaint() {
        return false;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
