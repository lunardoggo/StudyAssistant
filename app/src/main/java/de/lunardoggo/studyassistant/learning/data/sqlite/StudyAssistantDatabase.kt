package de.lunardoggo.studyassistant.learning.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class StudyAssistantDatabase(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(StudyAssistantContract.SQL_CREATE_FLASHCARD_GROUPS);
        database.execSQL(StudyAssistantContract.SQL_CREATE_FLASHCARDS);
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO implement!
    }

    companion object {
        public const val DATABASE_NAME = "study_assistant";
        public const val DATABASE_VERSION = 1;
    }
}