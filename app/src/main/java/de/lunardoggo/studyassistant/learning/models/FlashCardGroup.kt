package de.lunardoggo.studyassistant.learning.models

class FlashCardGroup {
    public var id : Int = -1
        get set;
    public var name : String = ""
        get set;
    public var color : Int = 0
        get set;
    public var subjectName : String = ""
        get set;
    public var flashCards : ArrayList<FlashCard> = ArrayList()
        get set;

    operator fun get(index : Int) : FlashCard {
        return this.flashCards[index];
    }
}