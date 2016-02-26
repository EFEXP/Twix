package xyz.donot.twix.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.content_picture.*
import org.greenrobot.eventbus.EventBus
import xyz.donot.twix.R
import xyz.donot.twix.view.adapter.PicturePagerAdapter

class PictureActivity : AppCompatActivity() {
  val eventBus by lazy { EventBus.getDefault() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        toolbar.inflateMenu(R.menu.picture_menu)
        toolbar.setNavigationOnClickListener { finish() }
        val strings = intent.extras.getStringArrayList("picture_urls")
        pager.adapter = PicturePagerAdapter(supportFragmentManager, strings)
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
