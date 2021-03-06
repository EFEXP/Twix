package xyz.donot.quetzal.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_user.*
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.realm.DBMute
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.bindToLifecycle
import xyz.donot.quetzal.util.extrautils.toast
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.viewmodel.adapter.AnyUserTimeLineAdapter

class UserActivity : RxAppCompatActivity() {
  var userId :Long=0

    val userName: String by lazy { intent.getStringExtra("user_name") }
  val twitter by lazy {getTwitterInstance()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        userId=intent.getLongExtra("user_id",0L)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.inflateMenu(R.menu.menu_user)
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
    Picasso.with(applicationContext).load(user.profileBannerIPadURL).into(banner)
    banner.setOnClickListener{startActivity(Intent(applicationContext, PictureActivity::class.java)
            .putStringArrayListExtra("picture_urls",arrayListOf(user.profileBannerIPadRetinaURL)))}
    toolbar.title=user.name
    toolbar.subtitle=user.screenName
   val adapter= AnyUserTimeLineAdapter(supportFragmentManager)
      adapter.user=user
    viewpager_user.adapter=adapter
      viewpager_user.offscreenPageLimit=3
    tabs_user.setupWithViewPager(viewpager_user)

      toolbar.setOnMenuItemClickListener {
          when(it.itemId)
          {
              R.id.mute_user->{
                  val muted=Realm.getDefaultInstance().where(DBMute::class.java).equalTo("id",userId).findAll()
                  if(muted.isEmpty()){
                  Realm.getDefaultInstance().executeTransaction {
                     val db= it.createObject(DBMute::class.java,user.id)
                      db.myid=getMyId()
                  }
                  toast("ミュートしました")
                  }
                  else{
                      Realm.getDefaultInstance().executeTransaction {
                          muted.deleteAllFromRealm()
                      }
                      toast("ミュート解除しました")
                  }
                  true
              }
              R.id.block_user->{

                  true
              }
              else -> {
                  throw IllegalAccessError()
              }
          }
      }
  }
}
