package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.content_picture.*
import org.greenrobot.eventbus.EventBus
import xyz.donot.quetzal.R
import xyz.donot.quetzal.event.OnSaveIt
import xyz.donot.quetzal.view.adapter.PicturePagerAdapter

class PictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        toolbar.inflateMenu(R.menu.picture_menu)
        toolbar.setNavigationOnClickListener { finish() }
        val strings = intent.extras.getStringArrayList("picture_urls")
        val starts = intent.extras.getInt("starts_with",0)
        pager.adapter = PicturePagerAdapter(supportFragmentManager, strings)
        pager.currentItem=starts
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
