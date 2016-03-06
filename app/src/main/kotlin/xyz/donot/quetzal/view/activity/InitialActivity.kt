package xyz.donot.quetzal.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.SignUpEvent
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_initial.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_initial.*
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.DBAccount
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.util.showSnackbar


class InitialActivity : RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        twitter_login_button.callback=object : Callback<TwitterSession>() {
          override fun success(result: Result<TwitterSession>) {
            val authToken = Twitter.getSessionManager().activeSession.authToken
          val  twitter_ =  TwitterFactory().instance.apply {
              setOAuthConsumer(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret))
              oAuthAccessToken= AccessToken(authToken.token, authToken.secret) }

            Answers.getInstance().logSignUp(SignUpEvent()
              .putMethod("Twitter")
              .putCustomAttribute("UserName",result.data.userName)
              .putSuccess(true))

           Realm.getDefaultInstance().use {
             realm->
             //すでに追加されているか
             if(! realm.where(DBAccount::class.java).equalTo("id",result.data.userId).isValid){
               showSnackbar( initial_activity_coordinator,R.string.description_already_added_account)
               return }
             //Twitterインスタンス保存
             realm.executeTransaction {
            if(realm.where(DBAccount::class.java).equalTo("isMain", true).findFirst()!=null){   it.where(DBAccount::class.java).equalTo("isMain", true).findFirst().isMain=false}
               if(it.where(DBAccount::class.java).equalTo("id", result.data.userId).findFirst()==null){
                 it.createObject(DBAccount::class.java).apply {
                   id =result.data.userId
                   isMain = true
                   twitter = twitter_.getSerialized()} }

               else{
                 Snackbar.make(coordinatorLayout,"すでに追加されているアカウントです",Snackbar.LENGTH_LONG).show()
               }
             }
           }
            //Userインスタンス


             TwitterObservable(this@InitialActivity, twitter_)
               .showUser(result.data.userId)
                .subscribe(
                  { user_->
                    Realm.getDefaultInstance()
                      .use {
                      realm->
                      realm.executeTransaction{
                    it.where(DBAccount::class.java).equalTo("id",result.data.userId)
                      .findFirst()
                      .apply { user=user_.getSerialized()}
                    }
                    }
                  }
                ,{it.printStackTrace()})
            //MuteUser
          /*  UsersObservable(twitter_).getMuteList(-1L)
              .subscribe({user->
                Realm.getDefaultInstance().use {
                  it.executeTransaction {
                    realmEX->
                    user.forEach {
                      realmEX
                        .createObject(DBMuteUser::class.java)
                        .apply {
                          myid=result.data.userId
                          id =result.data.userId
                        }
                    }
                  }}})*/


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
