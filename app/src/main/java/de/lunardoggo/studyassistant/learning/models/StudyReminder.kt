package de.lunardoggo.studyassistant.learning.models

import java.time.Instant

class StudyReminder {
    public var id : Int = -1
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
}

enum class Importance(intValue : Int) {
    HIGH(2),
    NORMAL(1),
    LOW(0);

    public val intValue : Int = intValue;

    companion object {
        public fun valueOf(intValue: Int): Importance {
            for (importance in Importance.values()) {
                if (importance.intValue == intValue) {
                    return importance;
                }
            }
            throw NotImplementedError("Importance of Int value \"${intValue}\" is not implemented.");
        }
    }
}