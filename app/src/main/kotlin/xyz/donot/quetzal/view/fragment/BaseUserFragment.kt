package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.adapter.UsersAdapter
import xyz.donot.quetzal.view.listener.OnLoadMoreListener
import java.util.*

abstract class BaseUserFragment : RxFragment() {
  open var page : Int = 0
    get() {
      field++
      return field
    }
  open val data by lazy { LinkedList<User>() }
  open val  mAdapter by lazy { UsersAdapter(context,data) }
  open val  twitter by lazy {context.getTwitterInstance() }
  open var swipeLayout : SwipeRefreshLayout?=null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    val recycler=v.findViewById(R.id.recycler_view)as RecyclerView
    swipeLayout=v.findViewById(R.id.swipe_layout)as SwipeRefreshLayout
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
    swipeLayout?.setOnRefreshListener {
      refreshTimeline()
    }

    return v}

  fun refreshTimeline() {
    page=0
    mAdapter.clear()
    TimelineLoader()
    swipeLayout?.isRefreshing=false
  }
  abstract fun TimelineLoader()


}
