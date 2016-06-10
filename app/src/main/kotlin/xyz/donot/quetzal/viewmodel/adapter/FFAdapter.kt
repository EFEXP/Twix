package xyz.donot.quetzal.viewmodel.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.quetzal.view.fragment.FollowerFragment

class FFAdapter(val mUserId:Long,fm: FragmentManager) : FragmentPagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when(position){
      0-> FollowerFragment()
      .apply {
        arguments= Bundle().apply {
          putLong("userId",mUserId)
          putInt("mode",FollowerFragment.FRIEND)
        }
      }
      1-> FollowerFragment()
              .apply {
                arguments= Bundle().apply {
                  putLong("userId",mUserId)
                  putInt("mode",FollowerFragment.FOLLOWER)
                }
              }
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
