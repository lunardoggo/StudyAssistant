package de.lunardoggo.studyassistant.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.android.NotificationHelper
import de.lunardoggo.studyassistant.config.ConfigurationLoader

class SettingsActivity : AppCompatActivity() {

    private lateinit var editIntervalCount : EditText;
    private lateinit var editLearnTimer : EditText;
    private lateinit var editBreakTimer : EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        this.editIntervalCount = this.findViewById(R.id.editIntervalCount);
        this.editLearnTimer = this.findViewById(R.id.editLearnTimer);
        this.editBreakTimer = this.findViewById(R.id.editBreakTimer);

        this.editIntervalCount.setOnFocusChangeListener(::onEditNumberSessionIntervals);
        this.editLearnTimer.setOnFocusChangeListener(::onEditNumberSessionTimers);
        this.editBreakTimer.setOnFocusChangeListener(::onEditNumberSessionTimers);
    }

    private fun onEditNumberSessionTimers(view : View, hasFocus : Boolean) {
        if(!hasFocus && view is EditText) {
            this.validateEditNumberValue(view as EditText, 1, 120);
        }
    }

    private fun onEditNumberSessionIntervals(view : View, hasFocus : Boolean) {
        if(!hasFocus && view is EditText) {
            this.validateEditNumberValue(view as EditText, 1, 20);
        }
    }

    private fun validateEditNumberValue(edit : EditText, min : Int, max : Int) {
        val number = edit.text.toString().toInt();
        if(number < min || number > max) {
            edit.error = edit.text.subSequence(0, edit.text.length - 1);
        }
    }

    override fun onStart() {
        super.onStart();
        this.loadSettings();
    }

    override fun onResume() {
        super.onResume();
        this.loadSettings();
    }

    override fun onPause() {
        super.onPause();
        this.saveSettings();
    }

    override fun onStop() {
        super.onStop();
        this.saveSettings();
    }

    private fun saveSettings() {
        val config = ConfigurationLoader.instance.load(this.applicationContext);

        config.studySessions.learningTimeMinutes = this.editLearnTimer.text.toString().toInt();
        config.studySessions.breakTimeMinutes = this.editBreakTimer.text.toString().toInt();
        config.studySessions.intervalCount = this.editIntervalCount.text.toString().toInt();

        ConfigurationLoader.instance.save(this.applicationContext, config);
        NotificationHelper.showToastShortDuration(this.applicationContext, "Config saved.", Toast.LENGTH_SHORT);
    }

    private fun loadSettings() {
        val config = ConfigurationLoader.instance.load(this.applicationContext);

        this.editIntervalCount.text.clear();
        this.editIntervalCount.text.append(config.studySessions.intervalCount.toString());
        this.editLearnTimer.text.clear();
        this.editLearnTimer.text.append(config.studySessions.learningTimeMinutes.toString());
        this.editBreakTimer.text.clear();
        this.editBreakTimer.text.append(config.studySessions.breakTimeMinutes.toString());
    }
}