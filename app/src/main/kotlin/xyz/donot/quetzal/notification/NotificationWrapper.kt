package xyz.donot.quetzal.notification

import android.content.Context
import android.net.Uri
import android.preference.PreferenceManager
import br.com.goncalves.pugnotification.notification.PugNotification
import twitter4j.Status
import xyz.donot.quetzal.R
import xyz.donot.quetzal.view.activity.NotificationActivity

class NotificationWrapper(val context: Context){
   val notification=PugNotification.with(context).load()
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
         click (NotificationActivity::class.java)

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
    fun sendingFailureNotification() {
        notification.apply {
            title("送信に失敗しました")
            smallIcon(R.drawable.ic_launcher)
            autoCancel(true)
        }
    }


}