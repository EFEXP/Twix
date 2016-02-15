package xyz.donot.twix.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_initial.*
import kotlinx.android.synthetic.main.content_initial.*
import xyz.donot.twix.R
import xyz.donot.twix.model.DBAccount


class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        setSupportActionBar(toolbar)
        twitter_login_button.callback=object : Callback<TwitterSession>() {
          override fun success(result: Result<TwitterSession>) {
            val authToken = Twitter.getSessionManager().activeSession.authToken
           Realm.getDefaultInstance().use {
             it.executeTransaction {
              it.createObject(DBAccount::class.java).apply {
                key = authToken.token
               secret = authToken.secret
               id =result.data.userId
                name =result.data.userName
               isMain = true
              }
             }
           }
            startActivity(Intent(this@InitialActivity, CentralActivity::class.java))
            finish()
          }
          override fun failure(exception: TwitterException) {
            Snackbar.make(initial_activity_coordinator,"認証に失敗しました",Snackbar.LENGTH_LONG).show()
          }
        }
    }
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)
    twitter_login_button.onActivityResult(requestCode, resultCode, data)
  }

}
