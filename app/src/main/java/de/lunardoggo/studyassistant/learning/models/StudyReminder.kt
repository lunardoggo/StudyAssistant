package de.lunardoggo.studyassistant.learning.models

import java.time.Instant

class StudyReminder {
    public var id : Int = StudyReminder.DefaultId
        get set;
    public var title : String = ""
        get set;
    public var description : String = ""
        get set;
    public var importance : Importance = Importance.NORMAL
        get set;
    public var date : Instant = Instant.MIN
        get set;

    public var epochSeconds : Long
        get() { return this.date.epochSecond; }
        set(value) { this.date = Instant.ofEpochSecond(value); }
    public var importanceInt : Int
        get() { return this.importance.intValue; }
        set(value) { this.importance = Importance.valueOf(value); }

    companion object {
        public val DefaultId = -1;
    }
}