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
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ColorPickerDialog dialog = new ColorPickerDialog(getActivity());
        final Resources res = getResources();
        dialog.addColor(res.getColor(R.color.material_red));
        dialog.addColor(res.getColor(R.color.material_pink));
        dialog.addColor(res.getColor(R.color.material_purple));
        dialog.addColor(res.getColor(R.color.material_indigo));
        dialog.addColor(res.getColor(R.color.material_blue));
        dialog.addColor(res.getColor(R.color.material_cyan));
        dialog.addColor(res.getColor(R.color.material_green));
        dialog.addColor(res.getColor(R.color.material_lime));
        dialog.addColor(res.getColor(R.color.material_yellow));
        dialog.addColor(res.getColor(R.color.material_amber));
        dialog.addColor(res.getColor(R.color.material_orange));
        dialog.addColor(res.getColor(R.color.material_deep_orange));
        final Activity activity = getActivity();
        final View colorView = activity.findViewById(R.id.color_view);
        final Drawable backgroundDrawable = colorView.getBackground();
        if (backgroundDrawable instanceof ColorDrawable) {
            dialog.setColor(((ColorDrawable) backgroundDrawable).getColor());
        }
        dialog.setPositiveButton(getString(android.R.string.ok), new ColorPickerListener() {
            @Override
            public void onClick(ColorPickerDialog dialog, int color) {
                colorView.setBackgroundColor(color);
            }
        });
        return dialog;
    }
}
