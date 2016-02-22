package xyz.donot.twix.view.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.event.OnCustomtabEvent
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterUpdateObservable
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.haveNetworkConnection
import xyz.donot.twix.util.haveToken
import xyz.donot.twix.util.showSnackbar
import xyz.donot.twix.view.adapter.TimeLinePagerAdapter


class MainActivity : AppCompatActivity() {
  val eventbus by lazy { EventBus.getDefault() }
  val twitter by lazy {  getTwitterInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {

      super.onCreate(savedInstanceState)
      if(!haveToken())
      {
        startActivity(Intent(this@MainActivity, InitialActivity::class.java))
        finish()
      }
      else if(haveNetworkConnection()) {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        viewpager.adapter=TimeLinePagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(viewpager)
        design_navigation_view.setNavigationItemSelectedListener({
          when (it.itemId) {
            R.id.setting -> {
              startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
              drawer_layout.closeDrawers()
            }
            R.id.account -> {
              startActivity(Intent(this@MainActivity, AccountSettingActivity::class.java))
              drawer_layout.closeDrawers() }
          }
          true
        })
        button_tweet.setOnClickListener({
          val tObserver= TwitterUpdateObservable(twitter);
          tObserver.updateStatusAsync(  editText_status.editableText.toString()).subscribe(object:TwitterSubscriber(){
            override fun onLoaded() {
            }
            override fun onStatus(status: Status) {

              Snackbar.make(coordinatorLayout,"投稿しました", Snackbar.LENGTH_LONG).setAction("取り消す", {
                Thread(Runnable {twitter.destroyStatus(status.id) })
              }).show()
                          }
          })
          editText_status.setText("")

        })
      }
      else{
        showSnackbar(coordinatorLayout,R.string.description_a_network_error_occurred)
      }



}

  override fun onPause() {
    super.onPause()
    eventbus.unregister(this@MainActivity)
  }

  override fun onResume() {
    super.onResume()
    eventbus.register(this@MainActivity)
  }

  @Subscribe
 fun onEventMainThread(onCustomTabEvent: OnCustomtabEvent){
    CustomTabsIntent.Builder()
      .setShowTitle(true)
      .setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
      .setStartAnimations(this@MainActivity, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
      .setExitAnimations(this@MainActivity, android.R.anim.slide_in_left, android.R.anim.slide_out_right).build()
      .launchUrl(this@MainActivity, Uri.parse(onCustomTabEvent.url))
  }

}
