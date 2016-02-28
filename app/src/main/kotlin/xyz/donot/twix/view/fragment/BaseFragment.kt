package xyz.donot.twix.view.fragment


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
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.view.adapter.StatusAdapter
import xyz.donot.twix.view.listener.OnLoadMoreListener
import java.util.*

abstract class BaseFragment : RxFragment() {
  open val twitter by lazy {context.getTwitterInstance()}
    open var page : Int = 0
      get() {
        field++
        return field
      }
    open val data by lazy { LinkedList<Status>() }
    open val  mAdapter by lazy { StatusAdapter(activity, data) }
    open var swipeLayout :SwipeRefreshLayout?=null
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    val recycler=v.findViewById(R.id.recycler_view)as RecyclerView
    swipeLayout=v.findViewById(R.id.swipe_layout)as SwipeRefreshLayout
    recycler.apply{
      layoutManager = LinearLayoutManager(context)
      itemAnimator= OvershootInRightAnimator(1f)
      itemAnimator.addDuration = 500
      itemAnimator.removeDuration = 1000
      itemAnimator.moveDuration = 1000
      itemAnimator.changeDuration =0
      adapter = AlphaInAnimationAdapter(mAdapter)
      addOnScrollListener(object: OnLoadMoreListener(){
        override fun onScrolledToBottom() {
          TimelineLoader()
        }
      }
      )
      swipeLayout?.isRefreshing=true
      TimelineLoader()
    }
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

