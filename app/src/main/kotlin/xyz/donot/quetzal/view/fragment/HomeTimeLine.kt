package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import rx.android.schedulers.AndroidSchedulers
import twitter4j.Paging
import xyz.donot.quetzal.util.bindToLifecycle
import xyz.donot.quetzal.util.extrautils.isConnected

class HomeTimeLine : TimeLineFragment() {
       override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tsm.statusSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mAdapter.setItemInsertListener {
                    val t=base_recycler_view.recyclerView.layoutManager as LinearLayoutManager
                    if(t.findFirstCompletelyVisibleItemPosition()==0){
                        base_recycler_view.scrollToPosition(0)
                    }
                    }
                mAdapter.insert(it,0)
            }
  }

  override fun loadMore() {
      if (!context.isConnected())
      {
          mAdapter.pauseMore()
      }
    twitterObservable.getHomeTimelineAsync(Paging(page))
      .bindToLifecycle(this@HomeTimeLine)
      .subscribe{
          mAdapter.addAll(it)
      }
  }




}
