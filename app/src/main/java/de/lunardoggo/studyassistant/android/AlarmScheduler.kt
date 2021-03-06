package de.lunardoggo.studyassistant.android

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import de.lunardoggo.studyassistant.MainActivity
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import de.lunardoggo.studyassistant.learning.utility.StudyReminderIntentConverter
import de.lunardoggo.studyassistant.ui.main.RequestCodes

class AlarmScheduler : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null && intent != null && intent.hasExtra(AlarmScheduler.IntentActionField)) {
            when (intent.getStringExtra(AlarmScheduler.IntentActionField)) {
                "show_reminder_notification" -> { //TODO: Enum or constants
                    val reminder = StudyReminderIntentConverter.instance.convertFrom(intent);
                    NotificationPublisher.showStudyReminderNotification(context, reminder);
                    MainActivity.instance.scheduleNextReminderNotification();
                };
            }
        }
    }

    companion object {

        private const val IntentActionField = "pending_action";

        public fun scheduleStudyReminder(context : Context, reminder : StudyReminder, utcMillis : Long) {
            val baseIntent = Intent(context, AlarmScheduler::class.java)
            val pendingIntent = StudyReminderIntentConverter.instance.convertTo(reminder, baseIntent).apply {
                this.putExtra(AlarmScheduler.IntentActionField, "show_reminder_notification"); //TODO: Enum or constants
            }.let { _intent -> PendingIntent.getBroadcast(context, RequestCodes.REQUEST_SCHEDULE_REMINDER, _intent, PendingIntent.FLAG_ONE_SHOT) };
            this.scheduleOnceRTC(context, utcMillis, pendingIntent);
        }

        private fun scheduleOnceRTC(context : Context, utcMillis : Long, intent : PendingIntent) {
            this.getAlarmManager(context).set(AlarmManager.RTC_WAKEUP, utcMillis, intent);
        }

        private fun getAlarmManager(context : Context) : AlarmManager {
            return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        }
    }
}