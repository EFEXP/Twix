package xyz.donot.quetzal.view.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.quetzal.view.fragment.PictureFragment
import java.util.*

class PicturePagerAdapter(fm: FragmentManager, var pictureUrls: ArrayList<String>) : FragmentPagerAdapter(fm) {

  override fun getItem(i: Int): Fragment {
    val p = PictureFragment()
    val bundle = Bundle()
    bundle.putString("url", pictureUrls[i])
    p.arguments = bundle
    return p
  }

  override fun getCount(): Int {
    return pictureUrls.count()
  }


  override fun getPageTitle(position: Int): CharSequence {
    return position.toString()
  }

}
