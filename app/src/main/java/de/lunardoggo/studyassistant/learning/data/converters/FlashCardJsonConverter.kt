package de.lunardoggo.studyassistant.learning.data.converters

import de.lunardoggo.studyassistant.learning.models.FlashCard
import org.json.JSONObject

class FlashCardJsonConverter : JsonConverter<FlashCard> {

    companion object {
        public val instance = FlashCardJsonConverter()
            get;
    }

    private constructor() {}

    override fun convertBack(card: FlashCard): JSONObject {
        val json = JSONObject();
        with(json) {
            this.put("type", card.type.toString().toUpperCase());
            this.put("content", card.content);
            this.put("title", card.title);
            return this;
        }
    }

    override fun convert(json: JSONObject): FlashCard {
        val card = FlashCard();
        with(card) {
            this.type = FlashCard.FlashCardType.valueOf(json.getString("type").toUpperCase());
            this.content = json.getString("content");
            this.title = json.getString("title");
            return this;
        }
    }
}