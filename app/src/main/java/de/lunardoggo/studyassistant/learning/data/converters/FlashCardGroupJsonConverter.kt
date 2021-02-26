package de.lunardoggo.studyassistant.learning.data.converters

import de.lunardoggo.studyassistant.learning.models.FlashCard
import de.lunardoggo.studyassistant.learning.models.FlashCardGroup
import org.json.JSONArray
import org.json.JSONObject

class FlashCardGroupJsonConverter : JsonConverter<FlashCardGroup> {

    companion object {
        public val instance = FlashCardGroupJsonConverter()
            get;
    }

    private constructor() {}

    override fun convertBack(group: FlashCardGroup): JSONObject {
        val json = JSONObject();
        with(json) {
            this.put("flashcards", getFlashCardObjects(group.flashCards));
            this.put("subject_name", group.subjectName);
            this.put("color", group.colorHex);
            this.put("name", group.name);
            return this;
        }
    }

    private fun getFlashCardObjects(cards : List<FlashCard>) : JSONArray {
        val array = JSONArray();
        for(card in cards) {
            array.put(FlashCardJsonConverter.instance.convertBack(card));
        }
        return array;
    }


    override fun convert(json: JSONObject): FlashCardGroup {
        val group = FlashCardGroup();
        with(group) {
            this.flashCards.addAll(getFlashCards(json.getJSONArray("flashcards")));
            this.subjectName = json.getString("subject_name");
            this.colorHex = json.getString("color");
            this.name = json.getString("name");
            return this;
        }
    }

    private fun getFlashCards(array : JSONArray) : List<FlashCard> {
        val output = ArrayList<FlashCard>();
        for(i in 0..array.length()) {
            output.add(FlashCardJsonConverter.instance.convert(array.getJSONObject(i)));
        }
        return output;
    }
}