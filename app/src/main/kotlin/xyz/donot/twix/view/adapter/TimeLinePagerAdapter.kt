package xyz.donot.twix.view.adapter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.twix.util.getMyName
import xyz.donot.twix.view.fragment.FavTimelineFragment

import xyz.donot.twix.view.fragment.HomeTimelineFragment
import xyz.donot.twix.view.fragment.UserTimelineFragment

class TimeLinePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
          0->HomeTimelineFragment()
          1->UserTimelineFragment(getMyName())
          2->FavTimelineFragment(getMyName())
          else->FavTimelineFragment(getMyName())//throw IllegalStateException()
        }}


    override fun getPageTitle(position: Int): CharSequence {
        return when(position){
          0->"Home"
          1->"User"
          2->"Me"
          else->"Unknown"
    }}

    override fun getCount(): Int {
        return 3
    }
}
