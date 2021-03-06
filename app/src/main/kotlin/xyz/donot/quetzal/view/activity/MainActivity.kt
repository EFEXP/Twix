package xyz.donot.quetzal.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_header.*
import rx.lang.kotlin.BehaviorSubject
import twitter4j.Status
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.databinding.ActivityMainBinding
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.event.TwitterUserSubscriber
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.twitter.UsersObservable
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.util.extrautils.*
import xyz.donot.quetzal.view.fragment.HelpFragment
import xyz.donot.quetzal.viewmodel.activity.MainViewModel
import xyz.donot.quetzal.viewmodel.adapter.MainTimeLineAdapter

class MainActivity : RxAppCompatActivity() {
  val REQUEST_WRITE_READ=0
  val twitter by lazy { getTwitterInstance() }
  val accountChanged by lazy { BehaviorSubject<Boolean>() }
    val viewModel by lazy { MainViewModel(applicationContext) }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (!haveToken()) {
      startActivity(intent<TwitterOauthActivity>())
      this.finish()
    } else {
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this@MainActivity, R.layout.activity_main)
        binding.viewModel = viewModel
        viewpager.adapter = MainTimeLineAdapter(supportFragmentManager)
        viewpager.offscreenPageLimit = 2

      if (!isConnected()) {
        showSnackbar(coordinatorLayout, R.string.description_a_network_error_occurred)
      }
      toolbar.apply {
        inflateMenu(R.menu.menu_main)
        setOnMenuItemClickListener {
          when(it.itemId){
            R.id.menu_search-> start<SearchSettingActivity>()
            R.id.menu_notification->start<NotificationActivity>()
          }
          true
        }
        setNavigationOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
      }

      design_navigation_view.setNavigationItemSelectedListener({
        if (isConnected()) {
          when (it.itemId) {
            R.id.my_profile -> {
                             startActivity(
                                     newIntent<UserActivity>(Bundle { putLong("user_id",getMyId()) }))
                              drawer_layout.closeDrawers()
                          }
            R.id.action_help -> {
                          HelpFragment().show(supportFragmentManager,"")
                        drawer_layout.closeDrawers()
                       }
            R.id.action_setting -> {

              start<SettingsActivity>()
              drawer_layout.closeDrawers()
            }
            R.id.action_account -> {
              startForResult<AccountSettingActivity>(0)
              drawer_layout.closeDrawers()
            }
            R.id.action_list -> {
              start<ListsActivity>(Bundle().apply { putLong("user_id",getMyId()) })
              drawer_layout.closeDrawers()
            }
            R.id.action_whats_new -> {
            onCustomTabEvent("http://donot.xyz/")
              drawer_layout.closeDrawers()
            }
          }
        }
        true
      })
      //set up header
      UsersObservable(twitter)
              .getMyUserInstance()
              .bindToLifecycle(this@MainActivity)
              .subscribe(object : TwitterUserSubscriber(applicationContext){
                override fun onUser(user: User) {
                  super.onUser(user)
                  Picasso.with(applicationContext).load(user.profileBannerIPadRetinaURL).placeholder(R.drawable.pugnotification_ic_placeholder).into(my_header)
                    Picasso.with(applicationContext).load(user.originalProfileImageURLHttps).transform(RoundCorner()).into(my_icon)
                  my_name_header.text= user.name
                  my_screen_name_header.text = "@${user.screenName}"
                }
              })



      //通知
        button_tweet.setOnClickListener(
              {
                 if( viewModel.statusSend.canExecute()){
                  val tObserver = TwitterUpdateObservable(applicationContext,twitter)
                  tObserver.updateStatusAsync(editText_status.editableText.toString())
                          .bindToLifecycle(this@MainActivity)
                          .subscribe(object : TwitterSubscriber(applicationContext) {
                            override fun onStatus(status: Status) {
                              editText_status.hideSoftKeyboard()
                              Snackbar.make(coordinatorLayout, "投稿しました", Snackbar.LENGTH_LONG).setAction("取り消す", {
                                tObserver.deleteStatusAsync(status.id).subscribe {
                                  toast("削除しました")
                                }
                              }).show()
                            }

                          })
                  editText_status.setText("")
              }})

        button_tweet.setOnLongClickListener({
            startActivity(intent<EditTweetActivity>())
            true
        })
    }
    accountChanged.subscribe {
      restart()
    }
    //パーミッション要求
    fromApi(23, true){
    val EX_WRITE=ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
    val LOCATION=ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    val EX_READ=ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
    if(!(EX_WRITE&&EX_READ&&LOCATION)){
      requestPermissions(
              arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE
              ,Manifest.permission.READ_EXTERNAL_STORAGE
              ,Manifest.permission.ACCESS_FINE_LOCATION)
              ,REQUEST_WRITE_READ)
    }
    }

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(resultCode== Activity.RESULT_OK){
      if(requestCode==0){
        accountChanged.onNext(true)
      }
    }
  }

    override fun onDestroy() {
        if (haveToken()) { viewModel.clean()}
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      if (requestCode == REQUEST_WRITE_READ) {
          if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          }
          if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
              toast("権限がないため画像の保存ができません")
          }
      }

  }
}


