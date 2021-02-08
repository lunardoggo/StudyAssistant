package de.lunardoggo.studyassistant.ui.main

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.sessions.SessionStatus
import de.lunardoggo.studyassistant.learning.sessions.SessionTimer
import java.text.DecimalFormat

class TimerFragment : Fragment() {

    private val learningMilliseconds : Long = 1000 * 5;
    private val breakMilliseconds : Long = 1000 * 2;
    private val numberFormat = DecimalFormat("00");

    private var timer : SessionTimer = SessionTimer(this.learningMilliseconds, this.breakMilliseconds, 2);

    private lateinit var timerProgress : ProgressBar;
    private lateinit var toggleStartButton : Button;
    private lateinit var timerLabel : TextView;
    private lateinit var pauseButton : Button;

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
        this.pauseButton = fragment.findViewById(R.id.pauseButton);
        this.pauseButton.setOnClickListener { _view ->
            this.onPausePressed(_view);
        };

        return fragment;
    }

    private fun onToggleStartPressed(view : View) {
        this.timer.start();
    }

    private fun onPausePressed(view : View) {
    }

    private fun onTimerLearningIntervalFinished(intervalNumber : Int) {
        this.playIntervalNotificationSound(AudioManager.STREAM_ALARM);
    }

    private fun onTimerLearningIntervalStarted(intervalNumber : Int) {
        this.playIntervalNotificationSound(AudioManager.STREAM_ALARM);
    }

    private fun onLearningSessionCompleted(completedIntervals : Int) {
        this.updateTimerDisplay(this.learningMilliseconds, 0, SessionStatus.FINISHED);
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

        this.timerLabel.text = "${this.numberFormat.format(minutes)}:${this.numberFormat.format(seconds)}";

        this.timerProgress.progress = percent;
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