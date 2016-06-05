package xyz.donot.quetzal.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import twitter4j.Query
import xyz.donot.quetzal.util.extrautils.Bundle
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.view.fragment.ImageSearchFragment
import xyz.donot.quetzal.view.fragment.SearchTweet
import xyz.donot.quetzal.view.fragment.TrendFragment
import xyz.donot.quetzal.view.fragment.UsersWatcher

class SearchAdapter(val query: Query, fm: FragmentManager) : FragmentPagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when(position){
      0-> SearchTweet().apply { arguments=Bundle {  putByteArray("query_bundle",query.getSerialized()) } }
      1->object : UsersWatcher(){
        override fun loadMore() {
          twitterObservable.getUserSearchAsync(query.query,page).subscribe(userSubscriber)
        }
      }
      2->TrendFragment()
      3->ImageSearchFragment().apply { arguments=Bundle {  putByteArray("query_bundle",query.getSerialized()) }
      }
      else->throw  IllegalStateException()
    }}
  override fun getPageTitle(position: Int): CharSequence {
    return when(position){
      0->"Tweet"
      1->"User"
      2->"Trend"
      3->"Search"
      else->throw  IllegalStateException()
    }}

  override fun getCount(): Int {
    return 4
  }
}

