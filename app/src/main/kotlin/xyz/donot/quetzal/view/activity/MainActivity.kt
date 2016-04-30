package xyz.donot.quetzal.view.activity


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.inputmethod.InputMethodManager
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_header.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import twitter4j.Status
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.OnAccountChanged
import xyz.donot.quetzal.event.OnCustomtabEvent
import xyz.donot.quetzal.event.OnHashtagEvent
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.twitter.StreamManager
import xyz.donot.quetzal.twitter.StreamType
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.twitter.UsersObservable
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.util.extrautils.intent
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.util.extrautils.toast
import xyz.donot.quetzal.view.adapter.TimeLinePagerAdapter


class MainActivity : RxAppCompatActivity() {
  val eventbus by lazy { EventBus.getDefault() }
  val twitter by lazy { getTwitterInstance() }
  private var accountChanged = true
  val pagerAdapter by lazy { TimeLinePagerAdapter(supportFragmentManager) }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (!haveToken()) {
      startActivity(intent<InitialActivity>())
      finish()
    } else {
      setContentView(R.layout.activity_main)
      if (!haveNetworkConnection()) {
        showSnackbar(coordinatorLayout, R.string.description_a_network_error_occurred)
      }
      viewpager.adapter = pagerAdapter
      toolbar.apply {
        inflateMenu(R.menu.menu_main)
        setOnMenuItemClickListener {
          start<SearchActivity>()
          true
        }
        setNavigationOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
      }


    tabs.setupWithViewPager(viewpager)
    design_navigation_view.setNavigationItemSelectedListener({
      if (haveNetworkConnection()) {
        when (it.itemId) {
          R.id.action_my_profile -> {
            start<EditProfileActivity>()
            drawer_layout.closeDrawers()
          }
          R.id.action_setting -> {
            start<SettingsActivity>()
            drawer_layout.closeDrawers()
          }
          R.id.action_account -> {
            start<AccountSettingActivity>()
            drawer_layout.closeDrawers()
          }
          R.id.action_list -> {
            start<ListsActivity>()
            drawer_layout.closeDrawers()
          }
          R.id.action_whats_new -> {
            EventBus.getDefault().post(OnCustomtabEvent("http://donot.xyz/whats_new.html"))
            drawer_layout.closeDrawers()
          }
        }
      }
      true
    })
      //set up header
      UsersObservable(twitter).getMyUserInstance().forEach {
        Picasso.with(applicationContext).load(it.profileBannerIPadRetinaURL).into(my_header)
        Picasso.with(applicationContext).load(it.originalProfileImageURLHttps).into(my_icon)
        my_screen_name.text = it.screenName
      }



    button_tweet.setOnLongClickListener {
      TweetComposer.Builder(this@MainActivity).text(editText_status.editableText.toString()).show()
      true
    }
    StreamManager.Factory.getStreamObject(applicationContext, twitter, StreamType.USER_STREAM).run()
    button_tweet.setOnClickListener(
      {
        if (!editText_status.editableText.isNullOrBlank()) {
          val tObserver = TwitterUpdateObservable(this@MainActivity,twitter);
          tObserver.updateStatusAsync(editText_status.editableText.toString())
            .bindToLifecycle(this@MainActivity)
            .subscribe(object : TwitterSubscriber(this@MainActivity) {
              override fun onStatus(status: Status) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(coordinatorLayout.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                Snackbar.make(coordinatorLayout, "投稿しました", Snackbar.LENGTH_LONG).setAction("取り消す", {
                  tObserver.deleteStatusAsync(status.id).subscribe {
                    toast("削除しました")
                  }
                }).show()
              }

            })
          editText_status.setText("")
        }
      })
    accountChanged = false

  }

      eventbus.register(this@MainActivity)
}
  override fun onDestroy() {
    super.onDestroy()
    eventbus.unregister(this@MainActivity)
  }

  @Subscribe
  fun onAccountChanged(onAccountChanged: OnAccountChanged)
  {
    accountChanged=true
  }
  @Subscribe
  fun onHashTagTouched(onHashtagEvent: OnHashtagEvent)
  {
    startActivity(Intent(this@MainActivity,SearchActivity::class.java).putExtra("query_txt",onHashtagEvent.tag))
  }


  @Subscribe
 fun onCustomTabEvent(onCustomTabEvent: OnCustomtabEvent){
    CustomTabsIntent.Builder()
      .setShowTitle(true)
      .addDefaultShareMenuItem()
      .setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
      .setStartAnimations(this@MainActivity, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
      .setExitAnimations(this@MainActivity, android.R.anim.slide_in_left, android.R.anim.slide_out_right).build()
      .launchUrl(this@MainActivity, Uri.parse(onCustomTabEvent.url))
  }

}
