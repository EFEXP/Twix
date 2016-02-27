package xyz.donot.twix.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.content_picture.*
import org.greenrobot.eventbus.EventBus
import xyz.donot.twix.R
import xyz.donot.twix.event.OnSaveIt
import xyz.donot.twix.view.adapter.PicturePagerAdapter

class PictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        toolbar.inflateMenu(R.menu.picture_menu)
        toolbar.setNavigationOnClickListener { finish() }
        val strings = intent.extras.getStringArrayList("picture_urls")
        pager.adapter = PicturePagerAdapter(supportFragmentManager, strings)

      toolbar.setOnMenuItemClickListener {
        when(it.itemId){
          R.id.save_it->{
            EventBus.getDefault().post(OnSaveIt())
            true
          }
          else->throw IllegalStateException()
        }
      }
    }


}
