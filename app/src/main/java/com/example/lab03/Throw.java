package com.example.lab03;

import android.hardware.SensorEvent;

public class Throw {

    private static long maxHeightTime;
    private static long throwTime;
    private static float maxHeight;
    private static boolean reachedTop;
    private static float throwMax = 0.0f;
    private static long throwStart;
    private boolean throwStarted = false;
    final private static float EarthGravity = -9.81f;

    public static boolean throwInAction = false;

    public Throw() {
        //Empty constructor
    }

    public static void startThrow(long startTime, float speed) {

        //Start the throw
        throwInAction = true;

        //Change startTime from nanoseconds to milliseconds
        long sTime = startTime / 1000000L;

        //Calculate when the ball will reach the top in seconds
        long topTime = (long) (speed / -EarthGravity);
        //Set value for the time the ball will reach the top in milliseconds
        maxHeightTime = topTime * 1000 + sTime;

        //Calculate what height the ball will have when it reaches the top
        maxHeight = (float) Math.pow(speed, 2) / (2 * -EarthGravity);

        //Set how long the throw will last will last in milliseconds
        throwTime = sTime + topTime * 2000;

        reachedTop = false;
    }

    public static void continueThrow(long currentTime) {
        //Check if the ball has reached the top on the milliseconds
        //Might be a bit late, but it's better than nothing
        if (!reachedTop && maxHeightTime <= currentTime / 1000000L) {
            //Ball has reached the top
            reachedTop = true;
            //Play a sound as the ball has reached the top
            MainActivity.playSound();
        }
        //Check if the ball has finished being thrown
        //Might be a bit late, but better than nothing
        if (reachedTop && throwTime <= currentTime / 1000000L) {
            //Give a message about how high the ball reached
            MainActivity.updateTextThrowEnd(maxHeight, throwMax);
            //If the ball is finished being thrown, end the throw
            endThrow();
        }
    }

    //End the throw (Not really needed, but could be useful if I want to end the throw early)
    //Could be used if accessing the settings while the throw is underway
    public static void endThrow() {
        throwInAction = false;
        throwMax = 0.0f;
    }

    public void updateACC(SensorEvent event) {
        float ACC;
        //Calculate the acceleration of the ball    /Lying flat, it will be about 0.2
        ACC = (float) Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2)) + EarthGravity;

        //If no throw has been started, and the acceleration is greater than the minimum
        if (!throwStarted && ACC > SettingsActivity.minACC) {
            //Start the throw-data gathering
            throwStarted = true;
            //Get the time of when the data-gathering starts
            throwStart = event.timestamp;

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
            startThrow(event.timestamp, throwMax);

            MainActivity.updateTextThrowStart(throwMax);

            //reset values
            throwStarted = false;


        }
    }
}
