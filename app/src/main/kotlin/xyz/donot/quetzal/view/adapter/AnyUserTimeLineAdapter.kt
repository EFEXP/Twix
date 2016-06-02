package xyz.donot.quetzal.view.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import twitter4j.Paging
import xyz.donot.quetzal.view.fragment.TimeLine
import xyz.donot.quetzal.view.fragment.UserDetailFragment


class AnyUserTimeLineAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm)
{

var userId:Long=0

override fun getItem(position: Int): Fragment {
  return when(position){
    0-> {
      UserDetailFragment().apply {
        arguments=   Bundle().apply { putLong("userId",userId) }

      }}
    1->object :TimeLine(){
      override fun loadMore() {
        twitterObservable.getUserTimelineAsync(userId, Paging(page)).subscribe {
          mAdapter.addAll(it.toList())
        }
      }
    }
    2-> object : TimeLine(){
      override fun loadMore() {
        twitterObservable.getFavoritesAsync(userId, Paging(page)).subscribe {   mAdapter.addAll(it.toList()) } }
      }

    else->throw  IllegalStateException()
  }}



override fun getPageTitle(position: Int): CharSequence {
  return when(position){
    0->"Info"
    1->"User"
    2->"Like"
    else->throw IllegalStateException()
  }}

override fun getCount(): Int {
  return 3
}
}
