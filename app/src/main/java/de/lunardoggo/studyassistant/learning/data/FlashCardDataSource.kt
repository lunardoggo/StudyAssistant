package de.lunardoggo.studyassistant.learning.data

import de.lunardoggo.studyassistant.learning.data.converters.FlashCardGroupJsonConverter
import de.lunardoggo.studyassistant.learning.io.AppFileManager
import de.lunardoggo.studyassistant.learning.models.FlashCard
import de.lunardoggo.studyassistant.learning.models.FlashCardGroup
import de.lunardoggo.studyassistant.learning.models.JsonDataFile
import org.json.JSONArray
import org.json.JSONObject

class FlashCardDataSource(fileManager : AppFileManager) {

    private val groups = ArrayList<FlashCardGroup>();
    private val fileManager = fileManager;

    public fun getFlashCardGroups() : List<FlashCardGroup> {
        val file = this.loadJsonData();
        return this.getFlashCardGroups(file.getModels());
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
    }
}