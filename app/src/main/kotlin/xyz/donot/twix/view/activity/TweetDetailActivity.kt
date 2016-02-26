package xyz.donot.twix.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.content_tweet_detail.*
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.view.adapter.StatusAdapter
import java.util.*

class TweetDetailActivity : AppCompatActivity() {

   val data by lazy { LinkedList<Status>() }
  val  mAdapter by lazy { StatusAdapter(this@TweetDetailActivity, data) }
  val twitter by lazy { getTwitterInstance()}
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_detail)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
      loadReply(intent.extras.getLong("status_id"))
    detail_recycler_view.adapter = mAdapter
      detail_recycler_view.layoutManager = LinearLayoutManager(this@TweetDetailActivity)



    }
  fun loadReply(long: Long){
    val observer= TwitterObservable(twitter).showStatusAsync(long)
    observer.subscribe {
      mAdapter.add(it) }
    observer.subscribe {
      val voo=it.inReplyToStatusId>0
      if(voo){
      loadReply(it.inReplyToStatusId)
    } }
  }


}
