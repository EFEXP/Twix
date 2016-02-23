package xyz.donot.twix.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import xyz.donot.twix.view.fragment.LikeTimelineFragment
import xyz.donot.twix.view.fragment.UserTimelineFragment

 class AnyUserTimeLineAdapter(fm: FragmentManager,val userId:Long) : FragmentPagerAdapter(fm)
{
override fun getItem(position: Int): Fragment {
  return when(position){
    0->UserTimelineFragment(userId)//AnyUserTimeLineFactory.user
    1-> LikeTimelineFragment(userId) //.like
    else->throw  IllegalStateException()
  }}


override fun getPageTitle(position: Int): CharSequence {
  return when(position){
    0->"User"
    1->"Like"
    else->throw IllegalStateException()
  }}

override fun getCount(): Int {
  return 2
}
}
object AnyUserTimeLineFactory{
 // var tabs:AnyUserTimeLineAdapter?=null
// fun getAnyUserTabs(fragmentManager: FragmentManager,long: Long):AnyUserTimeLineAdapter {
 //  if (tabs == null) {
 //    tabs= AnyUserTimeLineAdapter(fragmentManager,long)
 //  }
  // return tabs?:throw Exception()
// }
 // val user by lazy { UserTimelineFragment() }
 // val like by lazy { LikeTimelineFragment() }
}
