package xyz.donot.quetzal.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_header.*
import rx.android.schedulers.AndroidSchedulers
import twitter4j.Status
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.event.TwitterUserSubscriber
import xyz.donot.quetzal.model.StreamType
import xyz.donot.quetzal.model.TwitterStream
import xyz.donot.quetzal.notification.NotificationWrapper
import xyz.donot.quetzal.twitter.TwitterUpdateObservable
import xyz.donot.quetzal.twitter.UsersObservable
import xyz.donot.quetzal.util.*
import xyz.donot.quetzal.util.extrautils.*
import xyz.donot.quetzal.view.fragment.HelpFragment

class MainActivity : RxAppCompatActivity() {
  val REQUEST_WRITE_READ=0
  val twitter by lazy { getTwitterInstance() }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (!haveToken()) {
      startActivity(intent<TwitterOauthActivity>())
      finish()
    } else {
      setContentView(R.layout.activity_main)
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
              start<AccountSettingActivity>()
              drawer_layout.closeDrawers()
            }
            R.id.action_list -> {
              start<ListsActivity>(Bundle().apply { putLong("user_id",getMyId()) })
              drawer_layout.closeDrawers()
            }
            R.id.action_whats_new -> {
            onCustomTabEvent("http://www.latex-cmd.com/index.html#equation")
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
                  Picasso.with(applicationContext).load(user.profileBannerIPadRetinaURL).into(my_header)
                  Picasso.with(applicationContext).load(user.originalProfileImageURLHttps).transform(RoundCorner()).into(my_icon)
                  my_name_header.text="${user.name}"
                  my_screen_name_header.text = "@${user.screenName}"
                }
              })
      button_tweet.setOnLongClickListener {
        start<EditTweetActivity>()
        true
      }

      //通知
      val stream=  TwitterStream(applicationContext).run(StreamType.USER_STREAM).apply {
        isConnected.observeOn(AndroidSchedulers.mainThread()).subscribe {
          if(it){
            toast("ストリームに接続しました")
            connect_stream.setImageResource(R.drawable.ic_cloud_white_24dp)
            connect_stream.tag=true
          }
          else if(isConnected.hasValue()){
            toast("ストリームから切断されました")
            connect_stream.setImageResource(R.drawable.ic_cloud_off_white_24dp)
            connect_stream.tag=false
          }
        }
        statusSubject.subscribe {
          if(isMentionToMe(it)){
            if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notifications",false))
            {
              NotificationWrapper(applicationContext).replyNotification(it)
            }
          }
        }
      }
      connect_stream.setOnClickListener {
        val boo=connect_stream.tag
        if(connect_stream.tag!=null&&boo is Boolean){
          if(boo){

          }
          else{
            stream.run(StreamType.USER_STREAM)
          }
        }
      }
      button_tweet.setOnClickListener(
              {
                if (!editText_status.editableText.isNullOrBlank()&&editText_status.text.count()<=140) {
                  val tObserver = TwitterUpdateObservable(applicationContext,twitter);
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
                }
              })
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



  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if(requestCode==REQUEST_WRITE_READ){
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      }
      if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
        toast("権限がないため画像の保存ができません")
      }
    }
  }



}