# How to use the app

Just moving the phone when in the main menu will make the ball move.
You will get a message when the ball has begun to move, and once it has landed, you will get to know how far it went.
Once it has landed, you can move the phone again to throw the ball yet again.
You will know how hard you threw the ball while the ball is in the air, but it will vanish once the ball has landed.

# Some faults that might happen

The sound will continue to play even after the ball has landed, if the throw was weak enough for it to land before the sound stopped.
You will not be able to see how far the ball went if you throw it again, so be careful and read how far it went if you want to know.

# How to build the app

Don't really know how to explain it, but to use the debug way when testing. Yes, I am that lazy.

# Features that are working

Pretty much all of them should work. It has all been tested on a Samsung Galaxy S8 with android version 9 with API 28.
Minimum API is 18.

# Features that do not work

Dunno, some things might not work on some phones.
Updates are depenent on the movement of the phone, so it will likely not work on emulators (It worked just fine on my phone, as it constantly was changing for some reason)

# How the code is organized

I just try to make it work. And at times how I do it will vary. Would have liked to have the sensor stuff in it's own class, but I couldn't figure out how to constantly check it.

# Lab 03: Throwing a virtual ball upwords 

## The idea

Create an application that allows the user to throw a virtual (non existent) ball upwards. The app can consist of one main activity and one for Settings. The app should use accelerometer to detect a "throw event", which is an acceleration above certain sensitivity threshold. This "throw event" will initiate a following sequence of events:

   * the maximum acceleration will be used as the initial speed of the ball traveling upwards. Let's say, the top acceleration was equal to double the earth gravity (19.6m/s2).
   * we will assume that the ball is then traveling upwards with the speed 19.6m/s, and that the gravity is pulling it down (slowing it down) 9.8m/s2. 
   * Roughly speaking, the ball, after 2s, should reach the highest point, and stop for a brief moment. 
   * At that top moment, the phone should play a sound indicating that the ball reached the highest point.
   * After that, the ball should fall down exactly the same amount of time, as it was traveling upwards (yes, we are ignoring the air resistance).
   * When the ball travels upwards, you should display a counter, showing, how many meters the ball has travelled up before reaching the "stop" point. This would be the "SCORE" for a given throw. 

The above should be recalculated for the actual top acceleration reached during the "throw" movement of the phone.

To calculate the actual acceleration, you should use simple equation: `ACC = sqrt(x*x + y*y + z*z) - EarthGravity` where `x, y, z` are the acceleration readings from the accelerometer, and `EarthGravity` is the constant representing EarthGravity.  Therefore, a phone placed flat on a floor should provide you value of ACC close to 0.


## Preferences

The user should be able to specify in the preferences the sensitivity threshold (use slider) of what is the minimum value of acceleration ACC that would indicate a THROW event.  Anything below that MIN_ACC value will be ignored, and not considered as throw.


## Extras

To make the app more fun, you can play a sound with decreasing frequency on the ball movement UP, and a sound with increasing frequency on the way of the ball down.

You can also keep the "high score" list of the best "throws" of the ball (the highest distance reached). 

You can provide haptic feedback to the hand holding the phone, when the ball leaves the hand (the highest acceleration reached), and then, again, when the ball comes back down to the same location from where it has been thrown. This should feel as if the ball leaves the hand, and then comes back.



# Checklist

* [x] The git repository URL is correctly provided, such that command works: `git clone <url> `
* [x] The code is well, logically organised and structured into appropriate classes. Everything should be in a single package.
* [ ] The app has been user tested with someone other than the author.
* [x] The user can go to Preferences and set the MIN_ACC value (sensitivity).
* [x] The app plays sounds on the ball highest point.
* [x] The app records the highest point reached by the ball.
* [ ] Share your sensitivity constant as well as the size of the sliding window with others.
* [x] The repo is *public*, such that reviewers can access it and review it.
* [x] The repo has a README.md file, explaining how to build the app, how to use the app, which features are working, which are not working, how the code is organised, and what extras does the project have.


## Hints

The equation for calculating the temporary acceleration at a specific time point is provided above. To know if the acceleration is the "highest", you need to use a technique called "sliding window". What it means, is that you will calculate the ACC for example for 20 accelerometer readings, and then, subsequently, keep adding new readings as they are provided from the accelerometer. If the highest temporary reading "slides out" of the 20 readings, you consider it the highest overall. This means, the ball will "start flying" actually 20 timeslots after the event happened. You can tweak the size of the window for the app not to feel laggy. If the accelerometer provides you data with frequency 100Hz, 20 readings will take 1/5th of a second. Play with the constants, to achieve the most natural feel of the game.

