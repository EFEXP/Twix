package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import rx.android.schedulers.AndroidSchedulers
import twitter4j.Paging
import xyz.donot.quetzal.util.bindToLifecycle
import xyz.donot.quetzal.view.customview.ItemInsertListener

class HomeTimeLine(): TimeLine(){
       override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tsm.statusSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mAdapter.setItemInsertListener(object : ItemInsertListener {
                    override fun itemInserted(index: Int) {
                        base_recycler_view.scrollToPosition(0)
                    }
                })
                mAdapter.insert(it,0)
            }
  }

  override fun loadMore() {
    twitterObservable.getHomeTimelineAsync(Paging(page))
      .bindToLifecycle(this@HomeTimeLine)
      .subscribe{
          mAdapter.addAll(it)
      }
  }




}
