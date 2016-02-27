package xyz.donot.twix.view.activity


import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.widget.SearchView
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.event.OnCardViewTouchEvent
import xyz.donot.twix.event.OnCustomtabEvent
import xyz.donot.twix.event.OnHashtagEvent
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterUpdateObservable
import xyz.donot.twix.util.*
import xyz.donot.twix.view.adapter.TimeLinePagerAdapter


class MainActivity : RxAppCompatActivity() {
  val eventbus by lazy { EventBus.getDefault() }
  val twitter by lazy {  getTwitterInstance() }
  val searchView by lazy{toolbar.menu.findItem(R.id.menu_search).actionView as SearchView}
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      if(!haveToken())
      {
        startActivity(Intent(this@MainActivity, InitialActivity::class.java))
        finish()
      }
      else if(haveNetworkConnection()) {
        setContentView(R.layout.activity_main)
        toolbar.inflateMenu(R.menu.search)
        viewpager.adapter = TimeLinePagerAdapter(supportFragmentManager)
        toolbar.setNavigationOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
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
        button_tweet.setOnLongClickListener {
          TweetComposer.Builder(this@MainActivity).text(editText_status.editableText.toString()).show()
          true
        }
         button_tweet.setOnClickListener({
          val tObserver= TwitterUpdateObservable(twitter);
          tObserver.updateStatusAsync(editText_status.editableText.toString())
            .bindToLifecycle(this@MainActivity)
            .subscribe(object: TwitterSubscriber(){
            override fun onStatus(status: Status) {
            val   inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
              inputMethodManager.hideSoftInputFromWindow(coordinatorLayout.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
              Snackbar.make(coordinatorLayout,"投稿しました", Snackbar.LENGTH_LONG).setAction("取り消す", {
                tObserver.deleteStatusAsync(status.id).subscribe {
                Toast.makeText(this@MainActivity,"削除しました",Toast.LENGTH_LONG).show()
                }
                 }).show()
                          }

          })
          editText_status.setText("")
        })
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
          override fun onQueryTextChange(p0: String): Boolean {

            return true
          }

          override fun onQueryTextSubmit(p0: String): Boolean {
            if(!p0.isNullOrBlank()){
              startActivity(Intent(this@MainActivity,SearchActivity::class.java).putExtra("query_txt",p0))
              searchView.clearFocus()

            }
            return true
          }
        })

        eventbus.register(this@MainActivity)


      }
      else{
        showSnackbar(coordinatorLayout,R.string.description_a_network_error_occurred)
      }

}

  override fun onDestroy() {
    super.onDestroy()
    eventbus.unregister(this@MainActivity)

  }

  @Subscribe(threadMode = ThreadMode.POSTING)
 fun onCardviewTouched(onCardViewTouchEvent: OnCardViewTouchEvent)
  {

    val tweetItem=if(getMyId()==onCardViewTouchEvent.status.user.id){R.array.tweet_my_menu}else{R.array.tweet_menu}
    AlertDialog.Builder(this@MainActivity)
      .setItems(tweetItem, { dialogInterface, i ->
        val selectedItem=resources.getStringArray(tweetItem)[i]
        when (selectedItem) {
          "削除" -> {
            TwitterUpdateObservable(twitter).deleteStatusAsync(onCardViewTouchEvent.status.id).subscribe (object : TwitterSubscriber() {
              override fun onError(e: Throwable) {
                super.onError(e)
                Snackbar.make(coordinatorLayout, "失敗しました", Snackbar.LENGTH_LONG).show()
              }
              override fun onStatus(status: Status) {
                super.onStatus(status)
                Snackbar.make(coordinatorLayout, "削除しました", Snackbar.LENGTH_LONG).show()
              }
            })
          }
          "会話" -> {
            startActivity(Intent(this@MainActivity, TweetDetailActivity::class.java).putExtra("status_id", onCardViewTouchEvent.status.id))
          }
          "コピー" -> {
        val t= getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager
            t.primaryClip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_URILIST,onCardViewTouchEvent.status.text)
          }
        }


      })
      .show()
  }

  @Subscribe(threadMode = ThreadMode.POSTING)
  fun onHashTagTouched(onHashtagEvent: OnHashtagEvent)
  {

    startActivity(Intent(this@MainActivity,SearchActivity::class.java).putExtra("query_txt",onHashtagEvent.tag))
    searchView.clearFocus()

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
