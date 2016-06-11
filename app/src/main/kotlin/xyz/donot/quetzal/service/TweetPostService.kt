package xyz.donot.quetzal.service

import android.app.IntentService
import android.content.Intent
import twitter4j.StatusUpdate
import xyz.donot.quetzal.notification.NotificationWrapper
import xyz.donot.quetzal.util.extrautils.getNotificationManager
import xyz.donot.quetzal.util.getDeserialized
import xyz.donot.quetzal.util.getTwitterInstance
import java.io.File
import java.util.*

class TweetPostService() : IntentService("TweetPostService") {
    val twitter by lazy { getTwitterInstance() }
    override fun onHandleIntent(intent: Intent) {
        val filePath: ArrayList<String>
        val id=Random().nextInt(100)+1
        if(intent.hasExtra("StatusUpdate")){
            val updateStatus= intent.getByteArrayExtra("StatusUpdate").getDeserialized<StatusUpdate>()
            if(intent.hasExtra("FilePath")){
               filePath=intent.getStringArrayListExtra("FilePath")
                NotificationWrapper(applicationContext).sendingNotification(id)
                val uploadedMediaId = filePath.map {  twitter.uploadMedia(File(it)).mediaId }
                val array = LongArray(uploadedMediaId.size)
                var i=0
                while (i < uploadedMediaId.size) {
                    array[i] = uploadedMediaId[i]
                    i++
                }
                updateStatus.setMediaIds(array)
            }
          try {
              twitter.updateStatus(updateStatus)
          }
            catch(e:Exception){
                applicationContext.getNotificationManager().cancel(id)
                //     NotificationWrapper(applicationContext).sendingFailureNotification()
            }
            applicationContext.getNotificationManager().cancel(id)

        }
    }
}