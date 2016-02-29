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
import android.widget.Toast
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import twitter4j.Status
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.*
import xyz.donot.quetzal.twitter.StreamManager
import xyz.donot.quetzal.twitter.StreamType
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.view.adapter.TimeLinePagerAdapter


class MainActivity : RxAppCompatActivity() {
  val eventbus by lazy { EventBus.getDefault() }
  val twitter by lazy {  getTwitterInstance() }
  private var accountChanged=true
  val pagerAdapter by lazy { TimeLinePagerAdapter(supportFragmentManager) }
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      if(!haveToken())
      {
        startActivity(Intent(this@MainActivity, InitialActivity::class.java))
        finish()
      }
      else if(haveNetworkConnection()) {
        setContentView(R.layout.activity_main)
        viewpager.adapter = TimeLinePagerAdapter(supportFragmentManager)
        toolbar.apply {
          inflateMenu(R.menu.menu_main)
          setOnMenuItemClickListener { startActivity(Intent(this@MainActivity,SearchActivity::class.java))
            true
          }
          setNavigationOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
        }
        tabs.setupWithViewPager(viewpager)
        design_navigation_view.setNavigationItemSelectedListener({
          when (it.itemId) {
            R.id.my_profile-> {
              startActivity(Intent(this@MainActivity, EditProfileActivity::class.java))
              drawer_layout.closeDrawers()
            }
            R.id.setting -> {
              startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
              drawer_layout.closeDrawers()
            }
            R.id.account -> {
              startActivity(Intent(this@MainActivity, AccountSettingActivity::class.java))
              drawer_layout.closeDrawers()
            }
            R.id.list-> {
              startActivity(Intent(this@MainActivity, ListsActivity::class.java))
              drawer_layout.closeDrawers()
            }
            R.id. whats_new->{
              EventBus.getDefault().post(OnCustomtabEvent("http://donot.xyz/whats_new"))
              drawer_layout.closeDrawers()
            }
          }
          true
        })

        button_tweet.setOnLongClickListener {
          TweetComposer.Builder(this@MainActivity).text(editText_status.editableText.toString()).show()
          true
        }
        StreamManager.Factory.getStreamObject(applicationContext,twitter, StreamType.USER_STREAM).run()

        button_tweet.setOnClickListener(
          {  if(!editText_status.editableText.isNullOrBlank())
          {
            val tObserver= TwitterUpdateObservable(twitter);
            tObserver.updateStatusAsync(editText_status.editableText.toString())
              .bindToLifecycle(this@MainActivity)
              .subscribe(object: TwitterSubscriber(this@MainActivity){
                override fun onStatus(status: Status) {
                  val   inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                  inputMethodManager.hideSoftInputFromWindow(coordinatorLayout.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                  Snackbar.make(coordinatorLayout,"投稿しました", Snackbar.LENGTH_LONG).setAction("取り消す", {
                    tObserver.deleteStatusAsync(status.id).subscribe {
                      Toast.makeText(this@MainActivity,"削除しました", Toast.LENGTH_LONG).show()
                    }
                  }).show()
                }

              })
            editText_status.setText("")
          }})
        accountChanged=false
      }
      else{
        setContentView(R.layout.activity_main)
        showSnackbar(coordinatorLayout,R.string.description_a_network_error_occurred)
      }
      eventbus.register(this@MainActivity)

}

  override fun onStart() {
    super.onStart()

    if(accountChanged){
      pagerAdapter.destroyAllItem()
      viewpager.adapter=null
      tabs.removeAllTabs()
      viewpager.adapter = TimeLinePagerAdapter(supportFragmentManager)
      tabs.setupWithViewPager(viewpager)
    }
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
  fun onStreamDisconnected(onExceptionEvent: OnExceptionEvent)
  {

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
