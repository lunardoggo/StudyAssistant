package de.lunardoggo.studyassistant.learning.models

import java.lang.IllegalStateException

class Version {

    public var majorVersion : Int
        get;
    public var minorVersion : Int
        get;
    public var revision : Int
        get;

    constructor(majorVersion : Int, minorVersion : Int, revision : Int) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.revision = revision;
    }

    constructor(versionString: String) {
        val parts = versionString.split(".");
        if(parts.size == 3) {
            this.majorVersion = parts[0].toInt();
            this.minorVersion = parts[1].toInt();
            this.revision = parts[2].toInt();
        } else {
            throw IllegalStateException("Version string must have the format: \"0.0.0\"");
        }
    }

    public fun isNewer(other : Version) : Boolean {
        return     other.majorVersion < this.majorVersion
                && other.minorVersion < this.minorVersion
                && other.revision < this.revision;
    }

    public fun isValid() : Boolean {
        return this.majorVersion > 0 || this.minorVersion > 0 || this.revision > 0;
    }
}