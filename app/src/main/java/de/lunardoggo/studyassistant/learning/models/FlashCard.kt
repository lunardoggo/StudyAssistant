package de.lunardoggo.studyassistant.learning.models

class FlashCard {
    public var title : String = ""
        get set;
    public var content : String = ""
        get set;
    public var type : FlashCardType = FlashCardType.PLAINTEXT
        get set;

    public enum class FlashCardType {
        PLAINTEXT
    }
}