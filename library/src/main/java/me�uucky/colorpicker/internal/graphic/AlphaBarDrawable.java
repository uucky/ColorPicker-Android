package me.uucky.colorpicker.internal.graphic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;

import me.uucky.colorpicker.internal.Utils;

/**
 * Created by mariotaku on 15/2/15.
 */
public class AlphaBarDrawable extends AbsColorBarDrawable {
    private int mColor;

    public AlphaBarDrawable(Resources resources) {
        super(resources);
    }


    @Override
    protected void setupBackgroundPaint(Paint paint) {
        super.setupBackgroundPaint(paint);
        final Bitmap bitmap = Utils.getAlphaPatternBitmap(Math.round(getResources().getDisplayMetrics().density * 4));
        paint.setShader(new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT));
    }

    public void setColor(int color) {
        mColor = color;
        updatePaints();
        invalidateSelf();
    }

    @Override
    protected boolean hasBackgroundPaint() {
        return true;
    }

    @Override
    protected void updateForegroundPaint(Paint paint) {
        paint.setColorFilter(new PorterDuffColorFilter(mColor, Mode.SRC_ATOP));
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

}
