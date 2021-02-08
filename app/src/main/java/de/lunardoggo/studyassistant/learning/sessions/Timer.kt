package de.lunardoggo.studyassistant.learning.sessions

import android.os.CountDownTimer
import de.lunardoggo.studyassistant.events.Event

class Timer {
    public val TimerFinished = Event<Long>();
    public val TimerTicked = Event<Long>();

    private var timer : CountDownTimer? = null;

    public var isRunning : Boolean = false
            private set;

    public fun start(milliseconds : Long) {
        if(this.timer == null && !this.isRunning) {
            this.timer = object: CountDownTimer(milliseconds, 100) {
                override fun onTick(remainingMillis: Long) {
                    TimerTicked.invoke(remainingMillis);
                }

                override fun onFinish() {
                    stop();
                    TimerFinished.invoke(0);
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