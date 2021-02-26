package de.lunardoggo.studyassistant.learning.models

import java.lang.StringBuilder
import java.security.MessageDigest
import kotlin.random.Random

class Guid(bytes : Array<Byte>) {

    companion object {
        public fun empty() : Guid {
            return Guid(arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        }

        public fun new() : Guid {
            val sha = MessageDigest.getInstance("MD5");
            return Guid(sha.digest(Random.nextInt().toString().toByteArray()).toTypedArray());
        }
    }

    private val bytes : Array<Byte> = bytes;

    public fun equals(other : Guid) : Boolean {
        for(i in 0..this.bytes.size) {
            if(this.bytes[i] != other.bytes[i]) {
                return false;
            }
        }
        return true;
    }

    override fun toString(): String {
        val builder = StringBuilder(20);
        for(byte in bytes) {
            builder.append(String.format("%02X", byte));
        }
        builder.insert(8, "-");
        builder.insert(13, "-");
        builder.insert(18, "-");
        builder.insert(23, "-");
        return builder.toString();
    }
}