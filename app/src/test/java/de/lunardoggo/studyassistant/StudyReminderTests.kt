package de.lunardoggo.studyassistant

import de.lunardoggo.studyassistant.learning.models.Importance
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import org.junit.Test

import org.junit.Assert.*
import java.time.Instant

class StudyReminderTests {
    @Test
    public fun testCreateReminder() {
        //Test-reminder for normal priority at  2020-01-01 17:30:00 GMT/UTC
        val reminder : StudyReminder = this.getStudyReminder(0, "Test", 1, 1577899800);

        assertEquals(0, reminder.id);
        assertEquals("Test", reminder.description);
        assertEquals(1, reminder.importanceInt);
        assertEquals(Importance.NORMAL, reminder.importance);
        assertEquals(1577899800, reminder.epochSeconds);
        assertEquals(Instant.ofEpochSecond(1577899800), reminder.date);
    }

    @Test
    public fun testUpdateReminderImportance() {
        //Test-reminder for normal priority at  2020-01-01 17:30:00 GMT/UTC
        val reminder : StudyReminder = this.getStudyReminder(0, "Test", 1, 1577899800);

        assertEquals(1, reminder.importanceInt);
        assertEquals(Importance.NORMAL, reminder.importance);

        reminder.importance = Importance.HIGH;

        assertEquals(2, reminder.importanceInt);
        assertEquals(Importance.HIGH, reminder.importance);

        reminder.importanceInt = 0;

        assertEquals(0, reminder.importanceInt);
        assertEquals(Importance.LOW, reminder.importance);
    }

    @Test
    public fun testUpdateReminderDate() {
        //Test-reminder for normal priority at  2020-01-01 17:30:00 GMT/UTC
        val reminder : StudyReminder = this.getStudyReminder(0, "Test", 1, 1577899800);

        assertEquals(1577899800, reminder.epochSeconds);
        assertEquals(Instant.ofEpochSecond(1577899800), reminder.date);

        reminder.epochSeconds = 1577900700; //2020-01-01 17:45:00 GMT/UTC

        assertEquals(1577900700, reminder.epochSeconds);
        assertEquals(Instant.ofEpochSecond(1577900700), reminder.date);

        reminder.date = Instant.ofEpochSecond(1577898900); //2020-01-01 17:15:00 GMT/UTC

        assertEquals(1577898900, reminder.epochSeconds);
        assertEquals(Instant.ofEpochSecond(1577898900), reminder.date);
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