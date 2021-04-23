package de.lunardoggo.studyassistant

import android.content.Intent
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import de.lunardoggo.studyassistant.learning.utility.StudyReminderIntentConverter
import org.junit.Assert.*
import org.junit.Test

class StudyReminderIntentConverterTests {

    @Test
    public fun testConvertToStudyReminder() {
        val intent = this.getIntent(1, "Test", 1, 0);

        val reminder = StudyReminderIntentConverter.instance.convertFrom(intent);

        assertEquals("Test", reminder.description);
        assertEquals(1, reminder.importanceInt);
        assertEquals(0, reminder.epochSeconds);
        assertEquals(1, reminder.id);
    }

    @Test
    public fun testConvertToIntent() {
        val reminder = this.getStudyReminder(1, "Test", 1, 0);

        val intent = StudyReminderIntentConverter.instance.convertTo(reminder);

        assertEquals(1, intent.getIntExtra("reminder_importanceInt", -1));
        assertEquals(0, intent.getLongExtra("reminder_epochSeconds", -1));
        assertEquals("Test", intent.getStringExtra("reminder_description"));
        assertEquals(1, intent.getIntExtra("reminder_id", -1));
    }


    private fun getIntent(id : Int, description : String, importance : Int, epochSeconds : Long) : Intent {
        val intent = Intent();
        intent.putExtra("reminder_epochSeconds", epochSeconds);
        intent.putExtra("reminder_importanceInt", importance);
        intent.putExtra("reminder_description", description);
        intent.putExtra("reminder_id", id);
        return intent;
    }

    private fun getStudyReminder(id : Int, description : String, importance : Int, epochSeconds : Long) : StudyReminder {
        val reminder = StudyReminder();
        reminder.epochSeconds = epochSeconds;
        reminder.importanceInt = importance;
        reminder.description = description;
        reminder.id = id;
        return reminder;
    }
}