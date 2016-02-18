package xyz.donot.twix.view.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


import xyz.donot.twix.R
import xyz.donot.twix.event.OnCustomtabEvent
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.haveToken
import xyz.donot.twix.util.updateUserProfile
import xyz.donot.twix.view.adapter.TimeLinePagerAdapter


class MainActivity : AppCompatActivity() {
  val eventbus by lazy { EventBus.getDefault() }
    override fun onCreate(savedInstanceState: Bundle?) {

      super.onCreate(savedInstanceState)
      if(!haveToken())
      {
        startActivity(Intent(this@MainActivity, InitialActivity::class.java))
        finish()
      }
      else {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
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
              drawer_layout.closeDrawers()
                }
          }
          true
        })

        updateUserProfile(this@MainActivity.getTwitterInstance())
      }



}

  override fun onPause() {
    super.onPause()
    eventbus.unregister(this@MainActivity)
  }

  override fun onResume() {
    super.onResume()
    eventbus.register(this@MainActivity)}

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
