package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import rx.android.schedulers.AndroidSchedulers
import twitter4j.Paging
import xyz.donot.quetzal.util.bindToLifecycle

class HomeTimeLine(): TimeLine(){
       override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tsm.statusSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
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
