package de.lunardoggo.studyassistant.learning.sessions

import de.lunardoggo.studyassistant.events.Event

class SessionTimer(learningMilliseconds: Long, breakMilliseconds: Long, learningIntervals: Int) {

    public val learningIntervalCompleted = Event<Int>();
    public val learningSessionCompleted = Event<Int>();
    public val learningIntervalStarted = Event<Int>();
    public val timerTicked = Event<Long>();

    private val learningMilliseconds = learningMilliseconds;
    private val breakMilliseconds = breakMilliseconds;
    private val learningIntervals = learningIntervals;

    public var tickInterval : Long = 1000
        get set;

    public var currentStatus = SessionStatus.NONE
        private set;

    private var currentLearningInterval = 0;
    private var timer : Timer = Timer();

    init {
        this.timer.timerFinished += ::onTimerFinished;
        this.timer.timerTicked += ::onTimerTicked;
    }

    public var isRunning : Boolean = false
        get() { return this.currentStatus != SessionStatus.FINISHED && this.currentStatus != SessionStatus.NONE; }

    public fun start() {
        if(!this.timer.isRunning){
            this.currentStatus = SessionStatus.LEARNING;
            this.timer.start(this.learningMilliseconds, this.tickInterval);
            this.currentLearningInterval = 0;
        }
    }

    public fun stop() {
        if(this.timer.isRunning) {
            this.currentStatus = SessionStatus.NONE;
            this.currentLearningInterval = 0;
            this.timer.stop();
        }
    }

    private fun onTimerTicked(remainingMilliseconds : Long) {
        this.timerTicked.invoke(remainingMilliseconds);
    }

    private fun onTimerFinished(placeholder: Long) {
        when(this.currentStatus) {
            SessionStatus.TAKING_BREAK -> {
                this.currentStatus = SessionStatus.LEARNING;
                this.timer.start(this.learningMilliseconds, this.tickInterval);
                this.currentLearningInterval++;
                this.learningIntervalStarted.invoke(this.currentLearningInterval);
            }
            SessionStatus.LEARNING -> {
                if(this.currentLearningInterval < this.learningIntervals - 1) {
                    this.currentStatus = SessionStatus.TAKING_BREAK;
                    this.timer.start(this.breakMilliseconds, this.tickInterval);
                    this.learningIntervalCompleted.invoke(this.currentLearningInterval);
                } else {
                    this.currentStatus = SessionStatus.FINISHED;
                    this.learningSessionCompleted.invoke(this.learningIntervals);
                }
            }
            else -> {
                //TODO Error
            }
        }
    }
}

enum class SessionStatus() {
    TAKING_BREAK(),
    LEARNING(),
    FINISHED(),
    NONE()
}