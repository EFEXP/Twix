package xyz.donot.twix.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.content_picture.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import xyz.donot.twix.R
import xyz.donot.twix.event.OnFlickedEvent
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.util.logd
import xyz.donot.twix.view.adapter.PicturePagerAdapter

class PictureActivity : AppCompatActivity() {
  val eventBus by lazy { EventBus.getDefault() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val strings = intent.extras.getStringArrayList("picture_urls")
        pager.adapter = PicturePagerAdapter(supportFragmentManager, strings)
    }
  @Subscribe
  fun onEventMainThread(flickedEvent: OnFlickedEvent){
    finish()
  }
  override fun onPause() {
    super.onPause()
    eventBus.unregister(this)

  }

  override fun onResume() {
    super.onResume()
    eventBus.register(this)
  }

}
