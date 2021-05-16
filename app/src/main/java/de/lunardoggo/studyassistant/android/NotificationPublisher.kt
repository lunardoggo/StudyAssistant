package de.lunardoggo.studyassistant.android

import android.app.*
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.lunardoggo.studyassistant.MainActivity
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import de.lunardoggo.studyassistant.learning.sessions.SessionStatus
import de.lunardoggo.studyassistant.ui.main.RequestCodes


class NotificationPublisher {
    companion object {
        private const val NotificationChannelId = "study_assistant_notifications";
        private const val SessionProgressNotificationId = 1;
        private const val StudyReminderNotificationId = 2;

        public fun showToastShortDuration(context: Context, text: String, duration: Int) {
            Toast.makeText(context, text, duration).show();
        }

        public fun showStudyReminderNotification(context: Context, reminder : StudyReminder) {
            val content = context.getString(R.string.alert_study_reminder_due);
            val icon = R.drawable.ic_launcher_foreground;
            val title = reminder.title;

            val builder = getNotificationBuilder(context, NotificationChannelId, icon, title, content).apply {
                this.priority = NotificationCompat.PRIORITY_DEFAULT;
            };

            this.sendReminderNotification(context, builder);
        }

        public fun sendSessionProgressNotification(context: Context, status: SessionStatus, currentInterval: Int, totalIntervals: Int) {
            val content = getSessionStatusContent(status, currentInterval, totalIntervals);
            val icon = R.drawable.ic_launcher_foreground;
            val title = getSessionStatusTile(status);

            val builder = getNotificationBuilder(
                context,
                NotificationChannelId,
                icon,
                title,
                content
            );
            builder.priority = NotificationCompat.PRIORITY_DEFAULT;

            this.sendProgressNotification(context, builder);
        }

        private fun sendProgressNotification(context: Context, builder: NotificationCompat.Builder) {
            sendNotification(context, builder.build(), SessionProgressNotificationId);
        }

        private fun sendReminderNotification(context: Context, builder: NotificationCompat.Builder) {
            sendNotification(context, builder.build(), StudyReminderNotificationId);
        }

        private fun sendNotification(context : Context, notification : Notification, channelId : Int) {
            getNotificationChannel(context);
            with(NotificationManagerCompat.from(context)) {
                notify(channelId, notification);
            }
        }

        private fun getSessionStatusContent(
            status: SessionStatus,
            currentInterval: Int,
            totalIntervals: Int
        ) : String {
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

        private fun getSessionStatusTile(status: SessionStatus) : String {
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

        private fun getNotificationBuilder(
            context: Context,
            channelId: String,
            icon: Int,
            title: String,
            content: String
        ) : NotificationCompat.Builder {
            val  builder = NotificationCompat.Builder(context, channelId);
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setSmallIcon(icon);

            return builder;
        }

        private fun getNotificationChannel(context: Context) {
            if(VersionFeatures.notificationsRequireCreatedChannel()) {
                val channel = NotificationChannel(
                    NotificationChannelId,
                    "Study Assistant Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notification channel for the Study Assistant app."
                }

                val service = (getNotificationManager(context));
                service.createNotificationChannel(channel);
            }
        }


        private fun getNotificationManager(context: Context) : NotificationManager {
            return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
        }
    }
}