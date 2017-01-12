package xyz.donot.quetzal.viewmodel.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import twitter4j.Paging
import twitter4j.User
import xyz.donot.quetzal.util.extrautils.Bundle
import xyz.donot.quetzal.util.extrautils.toast
import xyz.donot.quetzal.view.fragment.TimeLineFragment
import xyz.donot.quetzal.view.fragment.UserDetailFragment


class AnyUserTimeLineAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm)
{
var user: User?=null
override fun getItem(position: Int): Fragment {
  return when(position){
    0->
    {
      val fragment=UserDetailFragment()
      fragment.arguments= Bundle { putLong("userId",user!!.id)  }
       fragment
    }


    1 ->  {
      val fragment=UserTimeLine(user!!)
      fragment.arguments= Bundle { putLong("userId",user!!.id)  }
      fragment
    }

    2 ->
    {
      val fragment=UserTimeLine2(user!!)
      fragment.arguments= Bundle { putLong("userId",user!!.id)  }
       fragment
    }

    /* 3->{
       val fragment=ImageSearchFragment()
       val query=Query()
       query.query="from:${user!!.screenName}"
       fragment.arguments= Bundle {putByteArray("query_bundle", query.getSerialized())}
        fragment
     }*/

    else->throw  IllegalStateException()
  }}
class UserTimeLine(val user:User) :TimeLineFragment(){
  override fun loadMore() {
    twitterObservable.getUserTimelineAsync(user.id, Paging(page)).subscribe {
      if(it.isNotEmpty()){
        mAdapter.addAll(it.toList())}
      else{
        empty.onNext(true)
      }
    }
  }
  }

  class UserTimeLine2(val user:User) :TimeLineFragment(){
    override fun loadMore() {twitterObservable.getFavoritesAsync(user.id, Paging(page)).subscribe( { if(it.isNotEmpty()){
      mAdapter.addAll(it.toList())}
    else{
      empty.onNext(true)
    } } ,{toast("Error")})}
  }



override fun getPageTitle(position: Int): CharSequence {
  return when(position){
    0->"Info"
    1->"User"
    2->"Like"
   // 3->"Media"
    else->throw IllegalStateException()
  }}

override fun getCount(): Int {
  return 3
}


}
