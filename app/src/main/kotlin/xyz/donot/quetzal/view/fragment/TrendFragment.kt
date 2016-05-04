package xyz.donot.quetzal.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxDialogFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import twitter4j.Trend
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterTrendObservable
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.activity.SearchActivity
import xyz.donot.quetzal.view.adapter.BasicRecyclerAdapter
import xyz.donot.quetzal.view.adapter.TrendAdapter
import java.util.*

class TrendFragment():RxDialogFragment(){
   val twitter by lazy {getTwitterInstance() }

   val data by lazy { LinkedList<Trend>() }
   val  mAdapter by lazy { TrendAdapter(context, data) }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
    val recycler=v.findViewById(R.id.base_recycler_view)as RecyclerView
    recycler.apply{
      itemAnimator= OvershootInRightAnimator(1f)
      adapter = AlphaInAnimationAdapter(mAdapter)
      mAdapter.setOnItemClickListener(object:BasicRecyclerAdapter.OnItemClickListener<TrendAdapter.ViewHolder,Trend>{
        override fun onItemClick(adapter: BasicRecyclerAdapter<TrendAdapter.ViewHolder, Trend>, position: Int, item: Trend) {
          this@TrendFragment.startActivity(Intent(context, SearchActivity::class.java).putExtra("query_txt",item.query))
        }
      })

      layoutManager = LinearLayoutManager(context)
      TimelineLoader() }
    return v}

  fun TimelineLoader(){
    TwitterTrendObservable(context,twitter).getTrend().subscribe {
      it.forEach {
        mAdapter.add(it)
      }
    }

  }

}
