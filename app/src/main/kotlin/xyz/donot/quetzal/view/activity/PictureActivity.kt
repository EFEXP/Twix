package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.content_picture.*
import xyz.donot.quetzal.R
import xyz.donot.quetzal.view.adapter.PicturePagerAdapter
import xyz.donot.quetzal.view.fragment.PictureFragment

class PictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        toolbar.inflateMenu(R.menu.picture_menu)
        toolbar.setNavigationOnClickListener { finish() }
        val strings = intent.extras.getStringArrayList("picture_urls")
        val starts = intent.extras.getInt("starts_with",0)
        val pagerAdapter=PicturePagerAdapter(supportFragmentManager, strings)

        pager.adapter = pagerAdapter
        pager.currentItem=starts
        toolbar.setOnMenuItemClickListener {
        when(it.itemId){
          R.id.save_it->{
              val fragment=pagerAdapter.findFragmentByPosition(pager,pager.currentItem)
              if(fragment is PictureFragment){
                  fragment.SavePics()
              }
            true
          }
          else->throw IllegalStateException()
        }
      }
    }


}
