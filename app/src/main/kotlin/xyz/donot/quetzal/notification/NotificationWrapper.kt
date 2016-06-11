package xyz.donot.quetzal.notification

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.preference.PreferenceManager
import android.support.v4.app.NotificationManagerCompat
import twitter4j.Status
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.extrautils.newIntent
import xyz.donot.quetzal.util.extrautils.newNotification
import xyz.donot.quetzal.view.activity.NotificationActivity

class NotificationWrapper(val context: Context){
    val ringtone = PreferenceManager.getDefaultSharedPreferences(context).getString("notifications_ringtone", "")

    fun replyReceived(status: Status) {
        val intent = context.newIntent<NotificationActivity>()
        val contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        val notification = context.newNotification {
            setSmallIcon(R.drawable.ic_reply_grey_400_24dp)
            setContentTitle("返信")
            setContentText(status.text)
            setWhen(status.createdAt.time)
            setTicker("${status.user.name}からの返信")
            setAutoCancel(true)
            setSound(Uri.parse(ringtone))
            setContentIntent(contentIntent)
        }
        val manager = NotificationManagerCompat.from(context);
        manager.notify(0, notification);
    }

    val res = context.resources
    fun sendingNotification(identifier: Int) {
        val notification = context.newNotification {
            setSmallIcon(R.drawable.ic_launcher)
            setContentTitle("送信中…")
            setTicker("送信中…")
            setAutoCancel(false)
            setProgress(100, 100, true)
        }
        val manager = NotificationManagerCompat.from(context);
        manager.notify(identifier, notification);
    }


}