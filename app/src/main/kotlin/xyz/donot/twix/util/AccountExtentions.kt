package xyz.donot.twix.util;

import android.content.Context
import io.realm.Realm
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import xyz.donot.twix.R
import xyz.donot.twix.model.DBAccount
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


fun getMyName(): String{
  Realm.getDefaultInstance().use {
    val ac= it.where(DBAccount::class.java).equalTo("isMain",true).findFirst()
    return ac.name?:throw IllegalStateException()
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
fun Context.loadAccessToken(): AccessToken {
  Realm.getDefaultInstance().use {
  val ac= it.where(DBAccount::class.java).equalTo("isMain",true).findFirst()
  return AccessToken(ac.key, ac.secret)
  }
}
fun Context.haveToken(): Boolean {
  Realm.getDefaultInstance().use {
   logi( "AddedAccounts","You have ${it.where(DBAccount::class.java).count()} accounts!")
 return  it.where(DBAccount::class.java).count()>0
 }
  }

