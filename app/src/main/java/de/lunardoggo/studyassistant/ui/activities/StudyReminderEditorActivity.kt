package de.lunardoggo.studyassistant.ui.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.android.NotificationHelper
import de.lunardoggo.studyassistant.learning.models.Importance
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import de.lunardoggo.studyassistant.learning.utility.StudyReminderIntentConverter
import de.lunardoggo.studyassistant.ui.main.ResultCodes
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalField
import java.util.*

class StudyReminderEditorActivity : AppCompatActivity() {

    private val dateSetListener = this.getOnDateSetListener();

    private lateinit var reminder : StudyReminder;

    private lateinit var descriptionInput : EditText;
    private lateinit var importanceInput : Spinner;
    private lateinit var titleInput : EditText;
    private lateinit var dateInput : EditText;
    private lateinit var saveButton : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_study_reminder_editor);

        this.reminder = this.getStudyReminder();

        this.descriptionInput = this.findViewById(R.id.reminderDescriptionInput);
        this.importanceInput = this.findViewById(R.id.reminderImportanceInput);
        this.titleInput = this.findViewById(R.id.reminderTitleInput);
        this.saveButton = this.findViewById(R.id.reminderSaveButton);
        this.dateInput = this.findViewById(R.id.reminderDateInput);

        this.saveButton.setOnClickListener(::onSaveReminderClicked);
        this.importanceInput.adapter = this.getImportanceAdapter();
        this.dateInput.setOnClickListener(::onDateInputClicked);

        this.updateDisplayValues();
    }

    private fun getImportanceAdapter() : ArrayAdapter<Importance> {
        return ArrayAdapter<Importance>(this.applicationContext, android.R.layout.simple_spinner_item, Importance.values().sortedByDescending { _importance -> _importance.intValue });
    }

    private fun getStudyReminder() : StudyReminder {
        return try {
            StudyReminderIntentConverter.instance.convertFrom(this.intent);
        } catch (ex : Exception) {
            val reminder = StudyReminder();
            reminder.date = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
            reminder.importance = Importance.NORMAL;
            reminder;
        }
    }

    private fun onSaveReminderClicked(view: View?) {
        this.updateReminderValues();
        if(this.reminder.title.isNotEmpty() && this.reminder.description.isNotEmpty()) {
            this.setResult(ResultCodes.RESULT_OK, StudyReminderIntentConverter.instance.convertTo(this.reminder));
            this.finish();
        } else {
            NotificationHelper.showToastShortDuration(this.applicationContext, "Please fill all inputs", Toast.LENGTH_LONG);
        }
    }

    private fun onDateInputClicked(view: View?) {
        val date = this.reminder.date.atZone(ZoneId.systemDefault());
        DatePickerDialog(view!!.context, this.dateSetListener, date.year, date.monthValue - 1, date.dayOfMonth).show();
    }

    private fun getOnDateSetListener() : DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { _picker, _year, _month, _day ->
            run {
                this.updateReminderValues();
                this.reminder.date = LocalDate.of(_year, _month, _day).atStartOfDay(ZoneId.systemDefault()).toInstant();
                this.updateDisplayValues();
            }
        };
    }

    private fun updateDisplayValues() {
        this.titleInput.setText(this.reminder.title);

        val format = DateFormat.getDateFormat(this.applicationContext);
        this.dateInput.setText(format.format(Date.from(this.reminder.date)));

        this.importanceInput.setSelection((this.importanceInput.adapter as ArrayAdapter<Importance>).getPosition(this.reminder.importance));

        this.descriptionInput.setText(this.reminder.description);
    }

    private fun updateReminderValues() {
        this.reminder.importance = this.importanceInput.selectedItem as Importance;
        this.reminder.description = this.descriptionInput.text.toString();
        this.reminder.title = this.titleInput.text.toString();
    }
}