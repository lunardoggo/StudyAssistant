package de.lunardoggo.studyassistant.learning.models

enum class Importance(intValue : Int) {
    HIGH(2),
    NORMAL(1),
    LOW(0);

    public val intValue : Int = intValue;

    companion object {

        public fun valueOf(intValue: Int) : Importance {
            for (importance in Importance.values()) {
                if (importance.intValue == intValue) {
                    return importance;
                }
            }
            throw NotImplementedError("Importance of Int value \"${intValue}\" is not implemented.");
        }
    }
}