package me.uucky.colorpicker.internal;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

import me.uucky.colorpicker.internal.graphic.AbsColorBarDrawable;

/**
 * Created by mariotaku on 15/2/15.
 */
public class HueDrawable extends AbsColorBarDrawable {
    public HueDrawable(Resources resources) {
        super(resources);
    }

    @Override
    protected Shader generateShader(Rect bounds) {
        final int[] hue = new int[361];
        int count = 0;
        for (int i = hue.length - 1; i >= 0; i--, count++) {
            hue[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        return (new LinearGradient(0, 0, bounds.width(), 0, hue, null, TileMode.REPEAT));
    }
}
