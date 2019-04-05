package com.example.lab03;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    private static Throw thrower;
    private static TextView height;
    private static MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up all the different parts of the app
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        player = MediaPlayer.create(MainActivity.this, R.raw.ping);
        height = findViewById(R.id.height);
        final Button settings = findViewById(R.id.btn_settings);
        thrower = new Throw();

        //Go into the settings when the settings button is clicked
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void updateTextThrowStart(float speed) {
        //Tell the user that the throw has started, and how fast they threw so they know about how long they need to wait for it to come back
        height.setText("The throw has started with a speed of " + Float.toString(speed) + "m/s");
    }

    public static void updateTextThrowEnd(float h, float speed) {
        //Give the user a message about how far up the ball reached
        height.setText("Height reached is: " + Float.toString(h) + "m\nSpeed was: " + Float.toString(speed) + "m/s");
    }

    public static void playSound() {
        //Play the sound
        player.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //If a ball is in the air, continue the throw
            if (Throw.throwInAction)
                Throw.continueThrow(event.timestamp);
            //Else check if a new throw can be started/continue the start
            else
                thrower.updateACC(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
