package xyz.donot.quetzal.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import xyz.donot.quetzal.R


object NewMentionNotification {
    private val NOTIFICATION_TAG = "NewMention"
    fun notify(context: Context,
               txtString: String, number: Int) {
        val res = context.resources
        val picture = BitmapFactory.decodeResource(res, R.drawable.ic_launcher)
        val title = "返信"
        val text = res.getString(R.string.new_mention_notification_placeholder_text_template, txtString)
        val ringtone = PreferenceManager.getDefaultSharedPreferences(context).getString("notifications_ringtone", "")
    val builder=  NotificationCompat.Builder(context).apply {
        setSound(Uri.parse(ringtone))
        setSmallIcon(R.drawable.ic_stat_new_mention)
        setContentText(text)
        setPriority(NotificationCompat.PRIORITY_DEFAULT)
        setLargeIcon(picture)
        setTicker(txtString)
        setNumber(number)
        setContentTitle(title)
        setStyle(NotificationCompat.BigTextStyle().bigText(text).setBigContentTitle(title))
        setAutoCancel(true)
      }
          //   .addAction(
        //  R.drawable.ic_action_stat_reply,
        //   res.getString(R.string.action_reply),
        //   PendingIntent.getActivity(context, 0,new Intent(context, MainActivity.class), 0))

        notify(context, builder.build())
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context, notification: Notification) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification)
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification)
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun cancel(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0)
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode())
        }
    }
}
