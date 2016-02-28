package xyz.donot.quetzal.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.quetzal.view.fragment.FollowerFragment

class FFAdapter(val userId:Long,fm: FragmentManager) : FragmentPagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when(position){
      0-> FollowerFragment(userId, FollowerFragment.Mode.Friend)
      1-> FollowerFragment(userId, FollowerFragment.Mode.Follower)
      else->throw  IllegalStateException()
    }}


  override fun getPageTitle(position: Int): CharSequence {
    return when(position){
      0->"Friend"
      1->"Follower"

      else->throw  IllegalStateException()
    }}

  override fun getCount(): Int {
    return 2
  }
}
