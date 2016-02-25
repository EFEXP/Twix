package xyz.donot.twix.view.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.bumptech.glide.Glide
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_user.*
import rx.Subscriber
import twitter4j.User
import xyz.donot.twix.R
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.bindToLifecycle
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.view.adapter.AnyUserTimeLineAdapter

class UserActivity : RxAppCompatActivity() {
  val userId by lazy { intent.getLongExtra("user_id",0) }
  val twitter by lazy {getTwitterInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        TwitterObservable(twitter).showUser(userId).bindToLifecycle(this@UserActivity)
      .subscribe(object : Subscriber<User>() {
        override fun onCompleted() {

        }
        override fun onError(p0: Throwable) {
        p0.printStackTrace()
        }
        override fun onNext(p0: User) {
          Glide.with(this@UserActivity).load(p0.profileBannerIPadURL).into(banner)
          toolbar.title=p0.name
          toolbar.subtitle=p0.screenName
        }
      })
        viewpager_user.adapter=AnyUserTimeLineAdapter(supportFragmentManager,userId)
        tabs_user.setupWithViewPager(viewpager_user)




    }
}
