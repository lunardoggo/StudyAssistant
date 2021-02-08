package de.lunardoggo.studyassistant.learning.sessions

import de.lunardoggo.studyassistant.events.Event

class SessionTimer(learningMilliseconds: Long, breakMilliseconds: Long, learningIntervals: Int) {

    public val LearningIntervalCompleted = Event<Int>();
    public val LearningSessionCompleted = Event<Int>();
    public val LearningIntervalStarted = Event<Int>();
    public val TimerTicked = Event<Long>();

    private val learningMilliseconds = learningMilliseconds;
    private val breakMilliseconds = breakMilliseconds;
    private val learningIntervals = learningIntervals;

    public var currentStatus = SessionStatus.NONE
        private set;

    private var currentLearningInterval = 0;
    private var timer : Timer = Timer();

    init {
        this.timer.TimerFinished += ::onTimerFinished;
        this.timer.TimerTicked += ::onTimerTicked;
    }

    public var isRunning : Boolean = false
        get() { return this.currentStatus != SessionStatus.FINISHED && this.currentStatus != SessionStatus.NONE; }

    public fun start() {
        if(!this.timer.isRunning){
            this.currentStatus = SessionStatus.LEARNING;
            this.timer.start(this.learningMilliseconds);
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
        this.TimerTicked.invoke(remainingMilliseconds);
    }

    private fun onTimerFinished(placeholder: Long) {
        when(this.currentStatus) {
            SessionStatus.TAKING_BREAK -> {
                this.currentStatus = SessionStatus.LEARNING;
                this.timer.start(this.learningMilliseconds);
                this.currentLearningInterval++;
                this.LearningIntervalStarted.invoke(this.currentLearningInterval);
            }
            SessionStatus.LEARNING -> {
                if(this.currentLearningInterval < this.learningIntervals - 1) {
                    this.currentStatus = SessionStatus.TAKING_BREAK;
                    this.timer.start(this.breakMilliseconds);
                    this.LearningIntervalCompleted.invoke(this.currentLearningInterval);
                } else {
                    this.currentStatus = SessionStatus.FINISHED;
                    this.LearningSessionCompleted.invoke(this.learningIntervals);
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