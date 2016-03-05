package xyz.donot.quetzal.view.fragment

import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import twitter4j.Paging
import xyz.donot.quetzal.event.OnStatusEvent
import xyz.donot.quetzal.util.bindToLifecycle

class HomeTimeLine(): TimeLine(){

  override fun loadMore() {
    twitterObservable.getHomeTimelineAsync(Paging(page))
      .bindToLifecycle(this@HomeTimeLine)
      .subscribe({mAdapter.addAll(it)})
  }
  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onEventMainThread(statusEvent: OnStatusEvent){
    data.add(0,statusEvent.status)
    val t=base_recycler_view.layoutManager as LinearLayoutManager
    if(t.findFirstCompletelyVisibleItemPosition()==0){base_recycler_view.scrollToPosition(0)}
    mAdapter.notifyItemInserted(0)
  }


}
