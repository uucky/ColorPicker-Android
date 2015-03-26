package me.uucky.colorpicker.internal.graphic;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;

/**
 * Created by mariotaku on 15/2/15.
 */
public class HueBarDrawable extends AbsColorBarDrawable {
    public HueBarDrawable(Resources resources) {
        super(resources);
    }

    @Override
    protected boolean hasBackgroundPaint() {
        return true;
    }

    @Override
    protected void setupBackgroundPaint(Paint paint) {
        final Rect bounds = getBounds();
        final int[] hue = new int[361];
        int count = 0;
        for (int i = hue.length - 1; i >= 0; i--, count++) {
            hue[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        paint.setShader(new LinearGradient(0, 0, bounds.width(), 0, hue, null, TileMode.REPEAT));
    }

}
