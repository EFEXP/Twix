package xyz.donot.quetzal.view.adapter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.view.fragment.HomeTimelineFragment
import xyz.donot.quetzal.view.fragment.MentionFragment
import xyz.donot.quetzal.view.fragment.UserTimelineFragment

class TimeLinePagerAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position){
          0->Factory.home
          1->Factory.mention
          2->Factory.user
          else->throw  IllegalStateException()
        }}


    override fun getPageTitle(position: Int): CharSequence {
        return when(position){
          0->"Home"
          1->"Mention"
          2->"User"
          else->throw  IllegalStateException()
    }}

    override fun getCount(): Int {
        return 3
    }

  companion object Factory{
    val home:HomeTimelineFragment by  lazy { HomeTimelineFragment()  }
    val user by lazy { UserTimelineFragment(getMyId())}
    val mention by lazy { MentionFragment() }
  }
  fun  destroyAllItem() {
    for(i in 0..2){
    fm.beginTransaction().remove(this.getItem(i)).commitAllowingStateLoss()
  }}
}

