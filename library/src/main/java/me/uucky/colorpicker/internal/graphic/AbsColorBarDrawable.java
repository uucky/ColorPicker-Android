package me.uucky.colorpicker.internal.graphic;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by mariotaku on 15/2/15.
 */
public abstract class AbsColorBarDrawable extends Drawable {
    private final Paint mBackgroundPaint, mPaint;
    private final Resources mResources;
    private RectF mBounds;

    protected Resources getResources() {
        return mResources;
    }

    public AbsColorBarDrawable(Resources resources) {
        mResources = resources;
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final float density = resources.getDisplayMetrics().density;
        mBackgroundPaint.setStrokeCap(Cap.ROUND);
        mPaint.setStrokeCap(Cap.ROUND);
        mBackgroundPaint.setStrokeWidth(density * 3);
        mPaint.setStrokeWidth(density * 3);
        mBounds = new RectF();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mBackgroundPaint.getShader() != null) {
            canvas.drawLine(mBounds.left, mBounds.centerY(), mBounds.right, mBounds.centerY(), mBackgroundPaint);
        }
        canvas.drawLine(mBounds.left, mBounds.centerY(), mBounds.right, mBounds.centerY(), mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.set(bounds);
        updateShader();
    }

    protected void updateShader() {
        mBackgroundPaint.setShader(generateBackgroundShader(getBounds()));
        mPaint.setShader(generateShader(getBounds()));
    }

    protected abstract Shader generateShader(Rect bounds);

    protected Shader generateBackgroundShader(Rect bounds) {
        return null;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
