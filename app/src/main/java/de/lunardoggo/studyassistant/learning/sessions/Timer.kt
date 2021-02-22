package de.lunardoggo.studyassistant.learning.sessions

import android.os.CountDownTimer
import de.lunardoggo.studyassistant.events.Event

class Timer {
    public val timerFinished = Event<Long>();
    public val timerTicked = Event<Long>();

    private var timer : CountDownTimer? = null;

    public var isRunning : Boolean = false
            private set;

    public fun start(milliseconds : Long, tickInterval : Long) {
        if(this.timer == null && !this.isRunning) {
            this.timer = object: CountDownTimer(milliseconds, tickInterval) {
                override fun onTick(remainingMillis: Long) {
                    timerTicked.invoke(remainingMillis);
                }

                override fun onFinish() {
                    stop();
                    timerFinished.invoke(0);
                }
            }
            this.timer!!.start();
            this.isRunning = true;
        }
    }

    public fun stop() {
        if(this.timer != null) {
            this.isRunning = false;
            this.timer!!.cancel();
            this.timer = null;
        }
    }
}