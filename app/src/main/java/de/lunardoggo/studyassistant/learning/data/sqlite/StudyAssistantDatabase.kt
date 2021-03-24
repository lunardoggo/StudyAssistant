package de.lunardoggo.studyassistant.learning.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import kotlinx.coroutines.awaitAll

class StudyAssistantDatabase(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(StudyAssistantContract.SQL_CREATE_FLASHCARD_GROUPS);
        database.execSQL(StudyAssistantContract.SQL_CREATE_FLASHCARDS);
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO implement!
        val i = 0;
    }

    companion object {
        public const val DATABASE_NAME = "StudyAssistant.db";
        public const val DATABASE_VERSION = 1;
    }
}