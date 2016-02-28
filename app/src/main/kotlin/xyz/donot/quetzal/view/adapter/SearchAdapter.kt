package xyz.donot.quetzal.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.quetzal.view.fragment.SearchTweetFragment
import xyz.donot.quetzal.view.fragment.SearchUserFragment
import xyz.donot.quetzal.view.fragment.TrendFragment

class SearchAdapter(val txt:String,fm: FragmentManager) : FragmentPagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when(position){
      0->SearchTweetFragment(txt)
      1->SearchUserFragment(txt)
      2->TrendFragment()
      else->throw  IllegalStateException()
    }}
  override fun getPageTitle(position: Int): CharSequence {
    return when(position){
      0->"Tweet"
      1->"User"
      2->"Trend"
      else->throw  IllegalStateException()
    }}

  override fun getCount(): Int {
    return 3
  }
}

