package xyz.donot.twix.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.twix.view.fragment.SearchTweetFragment
import xyz.donot.twix.view.fragment.SearchUserFragment
import xyz.donot.twix.view.fragment.TrendFragment

class SearchAdapter(val txt:String,fm: FragmentManager) : FragmentPagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when(position){
      0-> TrendFragment()
      1->SearchTweetFragment(txt)
      2->SearchUserFragment(txt)
      else->throw  IllegalStateException()
    }}
  override fun getPageTitle(position: Int): CharSequence {
    return when(position){
      0->"Trend"
      1->"Tweet"
      2->"User"
      else->throw  IllegalStateException()
    }}

  override fun getCount(): Int {
    return 3
  }
}

