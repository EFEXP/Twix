package xyz.donot.quetzal.viewmodel.adapter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import xyz.donot.quetzal.view.customview.DynamicViewPager
import xyz.donot.quetzal.view.fragment.HomeTimeLine

class TimeLinePagerAdapter(val fm: FragmentManager) : DynamicViewPager(fm) {


    override fun getItem(position: Int): Fragment {
        return when(position){
          0->Factory.home
          else->throw  IllegalStateException()
        }}


    override fun getPageTitle(position: Int): CharSequence {
        return when(position){
          0->"Home"
          else->throw  IllegalStateException()
    }}

    override fun getCount(): Int {
        return 1
    }

 private  object Factory{
    val home by  lazy {HomeTimeLine()}
  }

}

