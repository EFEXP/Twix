package xyz.donot.quetzal.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxDialogFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterTrendObservable
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.activity.EditTweetActivity
import xyz.donot.quetzal.view.activity.SearchActivity
import xyz.donot.quetzal.view.adapter.TrendAdapter

class TrendFragment():RxDialogFragment(){
   val twitter by lazy {getTwitterInstance() }


   val  mAdapter by lazy { TrendAdapter(context) }
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    base_recycler_view.apply{
      setItemAnimator(OvershootInRightAnimator(1f))
      adapter = AlphaInAnimationAdapter(mAdapter)
      mAdapter.setOnItemClickListener {
        val item=mAdapter.getItem(it)
        if(activity is SearchActivity){
          this@TrendFragment.startActivity(Intent(context, SearchActivity::class.java).putExtra("query_txt",item.query))
        }
        else if(activity is EditTweetActivity){
          (activity as EditTweetActivity).addTrendHashtag(item.name)
          this@TrendFragment.dismiss()
        }
      }
      base_recycler_view.setLayoutManager(LinearLayoutManager(context))
      TimelineLoader() }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_timeline_base, container, false)}

  fun TimelineLoader(){
    TwitterTrendObservable(context,twitter).getTrend().subscribe {
     mAdapter.addAll(it)
    }

  }

}
