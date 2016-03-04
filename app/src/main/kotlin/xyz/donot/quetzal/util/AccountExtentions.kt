package xyz.donot.quetzal.util;

import android.content.Context
import android.preference.PreferenceManager
import io.realm.Realm
import org.greenrobot.eventbus.EventBus
import twitter4j.Status
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.OnReplyEvent
import xyz.donot.quetzal.event.OnStatusEvent
import xyz.donot.quetzal.model.DBAccount
import xyz.donot.quetzal.model.DBMuteUser
import xyz.donot.quetzal.notification.NewMentionNotification
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import java.util.concurrent.TimeUnit


fun Context.getTwitterInstance(): twitter4j.Twitter {
  val consumerKey =this.getString(R.string.twitter_consumer_key)
  val consumerSecret = this.getString(R.string.twitter_consumer_secret)
  val twitter =  TwitterFactory().instance
  twitter.setOAuthConsumer(consumerKey, consumerSecret)
  twitter.oAuthAccessToken=loadAccessToken()
  return twitter
}

fun Context.getFabricTwitterInstance(): com.twitter.sdk.android.Twitter {
  return com.twitter.sdk.android.Twitter.getInstance()
}

fun isIgnore(id: Long): Boolean {
 return  Realm.getDefaultInstance().where(DBMuteUser::class.java)
  .equalTo("id",id).count()>0
}

fun isMentionToMe(status: Status): Boolean {
  return  status.userMentionEntities.map { it.id }.filter { it==getMyId() }.isNotEmpty()
}
/*Usage
*0=status
* 1=reply
* 2=notifications
*/
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
   }//execute
  }



fun Status.getSerialized():ByteArray{
  ByteArrayOutputStream().use {
  val out = ObjectOutputStream(it);
  out.writeObject(this);
  val bytes = it.toByteArray();
  out.close();
  it.close();
  return bytes;
  }
}
fun ByteArray.getStatusDeserialized():Status{
  return ObjectInputStream(ByteArrayInputStream(this)).readObject()as Status
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
fun loadAccessToken(): AccessToken {
  Realm.getDefaultInstance().use {
  val ac= it.where(DBAccount::class.java).equalTo("isMain",true).findFirst()
  return AccessToken(ac.key, ac.secret)
  }
}


fun haveToken(): Boolean {
  Realm.getDefaultInstance().use {
   logi( "AddedAccounts","You have ${it.where(DBAccount::class.java).count()} accounts!")
 return  it.where(DBAccount::class.java).count()>0
 }
  }

