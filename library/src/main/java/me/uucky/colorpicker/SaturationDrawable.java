package me.uucky.colorpicker;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

/**
 * Created by mariotaku on 15/2/15.
 */
public class SaturationDrawable extends ColorBarDrawable {
    private float mHue;

    public SaturationDrawable(Resources resources) {
        super(resources);
    }

    public void setHue(float hue) {
        mHue = hue;
        updateShader();
        invalidateSelf();
    }

    @Override
    protected Shader generateShader(Rect bounds) {
        final float[] hsv = {mHue, 1, 1};
        final int[] colors = {Color.HSVToColor(hsv), Color.WHITE};
        return (new LinearGradient(0, 0, bounds.width(), 0, colors, null, TileMode.CLAMP));
    }
}
