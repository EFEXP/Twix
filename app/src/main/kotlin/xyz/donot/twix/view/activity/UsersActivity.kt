package xyz.donot.twix.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import kotlinx.android.synthetic.main.content_users.*
import twitter4j.PagableResponseList
import twitter4j.User
import xyz.donot.twix.R
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.view.adapter.UsersAdapter
import xyz.donot.twix.view.listener.OnLoadMoreListener
import java.util.*

class UsersActivity() : RxAppCompatActivity() {
    val data by lazy { LinkedList<User>() }
    val mAdapter by lazy { UsersAdapter(this@UsersActivity,data) }
    val mode by lazy { intent.getStringExtra("mode") }
    val twitter by lazy { getTwitterInstance() }
    val userid by lazy { intent.getLongExtra("userid",0) }
    internal var cursor = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
      recycler_view_users.apply{
        itemAnimator= OvershootInRightAnimator(0.1f)
        adapter = AlphaInAnimationAdapter(mAdapter)
        recycler_view_users.adapter=mAdapter
        layoutManager = LinearLayoutManager(this@UsersActivity)
        addOnScrollListener(object: OnLoadMoreListener(){
          override fun onScrolledToBottom() {
            timelineLoader()
          }

        }

        )

        timelineLoader()
      }


    }
  internal fun addAdapter(list: PagableResponseList<User>) {
    Handler(Looper.getMainLooper()).post {
      cursor = list.nextCursor
      list.forEach { mAdapter.add(it) }
    }

}
fun timelineLoader(){
  when(mode){
    "friend"->{
      TwitterObservable(twitter).getFriendsAsync(userid,cursor).subscribe {
          addAdapter(it)
      }}
      "follower"->{
        TwitterObservable(twitter).getFollowerAsync(userid,cursor).subscribe {
          addAdapter(it)
        }}

    }
  }

}


