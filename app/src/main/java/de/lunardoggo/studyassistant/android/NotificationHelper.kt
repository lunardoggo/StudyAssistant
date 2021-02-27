package de.lunardoggo.studyassistant.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.sessions.SessionStatus

class NotificationHelper {
    companion object {
        private const val NotificationChannelId = "studyassistant_notifications";
        private const val SessionProgressNotificationId = 1;

        public fun showToastShortDuration(context : Context, text : String, duration : Int) {
            Toast.makeText(context, text, duration).show();
        }

        public fun sendSessionProgressNotification(context : Context, status : SessionStatus, currentInterval : Int, totalIntervals : Int) {
            val content = getSessionStatusContent(status, currentInterval, totalIntervals);
            val icon = R.drawable.ic_launcher_foreground;
            val title = getSessionStatusTile(status);

            val builder = getNotificationBuilder(context, NotificationChannelId, icon, title, content);
            builder.priority = NotificationCompat.PRIORITY_DEFAULT;

            sendNotification(context, builder);
        }

        private fun sendNotification(context : Context, builder : NotificationCompat.Builder) {
            createNotificationChannel(context);
            with(NotificationManagerCompat.from(context)) {
                notify(SessionProgressNotificationId, builder.build());
            }
        }

        private fun getSessionStatusContent(status : SessionStatus, currentInterval : Int, totalIntervals : Int) : String {
            return when(status) {
                SessionStatus.LEARNING -> {
                    "Current learning interval ${currentInterval + 1}/$totalIntervals";
                }
                SessionStatus.FINISHED -> {
                    "You finished $totalIntervals learning intervals";
                }
                SessionStatus.TAKING_BREAK -> {
                    "Take a break, next learning interval: ${currentInterval + 2} of $totalIntervals";
                }
                else -> {
                    "undefined"
                }
            }
        }

        private fun getSessionStatusTile(status : SessionStatus) : String {
            return when(status) {
                SessionStatus.LEARNING -> {
                    "Learning";
                }
                SessionStatus.FINISHED -> {
                    "Finished";
                }
                SessionStatus.TAKING_BREAK -> {
                    "Taking a break";
                }
                else -> {
                    "undefined"
                }
            }
        }

        private fun getNotificationBuilder(context : Context, channelId : String, icon : Int, title : String, content : String) : NotificationCompat.Builder {
            val  builder = NotificationCompat.Builder(context, channelId);
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setSmallIcon(icon);

            return builder;
        }

        private fun createNotificationChannel(context : Context) {
            if(VersionFeatures.notificationsRequireCreatedChannel()) {
                val channel = NotificationChannel(NotificationChannelId, "Study Assistant Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = "Notification channel for the Study Assistant app."
                }

                (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel);
            }
        }
    }
}