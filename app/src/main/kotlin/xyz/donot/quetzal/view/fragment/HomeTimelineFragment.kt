package xyz.donot.quetzal.view.fragment


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import twitter4j.Paging
import twitter4j.Status
import xyz.donot.quetzal.event.OnDeleteEvent
import xyz.donot.quetzal.event.OnExceptionEvent
import xyz.donot.quetzal.event.OnStatusEvent
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.twitter.StreamManager
import xyz.donot.quetzal.twitter.StreamType
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.bindToLifecycle

class HomeTimelineFragment : BaseFragment() {
  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    TwitterObservable(twitter)
      .getHomeTimelineAsync(paging)
      .bindToLifecycle(this@HomeTimelineFragment)
      .subscribe(object:
      TwitterSubscriber(){
      override fun onCompleted() {
      }

      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )

  }
  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onEventMainThread(statusEvent: OnStatusEvent){
    data.addFirst(statusEvent.status)
    val t=recycler_view.layoutManager as LinearLayoutManager
    if(t.findFirstCompletelyVisibleItemPosition()==0){recycler_view.scrollToPosition(0)}
    mAdapter.notifyItemInserted(0)
  }

  @Subscribe
  fun onEventMainThread(onExceptionEvent: OnExceptionEvent){
    StreamManager.Factory.getStreamObject(context,twitter, StreamType.USER_STREAM).run()
  }
  @Subscribe
  fun onEvent(deleteEvent: OnDeleteEvent){
    data.filter { it.id==deleteEvent.deletionNotice.statusId }.mapNotNull { mAdapter.remove(it) }
  }
  val eventBus by lazy { EventBus.getDefault() }
  override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
    eventBus.register(this)
  }
  override fun onDestroy() {
    super.onDestroy()
    eventBus.unregister(this)
  }


}
