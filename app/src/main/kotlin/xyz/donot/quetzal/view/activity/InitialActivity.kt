package xyz.donot.quetzal.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.SignUpEvent
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_initial.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_initial.*
import twitter4j.PagableResponseList
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.TwitterUsersSubscriber
import xyz.donot.quetzal.model.DBAccount
import xyz.donot.quetzal.model.DBMuteUser
import xyz.donot.quetzal.twitter.UsersObservable
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.util.showSnackbar


class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        twitter_login_button.callback=object : Callback<TwitterSession>() {
          override fun success(result: Result<TwitterSession>) {
            val authToken = Twitter.getSessionManager().activeSession.authToken
            Answers.getInstance().logSignUp(SignUpEvent()
              .putMethod("Twitter")
              .putCustomAttribute("UserName",result.data.userName)
              .putSuccess(true))
           Realm.getDefaultInstance().use {
             realm->
             if(! realm.where(DBAccount::class.java).equalTo("id",result.data.userId).isValid){
               showSnackbar( initial_activity_coordinator,R.string.description_already_added_account)
             return
             }
             realm.executeTransaction {
            if(it.where(DBAccount::class.java).equalTo("isMain", true).findFirst()!=null){   it.where(DBAccount::class.java).equalTo("isMain", true).findFirst().isMain=false}
               if(it.where(DBAccount::class.java).equalTo("id", result.data.userId).findFirst()==null){
               it.createObject(DBAccount::class.java).apply {
                key = authToken.token
                secret = authToken.secret
                id =result.data.userId
                screenName =result.data.userName
                   isMain = true

              }}
               else{
                 Snackbar.make(coordinatorLayout,"すでに追加されているアカウントです",Snackbar.LENGTH_LONG).show()
               }
             }
             //MuteUser

            UsersObservable(getTwitterInstance()).getMuteList(-1L).subscribe(
              object: TwitterUsersSubscriber(this@InitialActivity){
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
