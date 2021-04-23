package de.lunardoggo.studyassistant.learning.data.sqlite

import android.provider.BaseColumns

object StudyAssistantContract {

    public const val SQL_CREATE_STUDY_REMINDERS = "CREATE TABLE ${StudyReminderEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${StudyReminderEntry.COLUMN_NAME_TITLE} TEXT NOT NULL," +
            "${StudyReminderEntry.COLUMN_NAME_EPOCH_SECONDS} UNSIGNED BIG INT NOT NULL," +
            "${StudyReminderEntry.COLUMN_NAME_DESCRIPTION} TEXT NOT NULL," +
            "${StudyReminderEntry.COLUMN_NAME_IMPORTANCE} INTEGER NOT NULL);";

    object StudyReminderEntry : BaseColumns {
        const val TABLE_NAME = "study_reminders";
        const val COLUMN_NAME_EPOCH_SECONDS = "epoch_seconds";
        const val COLUMN_NAME_DESCRIPTION = "description";
        const val COLUMN_NAME_IMPORTANCE = "importance";
        const val COLUMN_NAME_TITLE = "title";
    }
}