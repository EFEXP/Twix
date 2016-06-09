package xyz.donot.quetzal.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import twitter4j.Query
import xyz.donot.quetzal.util.extrautils.Bundle
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.view.fragment.SearchTweet
import xyz.donot.quetzal.view.fragment.TrendFragment
import xyz.donot.quetzal.view.fragment.UserList

class SearchAdapter(val query: Query, fm: FragmentManager) : FragmentPagerAdapter(fm) {
  override fun getItem(position: Int): Fragment {
    return when(position){
      0-> SearchTweet().apply { arguments=Bundle {  putByteArray("query_bundle",query.getSerialized()) } }
      1 -> object : UserList() {
        override fun loadMore() {
          twitterObservable.getUserSearchAsync(query.query, page).subscribe { mAdapter.addAll(it) }
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

