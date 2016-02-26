package xyz.donot.twix.view.activity

import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.content_tweet_edit.*
import twitter4j.Status
import twitter4j.StatusUpdate
import xyz.donot.twix.R
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterUpdateObservable
import xyz.donot.twix.util.getTwitterInstance

class TweetEditActivity : RxAppCompatActivity() {
      val  twitter  by lazy {  getTwitterInstance() }
  val  statusId by lazy {  intent.getLongExtra("status_id",0) }
  val screenName by lazy { intent.getStringExtra("user_screen_name") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_edit)
      toolbar.setNavigationOnClickListener { finish() }
      editText_status.setText("@$screenName")
      send_status.setOnClickListener{
        val updateStatus= StatusUpdate(editText_status.text.toString())
        updateStatus.inReplyToStatusId=statusId
        TwitterUpdateObservable(twitter).updateStatusAsync(updateStatus)
        .subscribe (object : TwitterSubscriber() {
          override fun onError(e: Throwable) {
            super.onError(e)
          }

          override fun onStatus(status: Status) {
            super.onStatus(status)
          }
        })
        finish()
      }


    }

}
