package me.uucky.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.AllCaps;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.util.ArrayList;

import me.uucky.colorpicker.internal.CheckableColorView;
import me.uucky.colorpicker.internal.ColorView;
import me.uucky.colorpicker.internal.graphic.AlphaBarDrawable;
import me.uucky.colorpicker.internal.graphic.HueBarDrawable;
import me.uucky.colorpicker.internal.graphic.SaturationBarDrawable;
import me.uucky.colorpicker.internal.graphic.ValueBarDrawable;

/**
 * Created by mariotaku on 15/2/15.
 */
@SuppressWarnings("unused")
public final class ColorPickerDialog extends AlertDialog {

    private final Controller controller;

    public ColorPickerDialog(final Context context) {
        super(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        final View view = inflater.inflate(R.layout.cp__dialog_color_picker, null);
        setView(view);
        controller = new Controller(context, view);
        setOnShowListener(controller);
    }

    public void setInitialColor(int color) {
        controller.setInitialColor(color);
    }

    public void setColor(int color) {
        controller.setColor(color);
    }

    public int getColor() {
        return controller.getColor();
    }

    public void addColor(int... colors) {
        controller.addColor(colors);
    }


    public void setAlphaEnabled(boolean alphaEnabled) {
        controller.setAlphaEnabled(alphaEnabled);
    }

    public void setPresetsEnabled(boolean enabled) {
        controller.setPresetsEnabled(enabled);
    }

    public interface ColorPickerListener {
        void onClick(DialogInterface dialog, int color);
    }


    public void setNegativeButton(CharSequence text, ColorPickerListener listener) {
        setButton(BUTTON_NEGATIVE, text, new InternalClickListener(controller, listener));
    }

    public void setPositiveButton(CharSequence text, ColorPickerListener listener) {
        setButton(BUTTON_POSITIVE, text, new InternalClickListener(controller, listener));
    }

    public void setNeutralButton(CharSequence text, ColorPickerListener listener) {
        setButton(BUTTON_NEUTRAL, text, new InternalClickListener(controller, listener));
    }

    public final static class Controller implements OnShowListener {

        private final ColorsAdapter colorsAdapter;

        private RecyclerView colorPresetsView;
        private EditText editHexColor;
        private SeekBar hueSeekBar, saturationSeekBar, valueSeekBar, alphaSeekBar;
        private ColorView oldColorView, newColorView;

        public static Controller applyToDialogBuilder(AlertDialog.Builder builder) {
            final LayoutInflater inflater = LayoutInflater.from(builder.getContext());
            @SuppressLint("InflateParams")
            final View view = inflater.inflate(R.layout.cp__dialog_color_picker, null);
            builder.setView(view);
            return new Controller(builder.getContext(), view);
        }


        public Controller(final Context context, View view) {
            colorsAdapter = new ColorsAdapter(this, context);
            colorPresetsView = (RecyclerView) view.findViewById(R.id.color_presets);
            hueSeekBar = (SeekBar) view.findViewById(R.id.hue_seekbar);
            saturationSeekBar = (SeekBar) view.findViewById(R.id.saturation_seekbar);
            valueSeekBar = (SeekBar) view.findViewById(R.id.value_seekbar);
            alphaSeekBar = (SeekBar) view.findViewById(R.id.alpha_seekbar);
            editHexColor = (EditText) view.findViewById(R.id.color_hex);
            oldColorView = (ColorView) view.findViewById(R.id.old_color);
            newColorView = (ColorView) view.findViewById(R.id.new_color);
            editHexColor.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(8),
                    new AllCaps(),
//                new HexFilter()
            });
            editHexColor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        setColor(Color.parseColor("#" + s));
                        editHexColor.setError(null);
                    } catch (IllegalArgumentException e) {
                        editHexColor.setError(context.getString(R.string.invalid_hex_color));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            final Resources res = context.getResources();
            hueSeekBar.setProgressDrawable(new HueBarDrawable(res));
            hueSeekBar.setOnSeekBarChangeListener(new AbsOnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    updateHuePreview();
                    updateColorPreviewFromSeekBars();
                }
            });
            saturationSeekBar.setProgressDrawable(new SaturationBarDrawable(res));
            saturationSeekBar.setOnSeekBarChangeListener(new AbsOnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    updateSaturationPreview();
                    updateColorPreviewFromSeekBars();
                }

            });
            valueSeekBar.setProgressDrawable(new ValueBarDrawable(res));
            valueSeekBar.setOnSeekBarChangeListener(new AbsOnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    updateColorPreviewFromSeekBars();
                }
            });
            alphaSeekBar.setProgressDrawable(new AlphaBarDrawable(res));
            alphaSeekBar.setOnSeekBarChangeListener(new AbsOnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    updateColorPreviewFromSeekBars();
                }
            });

            colorPresetsView.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false));
            colorPresetsView.setAdapter(colorsAdapter);
            setColor(Color.WHITE);
        }


        public void addColor(int... colors) {
            colorsAdapter.addAll(colors);
        }

        public int getColor() {
            return newColorView.getColor();
        }

        public void setColor(int color) {
            if (color == newColorView.getColor()) return;
            newColorView.setColor(color);
            colorsAdapter.setCurrentColor(color);
            final int alpha = Color.alpha(color);
            final float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            setHue(hsv[0]);
            setSaturation(hsv[1]);
            setValue(hsv[2]);
            setAlpha(alpha);
            setColorText(color);
            updateHuePreview();
            updateSaturationPreview();
        }

        public void setInitialColor(int color) {
            oldColorView.setColor(color);
            setColor(color);
        }

        @Override
        public void onShow(DialogInterface dialog) {
            updateHuePreview();
            updateSaturationPreview();
            updateColorPreview();
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

        private int calculateColor() {
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

        private void updateColorPreview() {
            final AlphaBarDrawable alphaDrawable = (AlphaBarDrawable) alphaSeekBar.getProgressDrawable();
            final int color = getColor();
            setColorText(color);
            newColorView.setColor(color);
            alphaDrawable.setColor(color);
            colorsAdapter.setCurrentColor(color);
        }

        private void setColorText(int color) {
            if (isAlphaEnabled()) {
                editHexColor.setText(String.format("%08x", color));
            } else {
                editHexColor.setText(String.format("%06x", color & 0x00FFFFFF));
            }
        }

        private void updateColorPreviewFromSeekBars() {
            final AlphaBarDrawable alphaDrawable = (AlphaBarDrawable) alphaSeekBar.getProgressDrawable();
            final int color = calculateColor();
            setColorText(color);
            newColorView.setColor(color);
            alphaDrawable.setColor(color);
            colorsAdapter.setCurrentColor(color);
        }

        private void updateHuePreview() {
            final SaturationBarDrawable saturationDrawable = (SaturationBarDrawable) saturationSeekBar.getProgressDrawable();
            final ValueBarDrawable valueDrawable = (ValueBarDrawable) valueSeekBar.getProgressDrawable();
            final float hue = getHue();
            valueDrawable.setHue(hue);
            saturationDrawable.setHue(hue);
        }

        private void updateSaturationPreview() {
            final ValueBarDrawable valueDrawable = (ValueBarDrawable) valueSeekBar.getProgressDrawable();
            valueDrawable.setSaturation(getSaturation());
        }

        public void setPresetsEnabled(boolean enabled) {
            colorPresetsView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    private static class InternalClickListener implements DialogInterface.OnClickListener {

        private final Controller controller;
        private final ColorPickerListener listener;

        InternalClickListener(Controller controller, ColorPickerListener listener) {
            this.controller = controller;
            this.listener = listener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (listener == null) return;
            listener.onClick(dialog, controller.getColor());
        }
    }


    private static class ColorsAdapter extends Adapter<ColorViewHolder> {

        private final Controller mDialogController;
        private final LayoutInflater mInflater;
        private final ArrayList<Integer> mColors;
        private int mCurrentColor;

        public ColorsAdapter(Controller dialogController, final Context context) {
            setHasStableIds(true);
            mColors = new ArrayList<>();
            mDialogController = dialogController;
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
            holder.setColor(color);
            holder.setChecked(mCurrentColor == color, true);
        }

        @Override
        public void onViewRecycled(ColorViewHolder holder) {
            holder.setChecked(false, false);
            super.onViewRecycled(holder);
        }

        @Override
        public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = mInflater.inflate(R.layout.cp__adapter_item_color_picker_preset, parent, false);
            return new ColorViewHolder(view, this, mDialogController);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mColors.size();
        }

    }

    public static class ColorViewHolder extends ViewHolder implements View.OnClickListener {

        private final CheckableColorView colorView;
        private final ColorsAdapter mAdapter;
        private final Controller mDialogController;

        public ColorViewHolder(View itemView, ColorsAdapter adapter, Controller dialog) {
            super(itemView);
            mAdapter = adapter;
            mDialogController = dialog;
            itemView.setOnClickListener(this);
            colorView = (CheckableColorView) itemView.findViewById(R.id.item_color);
        }

        @Override
        public void onClick(View v) {
            mDialogController.setColor(mAdapter.getColor(getAdapterPosition()));
        }

        public void setChecked(boolean checked, boolean animate) {
            colorView.setChecked(checked, animate);
        }

        public void setColor(int color) {
            colorView.setColor(color);
        }
    }

    private static class AbsOnSeekBarChangeListener implements OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private static class HexFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source == null) return null;
            final String s = source.toString().replaceAll("[^a-fA-F1-9]", "");
            if (source instanceof Spanned) {
                SpannableString sp = new SpannableString(s);
                TextUtils.copySpansFrom((Spanned) source,
                        start, end, null, sp, 0);
                return sp;
            } else {
                return s;
            }
        }
    }

}
