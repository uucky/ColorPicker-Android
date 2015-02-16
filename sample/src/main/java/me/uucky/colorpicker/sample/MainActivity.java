package me.uucky.colorpicker.sample;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ColorPickerDialogFragment().show(getFragmentManager(), null);
    }


}
