/*
 * 				Twidere - Twitter client for Android
 * 
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.uucky.colorpicker.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import me.uucky.colorpicker.R;

public class ColorView extends View {

    private final Paint mColorPaint;
    private final Paint mAlphaPatternPaint;
    private RectF mRect;
    private float mRadius;

    public ColorView(final Context context) {
        this(context, null);
    }

    public ColorView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorView);
        mRect = new RectF();
        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Bitmap bitmap = Utils.getAlphaPatternBitmap(Math.round(getResources().getDisplayMetrics().density * 8));
        mAlphaPatternPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAlphaPatternPaint.setShader(new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT));
        mRadius = a.getDimension(R.styleable.ColorView_cp_cornerRadius, 0);
        ViewCompat.setElevation(this, a.getDimension(R.styleable.ColorView_cp_elevation, 0));
        a.recycle();
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            Internal.setOutlineProvider(this);
        }
    }

    public int getColor() {
        return mColorPaint.getColor();
    }

    public void setColor(int color) {
        mColorPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = getWidth();
        mRect.bottom = getHeight();
        canvas.drawRoundRect(mRect, mRadius, mRadius, mAlphaPatternPaint);
        canvas.drawRoundRect(mRect, mRadius, mRadius, mColorPaint);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec), height = MeasureSpec.getSize(heightMeasureSpec);
        final ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT && lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(height, height);
        } else if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT && lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            setMeasuredDimension(width, width);
        } else {
            if (width > height) {
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
                setMeasuredDimension(height, height);
            } else {
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
                setMeasuredDimension(width, width);
            }
        }
    }

    private static class Internal {
        @TargetApi(VERSION_CODES.LOLLIPOP)
        public static void setOutlineProvider(final ColorView colorView) {
            colorView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    final int left = 0;
                    final int top = 0;
                    final int right = view.getWidth();
                    final int bottom = view.getHeight();
                    outline.setRoundRect(left, top, right, bottom, colorView.mRadius);
                }
            });
        }
    }
}
