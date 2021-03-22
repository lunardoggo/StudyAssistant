package de.lunardoggo.studyassistant.learning.data.sqlite

import android.provider.BaseColumns

object StudyAssistantContract {

    public const val SQL_CREATE_FLASHCARD_GROUPS = "CREATE TABLE ${FlashCardGroupEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FlashCardGroupEntry.COLUMN_NAME_NAME} TEXT NOT NULL," +
            "${FlashCardGroupEntry.COLUMN_NAME_SUBJECT} TEXT NOT NULL," +
            "${FlashCardGroupEntry.COLUMN_NAME_COLOR} INTEGER NOT NULL);";

    public const val SQL_CREATE_FLASHCARDS = "CREATE TABLE ${FlashCardEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FlashCardEntry.COLUMN_NAME_TITLE} TEXT NOT NULL," +
            "${FlashCardEntry.COLUMN_NAME_CONTENT} TEXT NOT NULL," +
            "${FlashCardEntry.COLUMN_NAME_TYPE} INTEGER NOT NULL," +
            "${FlashCardEntry.COLUMN_NAME_GROUP_ID} INTEGER NOT NULL," +
            "FOREIGN KEY (${FlashCardEntry.COLUMN_NAME_GROUP_ID}) REFERENCES ${FlashCardGroupEntry.TABLE_NAME} (${BaseColumns._ID}));";

    public const val SQL_DELETE_FLASHCARD_GROUPS = "DROP TABLE IF EXISTS ${FlashCardGroupEntry.TABLE_NAME};";
    public const val SQL_DELETE_FLASHCARDS = "DROP TABLE IF EXISTS ${FlashCardEntry.TABLE_NAME};";

    object FlashCardEntry : BaseColumns {
        const val TABLE_NAME = "flashcards";
        const val COLUMN_NAME_TITLE = "title";
        const val COLUMN_NAME_CONTENT = "content";
        const val COLUMN_NAME_TYPE = "type";
        const val COLUMN_NAME_GROUP_ID = "fk_group_id";
    }

    object FlashCardGroupEntry : BaseColumns {
        const val TABLE_NAME = "flashcard_groups";
        const val COLUMN_NAME_NAME = "name";
        const val COLUMN_NAME_COLOR = "color";
        const val COLUMN_NAME_SUBJECT = "subject_name";
    }
}

/*public fun getFlashCardGroups() : List<FlashCardGroup> {
    if(this.groups.size == 0) {
        val file = this.loadJsonData();
        this.groups.addAll(this.getFlashCardGroups(file.getModels()));
    }
    return this.groups;
}

private fun getFlashCardGroups(jsonObjects : List<JSONObject>) : List<FlashCardGroup> {
    if(this.groups.size == 0) {
        for(i in 0..jsonObjects.size) {
            this.groups.add(FlashCardGroupJsonConverter.instance.convert(jsonObjects[i]));
        }
    }
    return this.groups;
}

private fun loadJsonData() : JsonDataFile {
    return JsonDataFile(this.fileManager.readFileContent("flashcards.json"));
}


public fun addFlashCardGroup(group : FlashCardGroup) {
    this.groups.add(group);
}*/

