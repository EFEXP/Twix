package xyz.donot.twix.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_initial.*
import kotlinx.android.synthetic.main.content_initial.*
import twitter4j.PagableResponseList
import twitter4j.User
import xyz.donot.twix.R
import xyz.donot.twix.event.TwitterUsersSubscriber
import xyz.donot.twix.model.DBAccount
import xyz.donot.twix.model.DBMuteUser
import xyz.donot.twix.twitter.UsersObservable
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.showSnackbar


class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        setSupportActionBar(toolbar)
        twitter_login_button.callback=object : Callback<TwitterSession>() {
          override fun success(result: Result<TwitterSession>) {

            val authToken = Twitter.getSessionManager().activeSession.authToken
           Realm.getDefaultInstance().use {
             realm->
             if(! realm.where(DBAccount::class.java).equalTo("id",result.data.userId).isValid){
               showSnackbar( initial_activity_coordinator,R.string.description_already_added_account)
             return
             }
             realm.executeTransaction {
               it.where(DBAccount::class.java).equalTo("isMain", true).findFirst().isMain=false
               it.createObject(DBAccount::class.java).apply {
                key = authToken.token
                secret = authToken.secret
                id =result.data.userId
                screenName =result.data.userName

                   isMain = true

              }
             }
             //MuteUser

            UsersObservable(getTwitterInstance()).getMuteList(-1L).subscribe(
              object: TwitterUsersSubscriber(){
                override fun onNext(user: PagableResponseList<User>) {
                  super.onNext(user)
                  realm.executeTransaction {
                    realmEX->
                    user.forEach {
                      realmEX.createObject(DBMuteUser::class.java).apply {
                        myid=result.data.userId
                        id =result.data.userId
                      }
                    }

                  }
                }
              })
           }
            startActivity(Intent(this@InitialActivity, MainActivity::class.java))
            finish()
          }
          override fun failure(exception: TwitterException?) {
        showSnackbar( initial_activity_coordinator,R.string.description_a_network_error_occurred)
          }
        }
    }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    twitter_login_button.onActivityResult(requestCode, resultCode, data)
  }

}
