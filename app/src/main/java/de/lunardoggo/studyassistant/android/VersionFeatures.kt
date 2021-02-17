package de.lunardoggo.studyassistant.android

import android.os.Build

class VersionFeatures {
    companion object {
        public fun notificationsRequireCreatedChannel() : Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
        }
    }
}