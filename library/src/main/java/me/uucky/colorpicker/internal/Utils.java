package me.uucky.colorpicker.internal;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

/**
 * Created by mariotaku on 15/3/18.
 */
public class Utils {

    public static Bitmap getAlphaPatternBitmap(int gridSize) {
        final int[] colors = {Color.WHITE, Color.LTGRAY, Color.LTGRAY, Color.WHITE};
        final Bitmap bitmap = Bitmap.createBitmap(colors, 2, 2, Config.ARGB_8888);
        try {
            return Bitmap.createScaledBitmap(bitmap, gridSize, gridSize, false);
        } finally {
            bitmap.recycle();
        }
    }

    /**
     * Get most contrasting color
     *
     * @param color Input color, alpha channel will be disposed.
     * @return {@link android.graphics.Color#WHITE} or {@link android.graphics.Color#BLACK}
     * @see <a href='http://24ways.org/2010/calculating-color-contrast/'>Calculating Color Contrast</a>
     */
    public static int getContrastYIQ(int color, int threshold, int colorDark, int colorLight) {
        final int r = Color.red(color), g = Color.green(color), b = Color.blue(color);
        final int yiq = ((r * 299) + (g * 587) + (b * 114)) / 1000;
        return (yiq >= threshold) ? colorDark : colorLight;
    }
}
