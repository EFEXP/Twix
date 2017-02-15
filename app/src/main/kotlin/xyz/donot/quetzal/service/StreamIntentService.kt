package xyz.donot.quetzal.service

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import twitter4j.*
import xyz.donot.quetzal.twitter.StreamCreateUtil
import xyz.donot.quetzal.util.getTwitterInstance


class StreamService : IntentService("StreamService") {
    val twitter= getTwitterInstance()

    override fun onHandleIntent(intent: Intent?) {
        val stream = TwitterStreamFactory().getInstance(twitter.authorization)
        StreamCreateUtil.addStatusListener(stream,MyStreamAdapter())
        stream.user()
        val mNotification = NotificationCompat.Builder(this)
             //   .setSmallIcon(R.drawable.tw__ic_logo_default)
                .setContentTitle("Streaming")
                .setAutoCancel(false)
                .setContentText("ストリームは正常に稼働中です。")
                .build()
        // mNotification.flags= Notification.FLAG_NO_CLEAR
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(0, mNotification )
    }

    override fun onDestroy() {
        super.onDestroy()

    }
    inner class MyStreamAdapter: UserStreamAdapter(){

        override fun onStatus(x: Status) {


        }

        override fun onException(ex: Exception) {
            super.onException(ex)
            ex.printStackTrace()
        }

        override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {

        }

        override fun onFavorite(source: User, target: User, favoritedStatus: Status) {
            super.onFavorite(source, target, favoritedStatus)
        }
    }
}
