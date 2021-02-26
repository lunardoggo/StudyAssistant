package de.lunardoggo.studyassistant.learning.models

import org.json.JSONObject
import java.lang.IllegalArgumentException

class JsonDataFile(jsonContent : String) {

    private lateinit var json : JSONObject;
    private val jsonContent = jsonContent;

    init {
        if(this.jsonContent != null && this.jsonContent.length > 2) {
            this.json = JSONObject(jsonContent);
        }
    }

    public fun getFileVersion() : Version {
        if(this.json != null) {
            val version = Version(this.json.getString("version"));
            if(version != null && !version.isValid()) {
                return version;
            }
        }
        throw IllegalArgumentException("Couldn't read the version number from file");
    }

    public fun getModels() : List<JSONObject> {
        val output = ArrayList<JSONObject>();
        if(this.json != null) {
            val models = this.json.getJSONArray("models");
            for(i in 0..models.length()) {
                output.add(models.getJSONObject(i));
            }
        }
        return output;
    }
}