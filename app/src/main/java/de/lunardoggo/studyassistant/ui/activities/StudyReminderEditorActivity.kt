package de.lunardoggo.studyassistant.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.android.AlertHelper
import de.lunardoggo.studyassistant.android.NotificationPublisher
import de.lunardoggo.studyassistant.learning.models.Importance
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import de.lunardoggo.studyassistant.learning.utility.DateTimeUtility
import de.lunardoggo.studyassistant.learning.utility.StudyReminderIntentConverter
import de.lunardoggo.studyassistant.ui.main.ResultCodes
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

class StudyReminderEditorActivity : AppCompatActivity() {

    private lateinit var reminder: StudyReminder;

    private lateinit var descriptionInput: EditText;
    private lateinit var importanceInput: Spinner;
    private lateinit var titleInput: EditText;
    private lateinit var dateInput: EditText;
    private lateinit var saveButton: Button;

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_study_reminder_editor, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reminderDeleteButton -> this.showDeleteStudyReminder();
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteStudyReminder() {
        if (this.reminder.id == StudyReminder.DefaultId) {
            this.requestDeleteStudyReminder();
        } else {
            val message = this.getString(R.string.alert_study_reminder_delete_message);
            val title = this.getString(R.string.alert_study_reminder_delete_title);
            AlertHelper.createYesNoAlert(this, title, message) { _dialog, _ ->
                _dialog.dismiss();
                this.requestDeleteStudyReminder();
            }.show();
        }
    }

    private fun requestDeleteStudyReminder() {
        val data = StudyReminderIntentConverter.instance.convertTo(this.reminder);
        this.setResult(ResultCodes.RESULT_DELETE_STUDY_REMINDER, data);
        this.finish();
    }


    private fun getImportanceAdapter(): ArrayAdapter<Importance> {
        val importanceList =
            Importance.values().sortedByDescending { _importance -> _importance.intValue };
        val layoutId = android.R.layout.simple_spinner_item;
        return ArrayAdapter<Importance>(this.applicationContext, layoutId, importanceList);
    }

    private fun getStudyReminder(): StudyReminder {
        return try {
            StudyReminderIntentConverter.instance.convertFrom(this.intent);
        } catch (ex: Exception) {
            val reminder = StudyReminder();
            reminder.date = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
            reminder.importance = Importance.NORMAL;
            reminder;
        }
    }

    private fun onSaveReminderClicked(view: View?) {
        this.updateReminderValues();
        if (this.reminder.title.isNotEmpty()) {
            this.setResult(ResultCodes.RESULT_OK, StudyReminderIntentConverter.instance.convertTo(this.reminder));
            this.finish();
        } else {
            val text = this.getString(R.string.study_reminder_fields_empty);
            NotificationPublisher.showToastShortDuration(this.applicationContext, text, Toast.LENGTH_LONG);
        }
    }

    private fun onDateInputClicked(view: View?) {
        val date = this.reminder.date.atZone(ZoneId.systemDefault());
        DatePickerDialog(view!!.context, ::onReminderDateSet, date.year, date.monthValue - 1, date.dayOfMonth).show();
    }

    private fun onReminderDateSet(picker: DatePicker, year: Int, month: Int, day: Int) {
        this.updateReminderValues();
        val previousDate = this.reminder.date.atZone(ZoneOffset.systemDefault());
        //month is 0-based, while LocalDate is 1-based -> increment by 1
        this.reminder.date = LocalDate.of(year, month + 1, day).atStartOfDay(ZoneId.systemDefault()).toInstant();
        TimePickerDialog(picker.context, ::onReminderTimeSet, previousDate.hour, previousDate.minute, DateFormat.is24HourFormat(this.applicationContext)).show();
    }

    private fun onReminderTimeSet(picker: TimePicker, hour: Int, minute: Int) {
        this.reminder.date = this.reminder.date.plusSeconds(((hour * 60 + minute) * 60).toLong());
        this.updateDisplayValues();
    }


    private fun updateDisplayValues() {
        this.titleInput.setText(this.reminder.title);

        this.dateInput.setText(
            DateTimeUtility.getFormattedReminderDateTime(
                this.applicationContext,
                this.reminder.date
            )
        );

        val adapter = this.importanceInput.adapter as ArrayAdapter<Importance>;
        this.importanceInput.setSelection(adapter.getPosition(this.reminder.importance));

        this.descriptionInput.setText(this.reminder.description);
    }

    private fun updateReminderValues() {
        this.reminder.importance = this.importanceInput.selectedItem as Importance;
        this.reminder.description = this.descriptionInput.text.toString();
        this.reminder.title = this.titleInput.text.toString();
    }
}