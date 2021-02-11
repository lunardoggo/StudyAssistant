package de.lunardoggo.studyassistant.config

class AppConfiguration {
    public val studySessions = StudySessionConfiguration();

}

class StudySessionConfiguration {
    public var learningTimeMinutes = 25
        get set;

    public var breakTimeMinutes = 5
        get set;

    public var intervalCount = 3
        get set;
}