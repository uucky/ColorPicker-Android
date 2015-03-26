package me.uucky.colorpicker.internal.graphic;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;

/**
 * Created by mariotaku on 15/2/15.
 */
public class ValueBarDrawable extends AbsColorBarDrawable {
    private float mHue, mSaturation;

    public ValueBarDrawable(Resources resources) {
        super(resources);
    }


    public void setHue(float hue) {
        mHue = hue;
        updatePaints();
        invalidateSelf();
    }

    @Override
    protected boolean hasBackgroundPaint() {
        return true;
    }

    @Override
    protected boolean hasForegroundPaint() {
        return true;
    }

    @Override
    protected void setupForegroundPaint(Paint paint) {
        final Rect bounds = getBounds();
        final int[] colors = {Color.WHITE, Color.TRANSPARENT};
        paint.setShader(new LinearGradient(0, 0, bounds.width(), 0, colors, null, TileMode.CLAMP));
    }

    @Override
    protected void setupBackgroundPaint(Paint paint) {
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void updateForegroundPaint(Paint paint) {
        final float[] hsv = {mHue, mSaturation, 1};
        paint.setColorFilter(new PorterDuffColorFilter(Color.HSVToColor(hsv), Mode.SRC_ATOP));
    }

    public void setSaturation(float saturation) {
        mSaturation = saturation;
        updatePaints();
        invalidateSelf();
    }

}
