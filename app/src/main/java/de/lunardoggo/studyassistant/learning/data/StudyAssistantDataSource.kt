package de.lunardoggo.studyassistant.learning.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import de.lunardoggo.studyassistant.learning.data.sqlite.StudyAssistantContract.StudyReminderEntry
import de.lunardoggo.studyassistant.learning.data.sqlite.StudyAssistantDatabase
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import java.time.LocalDate
import java.time.ZoneId

class StudyAssistantDataSource {

    private val database : StudyAssistantDatabase;

    public constructor(context : Context) {
        this.database = StudyAssistantDatabase(context);
    }

/*---------------------------------------------*\
|           REGION: Updates                     |
\*---------------------------------------------*/

    public fun updateStudyReminder(reminder : StudyReminder) {
        val values = this.getStudyReminderContentValues(reminder);
        val where = "${BaseColumns._ID} = ?";
        val whereArgs = arrayOf(reminder.id.toString());

        this.update(StudyReminderEntry.TABLE_NAME, values, where, whereArgs);
    }

/*---------------------------------------------*\
|           REGION: Inserts                     |
\*---------------------------------------------*/

    public fun insertStudyReminder(reminder : StudyReminder) {
        val values = this.getStudyReminderContentValues(reminder);
        values.put(BaseColumns._ID, this.getNextId(StudyReminderEntry.TABLE_NAME));

        this.insert(StudyReminderEntry.TABLE_NAME, values);
    }

/*---------------------------------------------*\
|           REGION: Deletes                     |
\*---------------------------------------------*/

    public fun deleteStudyReminder(reminder : StudyReminder) {
        val where = "${BaseColumns._ID} = ?";
        val whereArgs = arrayOf(reminder.id.toString());

        this.delete(StudyReminderEntry.TABLE_NAME, where, whereArgs);
    }

/*---------------------------------------------*\
|           REGION: Values                      |
\*---------------------------------------------*/

    private fun getStudyReminderContentValues(reminder : StudyReminder) : ContentValues {
        val values = ContentValues();
        values.put(StudyReminderEntry.COLUMN_NAME_TITLE, reminder.title);
        values.put(StudyReminderEntry.COLUMN_NAME_DESCRIPTION, reminder.description);
        values.put(StudyReminderEntry.COLUMN_NAME_IMPORTANCE, reminder.importanceInt);
        values.put(StudyReminderEntry.COLUMN_NAME_EPOCH_SECONDS, reminder.epochSeconds);
        return values;
    }

/*---------------------------------------------*\
|           REGION: Queries                     |
\*---------------------------------------------*/

    public fun getStudyReminders() : List<StudyReminder> {
        val columns = arrayOf(BaseColumns._ID, StudyReminderEntry.COLUMN_NAME_EPOCH_SECONDS, StudyReminderEntry.COLUMN_NAME_DESCRIPTION, StudyReminderEntry.COLUMN_NAME_IMPORTANCE, StudyReminderEntry.COLUMN_NAME_TITLE);
        val currentDateSeconds = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond;
        val where = "${StudyReminderEntry.COLUMN_NAME_EPOCH_SECONDS} >= ?";
        val cursor = this.query(StudyReminderEntry.TABLE_NAME, columns, where, arrayOf(currentDateSeconds.toString()), "${StudyReminderEntry.COLUMN_NAME_EPOCH_SECONDS} ASC");
        val output = this.mapStudyReminders(cursor);
        cursor.close();
        return output;
    }

    private fun getNextId(table : String) : Int {
        val columns = arrayOf("MAX(${BaseColumns._ID}) AS ${BaseColumns._ID}");
        val cursor = this.query(table, columns, "", arrayOf(), "");
        if(cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)) + 1;
        }
        return 0;
    }

/*---------------------------------------------*\
 |          REGION: Mappers                     |
\*---------------------------------------------*/

    private fun mapStudyReminders(cursor : Cursor) : List<StudyReminder> {
        val output = ArrayList<StudyReminder>();
        cursor.apply {
            while(this.moveToNext()) {
                val reminder = StudyReminder();
                reminder.id = this.getInt(this.getColumnIndexOrThrow(BaseColumns._ID));
                reminder.epochSeconds = this.getLong(this.getColumnIndexOrThrow(StudyReminderEntry.COLUMN_NAME_EPOCH_SECONDS));
                reminder.description = this.getString(this.getColumnIndexOrThrow(StudyReminderEntry.COLUMN_NAME_DESCRIPTION));
                reminder.importanceInt = this.getInt(this.getColumnIndexOrThrow(StudyReminderEntry.COLUMN_NAME_IMPORTANCE));
                reminder.title = this.getString(this.getColumnIndexOrThrow(StudyReminderEntry.COLUMN_NAME_TITLE));
                output.add(reminder);
            }
        }
        return output;
    }

/*---------------------------------------------*\
|          REGION: SQL Basics                   |
\*---------------------------------------------*/

    private fun query(table : String, columns : Array<String>, whereColumns : String, whereArgs : Array<String>, orderBy : String) : Cursor {
        synchronized(this.database) {
            return this.database.readableDatabase!!.query(table, columns, whereColumns, whereArgs, null, null, orderBy);
        }
    }

    private fun insert(table : String, values : ContentValues) : Long {
        synchronized(this.database) {
            return this.database.writableDatabase!!.insert(table, null, values);
        }
    }

    private fun update(table : String, values : ContentValues, where : String, whereArgs : Array<String>) : Int {
        synchronized(this.database) {
            return this.database.writableDatabase.update(table, values, where, whereArgs);
        }
    }

    private fun delete(table : String, where : String, whereArgs: Array<String>) : Int {
        synchronized(this.database) {
            return this.database.writableDatabase.delete(table, where, whereArgs);
        }
    }
}