package de.lunardoggo.studyassistant.learning.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import de.lunardoggo.studyassistant.learning.data.sqlite.StudyAssistantContract.FlashCardGroupEntry
import de.lunardoggo.studyassistant.learning.data.sqlite.StudyAssistantContract.FlashCardEntry
import de.lunardoggo.studyassistant.learning.data.sqlite.StudyAssistantDatabase
import de.lunardoggo.studyassistant.learning.models.FlashCard
import de.lunardoggo.studyassistant.learning.models.FlashCardGroup

class StudyAssistantDataSource {

    private val database : StudyAssistantDatabase;

    public constructor(context : Context) {
        this.database = StudyAssistantDatabase(context);
    }

    public fun saveFlashCardGroup(group : FlashCardGroup) {
        if(this.doesFlashCardGroupExist(group.id)) {
            this.updateFlashCardGroup(group);
        } else {
          this.insertFlashCardGroup(group);
        }
    }

    private fun saveFlashCard(card : FlashCard, groupId : Int) {
        if(this.doesFlashCardExist(card.id)) {
            this.updateFlashCard(card, groupId);
        } else {
            this.insertFlashCard(card, groupId);
        }
    }

    public fun getFlashCardGroups() : List<FlashCardGroup> {
        val cursor = this.getFlashCardGroupsCursor();
        with(cursor) {
            val output = ArrayList<FlashCardGroup>();

            while(this.moveToNext()) {
                val group = FlashCardGroup();
                group.id = this.getInt(this.getColumnIndexOrThrow(BaseColumns._ID));
                group.name = this.getString(this.getColumnIndexOrThrow(FlashCardGroupEntry.COLUMN_NAME_NAME));
                group.subjectName = this.getString(this.getColumnIndexOrThrow(FlashCardGroupEntry.COLUMN_NAME_SUBJECT));
                group.color = this.getInt(this.getColumnIndexOrThrow(FlashCardGroupEntry.COLUMN_NAME_COLOR));
                group.flashCards = getFlashCards(group.id);

                output.add(group);
            }
            return output;
        }
    }

    private fun getFlashCards(groupId : Int) : ArrayList<FlashCard> {
        val cursor = this.getFlashCardsCursor(groupId);
        with(cursor) {
            val output = ArrayList<FlashCard>();
            if(this.moveToNext()) {
                val card = FlashCard();
                card.id = this.getInt(this.getColumnIndexOrThrow(BaseColumns._ID));
                card.title = this.getString(this.getColumnIndexOrThrow(FlashCardEntry.COLUMN_NAME_TITLE));
                card.content = this.getString(this.getColumnIndexOrThrow(FlashCardEntry.COLUMN_NAME_CONTENT));
                card.type = FlashCard.FlashCardType.getById(this.getInt(this.getColumnIndexOrThrow(FlashCardEntry.COLUMN_NAME_TYPE)));

                output.add(card);
            }
            return output;
        }
    }

/*---------------------------------------------*\
|           REGION: Updates                     |
\*---------------------------------------------*/

    private fun updateFlashCardGroup(group : FlashCardGroup) {
        val table = FlashCardGroupEntry.TABLE_NAME;
        val values = this.getFlashCardGroupContentValues(group.id, group);
        if(this.update(table, values, "${BaseColumns._ID} = ?", arrayOf(group.id.toString())) > 0) {
            for(card in group.flashCards) {
                this.saveFlashCard(card, group.id);
            }
        }
    }

    private fun updateFlashCard(card : FlashCard, groupId : Int) {
        val table = FlashCardEntry.TABLE_NAME;
        val values = this.getFlashCardContentValues(card.id, card,groupId );
        this.update(table, values, "${BaseColumns._ID} = ?", arrayOf(card.id.toString()));
    }

/*---------------------------------------------*\
|           REGION: Inserts                     |
\*---------------------------------------------*/

    private fun insertFlashCardGroup(group : FlashCardGroup) {
        val table = FlashCardGroupEntry.TABLE_NAME;
        group.id = this.getNextId(table);
        val values = this.getFlashCardGroupContentValues(group.id, group);

        if(this.insert(table, values) > 0) {
            for (card in group.flashCards) {
                this.saveFlashCard(card, group.id);
            }
        }
    }

    private fun insertFlashCard(card : FlashCard, groupId : Int) {
        val table = FlashCardEntry.TABLE_NAME;
        val values = this.getFlashCardContentValues(getNextId(table), card, groupId);
        this.insert(table, values);
    }

/*---------------------------------------------*\
|           REGION: Values                      |
\*---------------------------------------------*/

    private fun getFlashCardGroupContentValues(groupId : Int, group : FlashCardGroup) : ContentValues {
        val values = ContentValues();
        with(values) {
            this.put(BaseColumns._ID, groupId);
            this.put(FlashCardGroupEntry.COLUMN_NAME_NAME, group.name);
            this.put(FlashCardGroupEntry.COLUMN_NAME_SUBJECT, group.subjectName);
            this.put(FlashCardGroupEntry.COLUMN_NAME_COLOR, group.color);
            return this;
        }
    }

    private fun getFlashCardContentValues(cardId : Int, card : FlashCard, groupId : Int) : ContentValues {
        val values = ContentValues();
        with(values) {
            this.put(BaseColumns._ID, cardId);
            this.put(FlashCardEntry.COLUMN_NAME_TITLE, card.title);
            this.put(FlashCardEntry.COLUMN_NAME_CONTENT, card.content);
            this.put(FlashCardEntry.COLUMN_NAME_TYPE, card.type.typeId);
            this.put(FlashCardEntry.COLUMN_NAME_GROUP_ID, groupId);
            return this;
        }
    }

/*---------------------------------------------*\
|           REGION: Queries                     |
\*---------------------------------------------*/

    private fun doesFlashCardGroupExist(id : Int) : Boolean {
        val table = FlashCardGroupEntry.TABLE_NAME;
        val columns = arrayOf(BaseColumns._ID);
        val cursor = this.query(table, columns, "${BaseColumns._ID} = ?", arrayOf(id.toString()))
        return cursor.count > 0;
    }

    private fun doesFlashCardExist(id : Int) : Boolean {
        val table = FlashCardEntry.TABLE_NAME;
        val columns = arrayOf(BaseColumns._ID);
        val cursor = this.query(table, columns, "${BaseColumns._ID} = ?", arrayOf(id.toString()));
        return cursor.count > 0;
    }

    private fun getFlashCardGroupsCursor() : Cursor{
        val table = FlashCardGroupEntry.TABLE_NAME;
        val columns = arrayOf(BaseColumns._ID, FlashCardGroupEntry.COLUMN_NAME_NAME, FlashCardGroupEntry.COLUMN_NAME_SUBJECT, FlashCardGroupEntry.COLUMN_NAME_COLOR);
        return this.query(table, columns, "", arrayOf());
    }

    private fun getFlashCardsCursor(groupId : Int) : Cursor {
        val table = FlashCardEntry.TABLE_NAME;
        val columns = arrayOf(BaseColumns._ID, FlashCardEntry.COLUMN_NAME_TITLE, FlashCardEntry.COLUMN_NAME_CONTENT, FlashCardEntry.COLUMN_NAME_TYPE, FlashCardEntry.COLUMN_NAME_GROUP_ID);
        return this.query(table, columns, "${FlashCardEntry.COLUMN_NAME_GROUP_ID} = ?", arrayOf(groupId.toString()))
    }

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