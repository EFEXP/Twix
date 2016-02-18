package xyz.donot.twix.view.adapter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.twix.util.getMyId
import xyz.donot.twix.view.fragment.HomeTimelineFragment
import xyz.donot.twix.view.fragment.LikeTimelineFragment
import xyz.donot.twix.view.fragment.UserTimelineFragment

class TimeLinePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position){
          0->Factory.home?:throw UnknownError()
          1->Factory.user
          2->Factory.like
          else->throw  IllegalStateException()
        }}


    override fun getPageTitle(position: Int): CharSequence {
        return when(position){
          0->"Home"
          1->"User"
          2->"Like"
          else->throw  IllegalStateException()
    }}

    override fun getCount(): Int {
        return 3
    }
  object Factory{
    val home:HomeTimelineFragment? = null
      get(){ return field?: HomeTimelineFragment() }
    val user by lazy { UserTimelineFragment(getMyId())}
    val like by lazy { LikeTimelineFragment(getMyId()) }
  }
}
