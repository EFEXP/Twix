package xyz.donot.twix.util;

import android.content.Context
import com.twitter.sdk.android.core.TwitterApiErrorConstants
import io.realm.Realm
import rx.lang.kotlin.observable
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.User
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
@Suppress
fun Context.getNamedTwitterInstance(id:Long): twitter4j.Twitter {
  val consumerKey =this.getString(R.string.twitter_consumer_key)
  val consumerSecret = this.getString(R.string.twitter_consumer_secret)
  val twitter =  TwitterFactory().instance
  twitter.setOAuthConsumer(consumerKey, consumerSecret)
  twitter.oAuthAccessToken=loadAccessToken()
  return twitter
}


fun updateUserProfile(twitter:Twitter){
  observable<User> {twitter.showUser(getMyId())  }
    .basicNetworkTask()
    .subscribe {
      user->
      Realm.getDefaultInstance().use {
        it.executeTransaction{
          val account=  it.where(DBAccount::class.java).equalTo("id",user.id).findFirst()
          account.apply {
            name=user.name
            profileImageUrl=user.originalProfileImageURL
            screenName=user.screenName
          }

        }

      }
    }
}

fun getMyId(): Long{
  Realm.getDefaultInstance().use {
    val ac= it.where(DBAccount::class.java).equalTo("isMain",true).findFirst()
    return ac.id
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

fun Context.profileUpdate()
{
 /* Realm.getDefaultInstance().use {realm->
    val ac= realm.where(DBAccount::class.java).equalTo("isMain",true).findAllAsync().asObservable()
    .map { it.forEach { return@map this.getNamedTwitterInstance(it.id) } }
    .map { it as Twitter }
    .map { it.verifyCredentials() }
    .subscribe {
      re
    }
  }*/
}
fun haveToken(): Boolean {
  Realm.getDefaultInstance().use {
   logi( "AddedAccounts","You have ${it.where(DBAccount::class.java).count()} accounts!")
 return  it.where(DBAccount::class.java).count()>0
 }
  }

