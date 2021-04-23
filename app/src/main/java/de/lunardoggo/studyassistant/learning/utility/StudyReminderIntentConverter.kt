package de.lunardoggo.studyassistant.learning.utility

import android.content.Intent
import de.lunardoggo.studyassistant.learning.models.StudyReminder

class StudyReminderIntentConverter : IntentConverter<StudyReminder> {

    companion object {
        public val instance : StudyReminderIntentConverter = StudyReminderIntentConverter();
    }

    private val keyEpochSeconds : String = "reminder_epochSeconds";
    private val keyImportanceInt : String = "reminder_importanceInt";
    private val keyDescription : String = "reminder_description";
    private val keyTitle : String = "reminder_title";
    private val keyId : String = "reminder_id";

    override fun convertFrom(data: Intent): StudyReminder {
        val reminder = StudyReminder();
        reminder.importanceInt = data.getIntExtra(this.keyImportanceInt, 1);
        reminder.epochSeconds = data.getLongExtra(this.keyEpochSeconds, 0);
        reminder.description = data.getStringExtra(this.keyDescription);
        reminder.title = data.getStringExtra(this.keyTitle);
        reminder.id = data.getIntExtra(this.keyId, -1);
        return reminder;
    }

    override fun convertTo(reminder: StudyReminder, baseIntent: Intent): Intent {
        val intent = Intent(baseIntent);
        intent.putExtra(this.keyImportanceInt, reminder.importanceInt);
        intent.putExtra(this.keyEpochSeconds, reminder.epochSeconds);
        intent.putExtra(this.keyDescription, reminder.description);
        intent.putExtra(this.keyTitle, reminder.title);
        intent.putExtra(this.keyId, reminder.id);
        return intent;
    }

    override fun convertTo(reminder: StudyReminder) : Intent {
        return this.convertTo(reminder, Intent());
    }
}