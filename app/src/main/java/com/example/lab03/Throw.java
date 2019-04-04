package com.example.lab03;

public class Throw {

    private static long maxHeightTime;
    private static long throwTime;
    private static float maxHeight;
    private static boolean reachedTop;

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
        long topTime = (long) (speed / -MainActivity.EarthGravity);
        //Set value for the time the ball will reach the top in milliseconds
        maxHeightTime = topTime * 1000 + sTime;

        //Calculate what height the ball will have when it reaches the top
        maxHeight = (float) Math.pow(speed, 2) / (2 * -MainActivity.EarthGravity);

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
            MainActivity.updateText(maxHeight);
            //If the ball is finished being thrown, end the throw
            endThrow();
        }
    }

    //End the throw (Not really needed, but could be useful if I want to end the throw early)
    //Could be used if accessing the settings while the throw is underway
    public static void endThrow() {
        throwInAction = false;
    }
}
