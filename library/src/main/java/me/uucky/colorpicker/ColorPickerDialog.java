package me.uucky.colorpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.util.ArrayList;

/**
 * Created by mariotaku on 15/2/15.
 */
public class ColorPickerDialog extends AlertDialog implements OnShowListener {

    private final ColorsAdapter colorsAdapter;

    private RecyclerView colorPresetsView;
    private EditText editHexColor;
    private SeekBar hueSeekBar, saturationSeekBar, valueSeekBar, alphaSeekBar;
    private ColorCompareView colorCompare;

    public ColorPickerDialog(Context context) {
        super(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_color_picker, null);
        setView(view);
        colorsAdapter = new ColorsAdapter(this, getContext());
        colorPresetsView = (RecyclerView) view.findViewById(R.id.color_presets);
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

        colorPresetsView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        colorPresetsView.setAdapter(colorsAdapter);
        setOnShowListener(this);
        setColor(Color.WHITE);
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

    public void setInitialColor(int color) {
        colorCompare.setOldColor(color);
        setAlpha(Color.alpha(color));
        final float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        setHue(hsv[0]);
        setSaturation(hsv[1]);
        setValue(hsv[2]);
    }

    public void setColor(int color) {
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
        colorsAdapter.setCurrentColor(color);
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

    public void setPresetsEnabled(boolean enabled) {
        colorPresetsView.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }


    private static class ColorsAdapter extends Adapter<ColorViewHolder> {

        private final ColorPickerDialog mDialog;
        private final LayoutInflater mInflater;
        private final ArrayList<Integer> mColors;
        private int mCurrentColor;

        public ColorsAdapter(ColorPickerDialog dialog, final Context context) {
            mColors = new ArrayList<>();
            mDialog = dialog;
            mInflater = LayoutInflater.from(context);
        }

        public void addAll(int... colors) {
            for (int color : colors) {
                mColors.add(color);
            }
            notifyDataSetChanged();
        }

        public int getColor(int position) {
            return mColors.get(position);
        }

        public void setCurrentColor(final int color) {
            mCurrentColor = color;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(ColorViewHolder holder, int position) {
            final int color = mColors.get(position);
            holder.setItem(position, color, mCurrentColor == color);
        }

        @Override
        public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = mInflater.inflate(R.layout.adapter_item_color_picker_preset, parent, false);
            return new ColorViewHolder(view, this, mDialog);
        }


        @Override
        public int getItemCount() {
            return mColors.size();
        }

    }

    public static class ColorViewHolder extends ViewHolder implements View.OnClickListener {

        private final View colorView;
        private final ColorsAdapter mAdapter;
        private final ColorPickerDialog mDialog;

        public ColorViewHolder(View itemView, ColorsAdapter adapter, ColorPickerDialog dialog) {
            super(itemView);
            mAdapter = adapter;
            mDialog = dialog;
            itemView.setOnClickListener(this);
            colorView = itemView.findViewById(R.id.color);
        }

        @Override
        public void onClick(View v) {
            mDialog.setColor(mAdapter.getColor(getPosition()));
        }

        public void setItem(int position, int color, boolean activated) {
            colorView.setBackgroundColor(color);
            colorView.setActivated(activated);
        }
    }
}
