package me.uucky.colorpicker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

/**
 * Created by mariotaku on 15/2/15.
 */
public class AlphaDrawable extends ColorBarDrawable {
    private int mColor;

    public AlphaDrawable(Resources resources) {
        super(resources);
    }


    public void setColor(int color) {
        mColor = color;
        updateShader();
        invalidateSelf();
    }

    @Override
    protected Shader generateBackgroundShader(Rect bounds) {
        final Bitmap bitmap = ColorCompareView.getAlphaPatternBitmap(getResources());
        return new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT);
    }

    @Override
    protected Shader generateShader(Rect bounds) {
        final int[] colors = {0xFF000000 | mColor, Color.TRANSPARENT};
        return new LinearGradient(0, 0, bounds.width(), 0, colors, null, TileMode.CLAMP);
    }
}
