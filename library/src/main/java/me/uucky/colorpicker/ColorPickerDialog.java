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
        colorsAdapter = new ColorsGridAdapter(getContext());
        setView(inflater.inflate(R.layout.dialog_color_picker, null));
        setOnShowListener(this);
        setButton(BUTTON_NEGATIVE, getContext().getString(android.R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        setButton(BUTTON_POSITIVE, getContext().getString(android.R.string.ok), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void onShow(DialogInterface dialog) {
        colorsGridView = (GridView) findViewById(R.id.colors_grid);
        hueSeekBar = (SeekBar) findViewById(R.id.hue_seekbar);
        saturationSeekBar = (SeekBar) findViewById(R.id.saturation_seekbar);
        valueSeekBar = (SeekBar) findViewById(R.id.value_seekbar);
        alphaSeekBar = (SeekBar) findViewById(R.id.alpha_seekbar);
        editHexColor = (EditText) findViewById(R.id.color_hex);
        colorCompare = (ColorCompareView) findViewById(R.id.color_compare);
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
        updateHuePreview();
        updateSaturationPreview();
        updateColorPreview();
        colorCompare.setBackColor(getColor());
    }

    public void addColor(int... colors) {
        colorsAdapter.addAll(colors);
    }

    private void updateHuePreview() {
        final SaturationDrawable saturationDrawable = (SaturationDrawable) saturationSeekBar.getProgressDrawable();
        final ValueDrawable valueDrawable = (ValueDrawable) valueSeekBar.getProgressDrawable();
        final float hue = (1 - hueSeekBar.getProgress() / (float) hueSeekBar.getMax()) * 360f;
        valueDrawable.setHue(hue);
        saturationDrawable.setHue(hue);
    }

    private void updateSaturationPreview() {
        final ValueDrawable valueDrawable = (ValueDrawable) valueSeekBar.getProgressDrawable();
        valueDrawable.setSaturation(1 - (saturationSeekBar.getProgress() / (float) saturationSeekBar.getMax()));
    }

    private void updateColorPreview() {
        final AlphaDrawable alphaDrawable = (AlphaDrawable) alphaSeekBar.getProgressDrawable();
        final int color = getColor();
        editHexColor.setText(String.format("%06X", color));
        colorCompare.setFrontColor(color);
        alphaDrawable.setColor(color);
    }

    public void setAlphaEnabled(boolean alphaEnabled) {
        alphaSeekBar.setVisibility(alphaEnabled ? View.VISIBLE : View.GONE);
    }

    public int getColor() {
        final int alpha;
        if (alphaSeekBar.getVisibility() == View.VISIBLE) {
            alpha = Math.round((1f - alphaSeekBar.getProgress() / (float) alphaSeekBar.getMax()) * 255f);
        } else {
            alpha = 0xff;
        }
        final float hue = (1 - hueSeekBar.getProgress() / (float) hueSeekBar.getMax()) * 360f;
        final float saturation = 1 - (saturationSeekBar.getProgress() / (float) saturationSeekBar.getMax());
        final float value = 1 - (valueSeekBar.getProgress() / (float) valueSeekBar.getMax());
        return Color.HSVToColor(alpha, new float[]{hue, saturation, value});
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
}
