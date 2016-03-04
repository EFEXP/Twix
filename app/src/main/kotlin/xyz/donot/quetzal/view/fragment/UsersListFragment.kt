package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import twitter4j.UserList
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.adapter.UsersListAdapter
import xyz.donot.quetzal.view.listener.OnLoadMoreListener
import java.util.*

class UsersListFragment(val userId:Long) : RxFragment() {
  val twitter by lazy { activity.getTwitterInstance() }
  val data by lazy { LinkedList<UserList>() }
  val mAdapter by lazy { UsersListAdapter(context,data) }
  internal var cursor = -1L
  internal var load=true
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    val recycler=v.findViewById(R.id.base_recycler_view)as RecyclerView
    recycler.apply{
      itemAnimator= OvershootInRightAnimator(0.1f)
      adapter = AlphaInAnimationAdapter(mAdapter)
      layoutManager = LinearLayoutManager(activity)
      addOnScrollListener(object: OnLoadMoreListener(){
        override fun onScrolledToBottom() {
          TimelineLoader()
        }
      }
      )}
    TimelineLoader()
    return v
  }

  fun TimelineLoader(){
    if(userId==0L) {
      TwitterObservable(context,twitter).showOwnUsersList(getMyId(), cursor)
        .subscribe {
          result ->
          if (load) {
            result.forEach {
              mAdapter.add(it)
            }
          }
          if (result.hasNext()) {
            cursor = result.nextCursor
          } else {
            load = false
          }
        }
    }
    else{
      TwitterObservable(context,twitter).showUsersList(userId)
        .subscribe {
          result ->
          result.forEach {
            mAdapter.add(it)
          }
        }
    }
  }




}
