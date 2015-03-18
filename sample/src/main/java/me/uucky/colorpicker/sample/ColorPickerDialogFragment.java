package me.uucky.colorpicker.sample;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import me.uucky.colorpicker.ColorPickerDialog;
import me.uucky.colorpicker.ColorPickerDialog.ColorPickerListener;


/**
 * Created by mariotaku on 15/2/15.
 */
public class ColorPickerDialogFragment extends DialogFragment {


    private final static int[] COLORS = {R.color.material_red, R.color.material_pink,
            R.color.material_purple, R.color.material_deep_purple, R.color.material_indigo,
            R.color.material_blue, R.color.material_light_blue, R.color.material_cyan,
            R.color.material_teal, R.color.material_green, R.color.material_light_green,
            R.color.material_lime, R.color.material_yellow, R.color.material_amber,
            R.color.material_orange, R.color.material_deep_orange};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ColorPickerDialog dialog = new ColorPickerDialog(getActivity());
        final Resources res = getResources();
        for (int color : COLORS) {
            dialog.addColor(res.getColor(color));
        }
        final Activity activity = getActivity();
        final View colorView = activity.findViewById(R.id.color_view);
        final Drawable backgroundDrawable = colorView.getBackground();
        if (backgroundDrawable instanceof ColorDrawable) {
            dialog.setInitialColor(((ColorDrawable) backgroundDrawable).getColor());
        }
        dialog.setPresetsEnabled(true);
        dialog.setAlphaEnabled(true);
        dialog.setPositiveButton(getString(android.R.string.ok), new ColorPickerListener() {
            @Override
            public void onClick(ColorPickerDialog dialog, int color) {
                colorView.setBackgroundColor(color);
            }
        });
        return dialog;
    }
}
