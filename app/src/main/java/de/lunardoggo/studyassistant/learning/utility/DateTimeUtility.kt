package de.lunardoggo.studyassistant.learning.utility

import android.content.Context
import android.text.format.DateFormat
import java.time.Instant
import java.util.*

class DateTimeUtility {
    companion object {
        public fun getFormattedReminderDateTime(context : Context, instant : Instant) : String{
            val dateFormat = DateFormat.getDateFormat(context);
            val timeFormat = DateFormat.getTimeFormat(context);
            val date = Date.from(instant);
            return "${dateFormat.format(date)} ${timeFormat.format(date)}";
        }
    }
}