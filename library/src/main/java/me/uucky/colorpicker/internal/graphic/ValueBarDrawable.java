package me.uucky.colorpicker.internal.graphic;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
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
        updateShader();
        invalidateSelf();
    }

    public void setSaturation(float saturation) {
        mSaturation = saturation;
        updateShader();
        invalidateSelf();
    }

    @Override
    protected Shader generateShader(Rect bounds) {
        final float[] hsv = {mHue, mSaturation, 1};
        final int[] colors = {Color.HSVToColor(hsv), Color.BLACK};
        return new LinearGradient(0, 0, bounds.width(), 0, colors, null, TileMode.CLAMP);
    }
}
