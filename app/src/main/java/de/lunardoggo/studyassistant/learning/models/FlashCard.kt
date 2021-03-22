package de.lunardoggo.studyassistant.learning.models

import java.lang.IllegalArgumentException

class FlashCard {
    public var id : Int = -1
        get set;
    public var title : String = ""
        get set;
    public var content : String = ""
        get set;
    public var type : FlashCardType = FlashCardType.PLAINTEXT
        get set;


    public enum class FlashCardType(val typeId : Int) {
        PLAINTEXT(0);

        companion object {
            public fun getById(id: Int) : FlashCardType {
                for(type in values()) {
                    if(type.typeId == id) {
                        return type;
                    }
                }
                throw IllegalArgumentException("\"${id}\" doesn't refer to a valid FlashCardType.");
            }
        }
    }
}