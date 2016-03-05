package xyz.donot.quetzal.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.quetzal.view.fragment.SearchTweet
import xyz.donot.quetzal.view.fragment.TrendFragment
import xyz.donot.quetzal.view.fragment.UsersWatcher

class SearchAdapter(val txt:String,fm: FragmentManager) : FragmentPagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when(position){
      0-> SearchTweet(txt)
      1->object : UsersWatcher(){
        override fun loadMore() {
          twitterObservable.getUserSearchAsync(txt,page).subscribe(userSubscriber)
        }
      }
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

