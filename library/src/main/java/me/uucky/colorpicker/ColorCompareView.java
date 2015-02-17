package me.uucky.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mariotaku on 15/2/16.
 */
public class ColorCompareView extends View {
    private final Paint mBackPaint, mFrontPaint;
    private final Paint mAlphaPatternPaint;
    private final RectF mBackRect, mFrontRect;
    private final float mCornerRadius;

    public ColorCompareView(Context context) {
        this(context, null);
    }

    public ColorCompareView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = resolveSize(0, heightMeasureSpec);
        setMeasuredDimension(Math.round(height * 1.5f), height);
    }

    public ColorCompareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFrontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAlphaPatternPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAlphaPatternPaint.setShader(new BitmapShader(getAlphaPatternBitmap(getResources()), TileMode.REPEAT, TileMode.REPEAT));
        mBackRect = new RectF();
        mFrontRect = new RectF();
        mCornerRadius = getResources().getDisplayMetrics().density * 2;
    }

    static Bitmap getAlphaPatternBitmap(Resources resources) {
        final int[] colors = {Color.WHITE, Color.LTGRAY, Color.LTGRAY, Color.WHITE};
        final Bitmap bitmap = Bitmap.createBitmap(colors, 2, 2, Config.ARGB_8888);
        final int size = Math.round(2 * resources.getDisplayMetrics().density * 4);
        try {
            return Bitmap.createScaledBitmap(bitmap, size, size, false);
        } finally {
            bitmap.recycle();
        }
    }

    public void setOldColor(int color) {
        mBackPaint.setColor(color);
        invalidate();
    }

    public void setFrontColor(int color) {
        mFrontPaint.setColor(color);
        invalidate();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBackRect.set(0, 0, bottom - top, bottom - top);
        mFrontRect.set(right - left - (bottom - top), 0, right - left, bottom - top);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mBackRect, mCornerRadius, mCornerRadius, mAlphaPatternPaint);
        canvas.drawRoundRect(mBackRect, mCornerRadius, mCornerRadius, mBackPaint);
        canvas.drawRoundRect(mFrontRect, mCornerRadius, mCornerRadius, mAlphaPatternPaint);
        canvas.drawRoundRect(mFrontRect, mCornerRadius, mCornerRadius, mFrontPaint);
    }
}
