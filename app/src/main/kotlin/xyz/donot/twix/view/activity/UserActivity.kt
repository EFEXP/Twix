package xyz.donot.twix.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_user.*
import twitter4j.User
import xyz.donot.twix.R
import xyz.donot.twix.event.TwitterUserSubscriber
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.bindToLifecycle
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.view.adapter.AnyUserTimeLineAdapter

class UserActivity : RxAppCompatActivity() {
  val userId by lazy { intent.getLongExtra("user_id",0) }
  val userName by lazy { intent.getStringExtra("user_name") }
  val twitter by lazy {getTwitterInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
      toolbar.setNavigationOnClickListener { finish() }
      if(userName.isNullOrEmpty()){
        TwitterObservable(twitter).showUser(userId).bindToLifecycle(this@UserActivity)
      .subscribe(object :TwitterUserSubscriber(){
        override fun onUser(user: User) {
          setUp(user)
        }
      })
      }
      else{
        TwitterObservable(twitter).showUser(userName).bindToLifecycle(this@UserActivity)
          .subscribe(object :TwitterUserSubscriber(){
            override fun onUser(user: User) {
              setUp(user)
            }
          })
      }





    }

  fun setUp(user: User){
    Picasso.with(this@UserActivity).load(user.profileBannerIPadURL).into(banner)
    banner.setOnClickListener{startActivity(Intent(this@UserActivity, PictureActivity::class.java).putStringArrayListExtra("picture_urls",arrayListOf(user.profileBannerIPadRetinaURL)))}
    toolbar.title=user.name
    toolbar.subtitle=user.screenName
    viewpager_user.adapter=AnyUserTimeLineAdapter(supportFragmentManager,user.id)
    tabs_user.setupWithViewPager(viewpager_user)
  }
}
