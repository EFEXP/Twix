package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.content_picture.*
import twitter4j.Status
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.getDeserialized
import xyz.donot.quetzal.util.getExpandedText
import xyz.donot.quetzal.util.getImageUrls
import xyz.donot.quetzal.view.fragment.PictureFragment
import xyz.donot.quetzal.viewmodel.adapter.PicturePagerAdapter

class PictureActivity : AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{onBackPressed()}
            R.id.save_it->{
                val fragment=(pager.adapter as PicturePagerAdapter).findFragmentByPosition(pager,pager.currentItem)
                if(fragment is PictureFragment){
                    fragment.SavePics()
                }
            }
            else->throw IllegalStateException()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeButtonEnabled(true);
         val starts = intent.extras.getInt("starts_with",0)
        val strings = intent.extras.getStringArrayList("picture_urls")
        val status :Status?= if(intent.hasExtra("status")){
            intent.extras.getByteArray("status").getDeserialized<Status>()
        }else{
            null
        }
        val pagerAdapter=if(status!=null&&strings==null){
            val imageUrls=getImageUrls(status)
            status_text.text = getExpandedText(status)
            pager.offscreenPageLimit=imageUrls.count()
            PicturePagerAdapter(supportFragmentManager,imageUrls)
        }else{
            BottomSheetBehavior.from(bottom_sheet).state=BottomSheetBehavior.STATE_HIDDEN
            pager.offscreenPageLimit=1
            PicturePagerAdapter(supportFragmentManager,strings)
        }
        pager.adapter = pagerAdapter
        pager.currentItem=starts

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
      menuInflater.inflate(R.menu.picture_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }


}
