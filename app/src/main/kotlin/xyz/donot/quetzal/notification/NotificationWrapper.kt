package xyz.donot.quetzal.notification

import android.content.Context
import android.net.Uri
import android.preference.PreferenceManager
import br.com.goncalves.pugnotification.notification.PugNotification
import twitter4j.Status
import xyz.donot.quetzal.R

class NotificationWrapper(val context: Context){
   val notification= PugNotification.with(context).load()
    val res = context.resources
    val ringtone = PreferenceManager.getDefaultSharedPreferences(context).getString("notifications_ringtone", "")
    fun replyNotification(status: Status): Unit {
        val text = res.getString(R.string.new_mention_notification_placeholder_text_template,status.text)
     notification.apply {
               title("返信")
               message(status.text)
               bigTextStyle(text)
               smallIcon(R.drawable.ic_reply_grey_400_24dp)
               largeIcon(R.drawable.ic_launcher)
               sound(Uri.parse(ringtone))
       }
    notification.simple().build()
    }
    fun sendingNotification(identifier: Int) {
        notification.apply {
            title("送信中…")
            identifier(identifier)
            smallIcon(R.drawable.ic_launcher)
            autoCancel(false)
        }
        notification.progress().value(0,100,true).build()
    }


}