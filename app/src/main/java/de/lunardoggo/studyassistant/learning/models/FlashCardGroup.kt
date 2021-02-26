package de.lunardoggo.studyassistant.learning.models

class FlashCardGroup {
    public var id : Guid = Guid.empty()
        get set;
    public var name : String = ""
        get set;
    public var colorHex : String = ""
        get set;
    public var subjectName : String = ""
        get set;
    public var flashCards : ArrayList<FlashCard> = ArrayList<FlashCard>()
        get;

    operator fun get(index : Int) : FlashCard {
        return this.flashCards[index];
    }
}