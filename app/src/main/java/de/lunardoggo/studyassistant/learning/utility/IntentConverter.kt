package de.lunardoggo.studyassistant.learning.utility

import android.content.Intent

interface IntentConverter<T> {

    public fun convertFrom(data : Intent) : T;

    public fun convertTo(value : T, baseIntent : Intent) : Intent;
    public fun convertTo(value : T) : Intent;
}