package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import twitter4j.UserList
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.adapter.UsersListAdapter
import java.util.*

class UsersListFragment: RxFragment() {


  val twitter by lazy {getTwitterInstance() }
  val data by lazy { LinkedList<UserList>() }
  val mAdapter by lazy { UsersListAdapter(context) }
  internal var cursor = -1L
  internal var load=true
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       base_recycler_view.apply {
           setItemAnimator(OvershootInRightAnimator(0.1f))
           adapter = AlphaInAnimationAdapter(mAdapter)
           setLayoutManager(LinearLayoutManager(activity))
           mAdapter.setMore(R.layout.item_loadmore, { TimelineLoader()})
       }
        TimelineLoader()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    return v
  }

  fun TimelineLoader(){
    val userId=  arguments.getLong("userId")
    if(userId==0L) {
      TwitterObservable(context,twitter).showOwnUsersList(getMyId(), cursor)
        .subscribe {
          result ->
          if (load) {
            mAdapter.addAll(result)
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
            mAdapter.addAll(result)
        }
    }
  }




}
