package de.lunardoggo.studyassistant.learning.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import de.lunardoggo.studyassistant.learning.data.sqlite.StudyAssistantContract.FlashCardGroupEntry
import de.lunardoggo.studyassistant.learning.data.sqlite.StudyAssistantContract.FlashCardEntry
import de.lunardoggo.studyassistant.learning.data.sqlite.StudyAssistantDatabase

class StudyAssistantDataSource {

    private val database : StudyAssistantDatabase;

    public constructor(context : Context) {
        this.database = StudyAssistantDatabase(context);
    }

/*---------------------------------------------*\
|           REGION: Updates                     |
\*---------------------------------------------*/


/*---------------------------------------------*\
|           REGION: Inserts                     |
\*---------------------------------------------*/


/*---------------------------------------------*\
|           REGION: Values                      |
\*---------------------------------------------*/


/*---------------------------------------------*\
|           REGION: Queries                     |
\*---------------------------------------------*/


    private fun getNextId(table : String) : Int {
        val columns = arrayOf("MAX(${BaseColumns._ID}) AS ${BaseColumns._ID}");
        val cursor = this.query(table, columns, "", arrayOf());
        if(cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)) + 1;
        }
        return 0;
    }

/*---------------------------------------------*\
|          REGION: SQL Basics                   |
\*---------------------------------------------*/

    private fun query(table : String, columns : Array<String>, whereColumns : String, whereArgs : Array<String>) : Cursor {
        synchronized(this.database) {
            return this.database.readableDatabase!!.query(table, columns, whereColumns, whereArgs, null, null, null);
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