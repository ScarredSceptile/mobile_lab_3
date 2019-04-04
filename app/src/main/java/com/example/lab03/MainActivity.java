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
    private float ACC = 0.0f;
    private float throwMax = 0.0f;
    private long throwStart;
    private boolean throwStarted = false;

    private static Throw thrower;
    private static TextView height;
    private static TextView msg;
    private static MediaPlayer player;

    final public static float EarthGravity = -9.81f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up all the different parts of the app
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        player = MediaPlayer.create(MainActivity.this, R.raw.ping);
        height = findViewById(R.id.height);
        msg = findViewById(R.id.throwStarted);
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

    public static void updateText(float h) {
        //Give the user a message about how far up the ball reached
        height.setText("Height reached is: " + Float.toString(h));
        //Remove the message telling the user that the ball has started flying
        msg.setText("");
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
                updateACC(event);
        }
    }

    private void updateACC(SensorEvent event) {
        //Calculate the acceleration of the ball    /Lying flat, it will be about 0.2
        ACC = (float) Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2)) + EarthGravity;

        //If no throw has been started, and the acceleration is greater than the minimum
        if (!throwStarted && ACC > SettingsActivity.minACC) {
            //Start the throw-data gathering
            throwStarted = true;
            //Get the time of when the data-gathering starts
            throwStart = event.timestamp;

            //Remove the previous height
            height.setText("");

            throwMax = ACC;     //If it is the only reading to go above the lowest ACC accepted
        }

        //If the data-gathering time is not over, check if a new max speed is reached
        else if (throwStarted && event.timestamp - throwStart < 500000000) {
            if (ACC > SettingsActivity.minACC) {
                if (ACC > throwMax) {
                    throwMax = ACC;
                }
            }
        }

        //Once enough time for the throw to be registered has passed, begin the throw
        else if (throwStarted && event.timestamp - throwStart >= 500000000) {

            //Start the throw
            thrower.startThrow(event.timestamp, throwMax);

            //Message so that the user knows when the throw has started
            //And what speed the ball is going at
            msg.setText("The throw has started\nThe speed is: " + Float.toString(throwMax));

            //reset values
            throwStarted = false;
            throwMax = 0.0f;


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
