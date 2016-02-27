package xyz.donot.twix.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import twitter4j.Trend
import xyz.donot.twix.R
import xyz.donot.twix.twitter.TwitterTrendObservable
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.view.adapter.TrendAdapter
import java.util.*

class TrendFragment():RxFragment(){
   val twitter by lazy { context.getTwitterInstance() }
   var page : Int = 0
    get() {
      field++
      return field
    }
   val data by lazy { LinkedList<Trend>() }
   val  mAdapter by lazy { TrendAdapter(context, data) }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    val recycler=v.findViewById(R.id.recycler_view)as RecyclerView
    recycler.apply{
      itemAnimator= OvershootInRightAnimator(1f)
      adapter = AlphaInAnimationAdapter(mAdapter)
      layoutManager = LinearLayoutManager(context)
      TimelineLoader()
    }
    return v}

  fun TimelineLoader(){
    TwitterTrendObservable(twitter).getTrend().subscribe {
      it.forEach {
        mAdapter.add(it)
      }
    }

  }

}
