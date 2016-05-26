package xyz.donot.quetzal.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_user.*
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.bindToLifecycle
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.adapter.AnyUserTimeLineAdapter

class UserActivity : RxAppCompatActivity() {
  var userId :Long=0

    val userName by lazy { intent.getStringExtra("user_name") }
  val twitter by lazy {getTwitterInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        userId=intent.getLongExtra("user_id",0L)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
      toolbar.setNavigationOnClickListener { finish() }
      if(userName.isNullOrEmpty()){
        TwitterObservable(applicationContext,twitter)
          .showUser(userId)
          .bindToLifecycle(this@UserActivity)
      .subscribe({ setUp(it)})
      }
      else{
        TwitterObservable(applicationContext,twitter).showUser(userName).bindToLifecycle(this@UserActivity)
          .subscribe{
              userId=it.id
              setUp(it)}
      }
    }

  fun setUp(user: User){
    Picasso.with(this@UserActivity).load(user.profileBannerIPadURL).into(banner)
    banner.setOnClickListener{startActivity(Intent(this@UserActivity, PictureActivity::class.java)
            .putStringArrayListExtra("picture_urls",arrayListOf(user.profileBannerIPadRetinaURL)))}
    toolbar.title=user.name
    toolbar.subtitle=user.screenName
   val adapter= AnyUserTimeLineAdapter(supportFragmentManager)
    adapter.userId=userId
    viewpager_user.adapter=adapter

    tabs_user.setupWithViewPager(viewpager_user)
  }
}
