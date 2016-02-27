package xyz.donot.twix.view.adapter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.twix.util.getMyId
import xyz.donot.twix.view.fragment.HomeTimelineFragment
import xyz.donot.twix.view.fragment.MentionFragment
import xyz.donot.twix.view.fragment.UserTimelineFragment

class TimeLinePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
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
}

