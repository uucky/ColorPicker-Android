package me.uucky.colorpicker.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import me.uucky.colorpicker.internal.CheckableColorView;


public class MainActivity extends Activity implements OnClickListener {

    @Override
    public void onClick(View v) {
        new ColorPickerDialogFragment().show(getFragmentManager(), null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CheckableColorView checkableColorView = (CheckableColorView) findViewById(R.id.check_view);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                checkableColorView.setCheckedProgress(progress / (float) seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}
