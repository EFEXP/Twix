package xyz.donot.quetzal.util;

import android.content.Context
import android.os.Environment
import android.preference.PreferenceManager
import io.realm.Realm
import org.greenrobot.eventbus.EventBus
import twitter4j.Status
import twitter4j.Twitter
import xyz.donot.quetzal.event.OnReplyEvent
import xyz.donot.quetzal.event.OnStatusEvent
import xyz.donot.quetzal.model.DBAccount
import xyz.donot.quetzal.model.DBMuteUser
import xyz.donot.quetzal.notification.NewMentionNotification
import xyz.donot.quetzal.util.extrautils.i
import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit


fun getPictureStorePath(): File {
  return  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
}
fun getFabricTwitterInstance(): com.twitter.sdk.android.Twitter {
  return com.twitter.sdk.android.Twitter.getInstance()
}

fun isIgnore(id: Long): Boolean {
 return  Realm.getDefaultInstance().where(DBMuteUser::class.java)
  .equalTo("id",id).count()>0
}

fun isMentionToMe(status: Status): Boolean {
  return  status.userMentionEntities.map { it.id }.filter { it==getMyId() }.isNotEmpty()
}

fun Status.save(context:Context){
  val status=this
  if(!isIgnore(status.user.id)){
    if(isMentionToMe(status)){
      EventBus.getDefault().post(OnReplyEvent(status))
      if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notifications",false))
      {
        NewMentionNotification.notify(context,status.text,0)
      }
      }
      EventBus.getDefault().post(OnStatusEvent(status))
   }
  }



fun<T:Serializable> T.getSerialized():ByteArray{
  ByteArrayOutputStream().use {
  val out = ObjectOutputStream(it);
  out.writeObject(this);
  val bytes = it.toByteArray();
  out.close();
  return bytes;
  }
}


fun<T> ByteArray.getDeserialized():T{
  @Suppress("UNCHECKED_CAST")
  return ObjectInputStream(ByteArrayInputStream(this)).readObject()as T
}

fun getMyId(): Long{
  Realm.getDefaultInstance().use {
    return  it.where(DBAccount::class.java).equalTo("isMain",true).findFirst().id
  }
}


fun getRelativeTime(create: Date): String {
  val datetime1 = System.currentTimeMillis()
  val datetime2 = create.time
  val Difference = datetime1 - datetime2
  return if (Difference < 60000L) {
    "%d秒前".format(TimeUnit.MILLISECONDS.toSeconds(Difference))
  } else if (Difference < 3600000L) {
    "%d分前".format(TimeUnit.MILLISECONDS.toMinutes(Difference))
  } else if (Difference < 86400000L) {
    "%d時間前".format(TimeUnit.MILLISECONDS.toHours(Difference))
  } else {
    "%d日前".format(TimeUnit.MILLISECONDS.toDays(Difference))
  }
}


fun getTwitterInstance(): twitter4j.Twitter {
  Realm.getDefaultInstance().use {
    val ac= it.where(DBAccount::class.java).equalTo("isMain",true).findFirst()
    return ac.twitter?.getDeserialized<Twitter>()?:throw IllegalStateException()
  }

}

fun haveToken(): Boolean {
  Realm.getDefaultInstance().use {
   i( "AddedAccounts","You have ${it.where(DBAccount::class.java).count()} accounts!")
 return  it.where(DBAccount::class.java).count()>0
 }
  }


