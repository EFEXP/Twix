package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxDialogFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import org.greenrobot.eventbus.EventBus
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.extrautils.hide
import xyz.donot.quetzal.util.extrautils.show
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.listener.OnLoadMoreListener

abstract class PlainFragment<L,T:RecyclerView.Adapter<X>,X:RecyclerView.ViewHolder>:RxDialogFragment()
{
  val twitterObservable : TwitterObservable by lazy { TwitterObservable(context,twitter) }
  val twitter by lazy {getTwitterInstance()}
  abstract val data:MutableList<L>
  abstract val  mAdapter : T
  abstract fun loadMore()
  var page : Int = 0
    get() {
      field++
      return field
    }

  val eventBus by lazy { EventBus.getDefault() }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    val recycler=v.findViewById(R.id.base_recycler_view)as RecyclerView
    val swipeLayout=v.findViewById(R.id.swipe_layout)as SwipeRefreshLayout
    recycler.apply{
      itemAnimator= OvershootInRightAnimator(0.1f)
      adapter = AlphaInAnimationAdapter(mAdapter)
      layoutManager = LinearLayoutManager(activity)
      addOnScrollListener(object: OnLoadMoreListener(){
        override fun onScrolledToBottom() {
          loadMore()
        }
      }
      )}

    loadMore()
    swipeLayout.setOnRefreshListener { reload(swipeLayout) }
    return v}
    fun reload(sl:SwipeRefreshLayout){
      progress_bar_load.show()
   page=0
    data.clear()
    mAdapter.notifyDataSetChanged()
    loadMore()
    sl.isRefreshing=false
      progress_bar_load.hide()
  }

}
