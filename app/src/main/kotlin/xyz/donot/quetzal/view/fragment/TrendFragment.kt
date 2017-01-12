package xyz.donot.quetzal.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import twitter4j.Trend
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterTrendObservable
import xyz.donot.quetzal.view.activity.EditTweetActivity
import xyz.donot.quetzal.view.activity.SearchActivity
import xyz.donot.quetzal.viewmodel.adapter.TrendAdapter

class TrendFragment : BaseRecyclerFragment<Trend, TrendAdapter>() {
  override fun setUpRecycler() {
    base_recycler_view.setLayoutManager(LinearLayoutManager(context))
    mAdapter.setOnItemClickListener {
      val item = mAdapter.getItem(it)
      if (activity is SearchActivity) {
        this@TrendFragment.startActivity(Intent(context, SearchActivity::class.java).putExtra("query_txt", item.query))
      } else if (activity is EditTweetActivity) {
        (activity as EditTweetActivity).addTrendHashtag(item.name)
        this@TrendFragment.dismiss()
      }
    }
  }

  override fun loadMore() {
    TwitterTrendObservable(context,twitter).getTrend().subscribe {
      mAdapter.addAll(it)
    }
  }

  override val mAdapter: TrendAdapter by lazy { TrendAdapter(context) }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_timeline_base, container, false)
  }

}
