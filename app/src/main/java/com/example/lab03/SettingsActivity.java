package com.example.lab03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public static int minACC = 2;     //Basic start position of it
    private TextView sensitivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SeekBar seekBar = findViewById(R.id.slider);
        sensitivity = findViewById(R.id.sensitivity);

        seekBar.setMax(99);
        //Since the seekBar goes from 0-99, and we have values from 1-100
        //Reduce the value by one to get an accurate seekBar progress
        seekBar.setProgress(minACC - 1);

        //seekBar progress value will always be 1 lower than we want, so increase it by 1
        int progress = seekBar.getProgress() + 1;
        sensitivity.setText(Integer.toString(progress));

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            sensitivity.setText(Integer.toString(progress + 1));
            minACC = progress + 1;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
