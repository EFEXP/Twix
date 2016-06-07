package xyz.donot.quetzal.view.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import twitter4j.Paging
import twitter4j.Query
import twitter4j.User
import xyz.donot.quetzal.util.extrautils.Bundle
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.view.fragment.ImageSearchFragment
import xyz.donot.quetzal.view.fragment.TimeLine
import xyz.donot.quetzal.view.fragment.UserDetailFragment


class AnyUserTimeLineAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm)
{
var user: User?=null
override fun getItem(position: Int): Fragment {
  return when(position){
    0-> {
      UserDetailFragment().apply {
        arguments=   Bundle().apply { putLong("userId",user!!.id) }

      }}
    1->object :TimeLine(){
      override fun loadMore() {
        twitterObservable.getUserTimelineAsync(user!!.id, Paging(page)).subscribe {
          mAdapter.addAll(it.toList())
        }
      }
    }
    2-> object : TimeLine(){
      override fun loadMore() {
        twitterObservable.getFavoritesAsync(user!!.id, Paging(page)).subscribe {   mAdapter.addAll(it.toList()) } }
      }
     3->{
      val query=Query()
       query.query="from:${user!!.screenName}"
       ImageSearchFragment().apply { arguments= Bundle { putByteArray("query_bundle", query.getSerialized()) }}
     }

    else->throw  IllegalStateException()
  }}



override fun getPageTitle(position: Int): CharSequence {
  return when(position){
    0->"Info"
    1->"User"
    2->"Like"
    3->"Media"
    else->throw IllegalStateException()
  }}

override fun getCount(): Int {
  return 4
}
}
