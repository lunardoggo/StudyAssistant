package de.lunardoggo.studyassistant.learning.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudyAssistantDatabase(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(StudyAssistantContract.SQL_CREATE_STUDY_REMINDERS);
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun onDowngrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        public const val DATABASE_NAME = "StudyAssistant.db";
        public const val DATABASE_VERSION = 1;
    }
}