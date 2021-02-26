package de.lunardoggo.studyassistant.learning.data.converters

import org.json.JSONObject

interface JsonConverter<T> {

    public fun convertBack(value : T) : JSONObject;
    public fun convert(json : JSONObject) : T;
}