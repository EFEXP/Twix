package xyz.donot.twix.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.twix.view.fragment.SearchTweetFragment
import xyz.donot.twix.view.fragment.SearchUserFragment

class SearchAdapter(val txt:String,fm: FragmentManager) : FragmentPagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when(position){
      0->SearchTweetFragment(txt)
      1-> SearchUserFragment(txt)
      else->throw  IllegalStateException()
    }}


  override fun getPageTitle(position: Int): CharSequence {
    return when(position){
      0->"Tweet"
      1->"User"

      else->throw  IllegalStateException()
    }}

  override fun getCount(): Int {
    return 2
  }
}
