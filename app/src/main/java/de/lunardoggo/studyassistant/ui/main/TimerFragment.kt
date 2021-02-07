package de.lunardoggo.studyassistant.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import de.lunardoggo.studyassistant.R
import java.text.DecimalFormat

class TimerFragment : Fragment() {

    private val timerTime : Long = 25 * 60 * 1000;
    private val timeFormat = DecimalFormat("00");

    private val timer = object: CountDownTimer(timerTime, 100) {
        override fun onTick(remainingMillis: Long) {
            val minutes = (remainingMillis.toDouble() / (1000 * 60)).toInt();
            val seconds = ((remainingMillis.toDouble() / 1000) % 60).toInt();
            val percent = ((remainingMillis.toDouble() / timerTime) * 1000).toInt();

            timerLabel.text = "${timeFormat.format(minutes)}:${timeFormat.format(seconds)}";
            timerProgress.progress = percent.toInt();
        }

        override fun onFinish() {

        }
    }

    private lateinit var timerProgress : ProgressBar;
    private lateinit var toggleStartButton : Button;
    private lateinit var timerLabel : TextView;
    private lateinit var pauseButton : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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

    companion object {
        @JvmStatic
        fun newInstance() = TimerFragment().apply { arguments = Bundle().apply { } }
    }
}