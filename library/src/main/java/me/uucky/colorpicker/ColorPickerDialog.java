package me.uucky.colorpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mariotaku on 15/2/15.
 */
public class ColorPickerDialog extends AlertDialog implements OnShowListener {

    private final ColorsGridAdapter colorsAdapter;

    private GridView colorsGridView;
    private EditText editHexColor;
    private SeekBar hueSeekBar, saturationSeekBar, valueSeekBar, alphaSeekBar;
    private ColorCompareView colorCompare;

    public ColorPickerDialog(Context context) {
        super(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_color_picker, null);
        setView(view);
        colorsAdapter = new ColorsGridAdapter(getContext());
        colorsGridView = (GridView) view.findViewById(R.id.colors_grid);
        hueSeekBar = (SeekBar) view.findViewById(R.id.hue_seekbar);
        saturationSeekBar = (SeekBar) view.findViewById(R.id.saturation_seekbar);
        valueSeekBar = (SeekBar) view.findViewById(R.id.value_seekbar);
        alphaSeekBar = (SeekBar) view.findViewById(R.id.alpha_seekbar);
        editHexColor = (EditText) view.findViewById(R.id.color_hex);
        colorCompare = (ColorCompareView) view.findViewById(R.id.color_compare);
        final Resources res = getContext().getResources();
        hueSeekBar.setProgressDrawable(new HueDrawable(res));
        hueSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateHuePreview();
                updateColorPreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        saturationSeekBar.setProgressDrawable(new SaturationDrawable(res));
        saturationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSaturationPreview();
                updateColorPreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        valueSeekBar.setProgressDrawable(new ValueDrawable(res));
        valueSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorPreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        alphaSeekBar.setProgressDrawable(new AlphaDrawable(res));
        alphaSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorPreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        colorsGridView.setAdapter(colorsAdapter);
        setOnShowListener(this);
    }

    public void addColor(int... colors) {
        colorsAdapter.addAll(colors);
    }

    public int getColor() {
        final int alpha;
        if (isAlphaEnabled()) {
            alpha = Math.round((1f - alphaSeekBar.getProgress() / (float) alphaSeekBar.getMax()) * 255f);
        } else {
            alpha = 0xff;
        }
        final float hue = getHue();
        final float saturation = getSaturation();
        final float value = getValue();
        return Color.HSVToColor(alpha, new float[]{hue, saturation, value});
    }

    public void setColor(int color) {
        colorCompare.setOldColor(color);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        setHue(hsv[0]);
        setSaturation(hsv[1]);
        setValue(hsv[2]);
        setAlpha(Color.alpha(color));
    }

    @Override
    public void onShow(DialogInterface dialog) {
        updateHuePreview();
        updateSaturationPreview();
        updateColorPreview();
    }

    public void setNegativeButton(CharSequence text, ColorPickerListener listener) {
        setButton(BUTTON_NEGATIVE, text, new InternalClickListener(listener));
    }

    public void setPositiveButton(CharSequence text, ColorPickerListener listener) {
        setButton(BUTTON_POSITIVE, text, new InternalClickListener(listener));
    }

    public void setNeutralButton(CharSequence text, ColorPickerListener listener) {
        setButton(BUTTON_NEUTRAL, text, new InternalClickListener(listener));
    }

    private float getHue() {
        return (1 - hueSeekBar.getProgress() / (float) hueSeekBar.getMax()) * 360f;
    }

    private void setHue(float hue) {
        hueSeekBar.setProgress(Math.round(hueSeekBar.getMax() * (1 - hue / 360)));
    }

    private float getSaturation() {
        return 1 - (saturationSeekBar.getProgress() / (float) saturationSeekBar.getMax());
    }

    private void setSaturation(float hue) {
        saturationSeekBar.setProgress(Math.round(saturationSeekBar.getMax() * (1 - hue)));
    }

    private float getValue() {
        return 1 - (valueSeekBar.getProgress() / (float) valueSeekBar.getMax());
    }

    private void setValue(float hue) {
        valueSeekBar.setProgress(Math.round(valueSeekBar.getMax() * (1 - hue)));
    }

    private boolean isAlphaEnabled() {
        return alphaSeekBar.getVisibility() == View.VISIBLE;
    }

    public void setAlphaEnabled(boolean alphaEnabled) {
        alphaSeekBar.setVisibility(alphaEnabled ? View.VISIBLE : View.GONE);
    }

    private void setAlpha(int alpha) {
        alphaSeekBar.setProgress(Math.round(alphaSeekBar.getMax() * (1 - alpha / 255f)));
    }

    private void updateColorPreview() {
        final AlphaDrawable alphaDrawable = (AlphaDrawable) alphaSeekBar.getProgressDrawable();
        final int color = getColor();
        if (isAlphaEnabled()) {
            editHexColor.setText(String.format("%08x", color));
        } else {
            editHexColor.setText(String.format("%06x", color & 0x00FFFFFF));
        }
        colorCompare.setFrontColor(color);
        alphaDrawable.setColor(color);
    }

    private void updateHuePreview() {
        final SaturationDrawable saturationDrawable = (SaturationDrawable) saturationSeekBar.getProgressDrawable();
        final ValueDrawable valueDrawable = (ValueDrawable) valueSeekBar.getProgressDrawable();
        final float hue = getHue();
        valueDrawable.setHue(hue);
        saturationDrawable.setHue(hue);
    }

    private void updateSaturationPreview() {
        final ValueDrawable valueDrawable = (ValueDrawable) valueSeekBar.getProgressDrawable();
        valueDrawable.setSaturation(getSaturation());
    }

    public interface ColorPickerListener {
        void onClick(ColorPickerDialog dialog, int color);
    }

    private static class ColorsGridAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private List<Integer> mColors = new ArrayList<>();

        public ColorsGridAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void add(int color) {
            mColors.add(color);
            notifyDataSetChanged();
        }

        public void addAll(int[] colors) {
            for (int color : colors) {
                mColors.add(color);
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mColors.size();
        }

        @Override
        public Integer getItem(int position) {
            return mColors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = convertView != null ? convertView : mInflater.inflate(R.layout.grid_item_color, parent, false);
            view.setBackgroundColor(getItem(position));
            return view;
        }
    }

    private static class InternalClickListener implements DialogInterface.OnClickListener {

        private final ColorPickerListener listener;

        InternalClickListener(ColorPickerListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (listener == null) return;
            final ColorPickerDialog colorPickerDialog = ((ColorPickerDialog) dialog);
            listener.onClick(colorPickerDialog, colorPickerDialog.getColor());
        }
    }
}