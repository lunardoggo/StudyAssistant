package de.lunardoggo.studyassistant.ui.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.sessions.SessionStatus
import de.lunardoggo.studyassistant.learning.sessions.SessionTimer
import java.text.DecimalFormat

class TimerFragment : Fragment() {

    private val learningMilliseconds : Long = 1000 * 60 * 15;
    private val breakMilliseconds : Long = 1000 * 60 * 5;
    private val numberFormat = DecimalFormat("00");

    private var timer : SessionTimer = SessionTimer(this.learningMilliseconds, this.breakMilliseconds, 2);
    private var lastSessionStatus : SessionStatus = SessionStatus.NONE;

    private lateinit var timerProgress : ProgressBar;
    private lateinit var toggleStartButton : Button;
    private lateinit var timerLabel : TextView;

    init {
        this.timer.LearningIntervalCompleted += ::onTimerLearningIntervalFinished;
        this.timer.LearningIntervalStarted += ::onTimerLearningIntervalStarted;
        this.timer.LearningSessionCompleted += ::onLearningSessionCompleted;
        this.timer.TimerTicked += ::onTimerTicked;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.fragment_timer, container, false);
        this.timerProgress = fragment.findViewById(R.id.timerProgress);
        this.timerLabel = fragment.findViewById(R.id.timerLabel);

        this.toggleStartButton = fragment.findViewById(R.id.toggleStartButton);
        this.toggleStartButton.setOnClickListener { _view ->
            this.onToggleStartPressed(_view);
        };
        this.resetTimerDisplay(SessionStatus.NONE);

        return fragment;
    }

    private fun onToggleStartPressed(view : View) {
        if(this.timer.isRunning) {
            this.timer.stop();
            this.resetTimerDisplay(SessionStatus.NONE);
        } else {
            this.timer.start();
            this.updateTimerDisplay(this.learningMilliseconds, this.learningMilliseconds, SessionStatus.LEARNING);
        }
        this.updateStartToggleButtonText();
    }

    private fun onTimerLearningIntervalFinished(intervalNumber : Int) {
        this.playIntervalNotificationSound(AudioManager.STREAM_ALARM);
    }

    private fun onTimerLearningIntervalStarted(intervalNumber : Int) {
        this.playIntervalNotificationSound(AudioManager.STREAM_ALARM);
    }

    private fun onLearningSessionCompleted(completedIntervals : Int) {
        this.resetTimerDisplay(SessionStatus.FINISHED);
        this.playIntervalNotificationSound(AudioManager.STREAM_ALARM);
    }

    private fun onTimerTicked(remainingMilliseconds: Long) {
        if(this.timer.currentStatus == SessionStatus.LEARNING) {
            this.updateTimerDisplay(this.learningMilliseconds, remainingMilliseconds, this.timer.currentStatus);
        } else {
            this.updateTimerDisplay(this.breakMilliseconds, remainingMilliseconds, this.timer.currentStatus);
        }
    }

    private fun updateTimerDisplay(totalMilliseconds : Long, remainingMilliseconds : Long, currentStatus : SessionStatus) {
        val minutes = (remainingMilliseconds.toDouble() / (60 * 1000)).toLong();
        val seconds = ((remainingMilliseconds.toDouble() / 1000) - (minutes * 60)).toLong();
        val percent = ((remainingMilliseconds.toDouble() / totalMilliseconds) * 1000).toInt();

        if(this.lastSessionStatus != currentStatus) {
            this.timerProgress.progressDrawable = this.getProgressDrawable(currentStatus);
            this.lastSessionStatus = currentStatus;
        }
        this.timerLabel.text = "${this.numberFormat.format(minutes)}:${this.numberFormat.format(seconds)}";

        this.timerProgress.progress = percent;
    }

    private fun updateStartToggleButtonText() {
        if(this.timer.isRunning) {
            this.toggleStartButton.text = this.getString(R.string.stop)!!;
            this.toggleStartButton.setTextColor(ContextCompat.getColor(this.context!!, R.color.lightRed));
        } else {
            this.toggleStartButton.text = this.getString(R.string.start)!!;
            this.toggleStartButton.setTextColor(ContextCompat.getColor(this.context!!, R.color.green));
        }
    }

    private fun resetTimerDisplay(newStatus : SessionStatus) {
        this.updateTimerDisplay(1, 0, newStatus);
        this.timerProgress.progress = this.timerProgress.max;
        this.updateStartToggleButtonText();
    }

    private fun getProgressDrawable(status : SessionStatus) : Drawable {
        return when(status) {
            SessionStatus.LEARNING -> ContextCompat.getDrawable(this.context!!, R.drawable.circle_light_red)!!;
            SessionStatus.TAKING_BREAK -> ContextCompat.getDrawable(this.context!!, R.drawable.circle_green)!!;
            SessionStatus.FINISHED -> ContextCompat.getDrawable(this.context!!, R.drawable.circle_gold)!!;
            else -> ContextCompat.getDrawable(this.context!!, R.drawable.circle_gray)!!;
        }
    }

    private fun playIntervalNotificationSound(soundId : Int) {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        val player = MediaPlayer();
        player.setDataSource(this.context!!, uri);
        val audio = this.context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager;

        if(audio.getStreamVolume(soundId) != 0) {
            player.setAudioStreamType(soundId);
            player.prepare();
            player.start();
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TimerFragment().apply { arguments = Bundle().apply { } }
    }
}