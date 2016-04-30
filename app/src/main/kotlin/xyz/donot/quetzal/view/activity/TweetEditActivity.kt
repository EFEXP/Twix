package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.widget.Toast
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_tweet_edit.*
import kotlinx.android.synthetic.main.content_tweet_edit.*
import twitter4j.StatusUpdate
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.getTwitterInstance

class TweetEditActivity : RxAppCompatActivity() {
      val  twitter  by lazy {  getTwitterInstance() }
  val  statusId by lazy {  intent.getLongExtra("status_id",0) }
  val screenName by lazy { intent.getStringExtra("user_screen_name") }
  val statusTxt by lazy { intent.getStringExtra("status_txt") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_edit)
      toolbar.setNavigationOnClickListener { finish() }
      editText_status.setText("@$screenName")
      editText_status.selectionEnd
      reply_for_status.text=statusTxt
      send_status.setOnClickListener{
        val updateStatus= StatusUpdate(editText_status.text.toString())
        updateStatus.inReplyToStatusId=statusId
        TwitterUpdateObservable(this@TweetEditActivity,twitter).updateStatusAsync(updateStatus)
        .subscribe ({ Toast.makeText(this@TweetEditActivity,"送信しました",Toast.LENGTH_LONG).show()
          finish()},
          {Toast.makeText(this@TweetEditActivity,"失敗しました",Toast.LENGTH_LONG).show()
            finish()
          })

      }


    }

}
