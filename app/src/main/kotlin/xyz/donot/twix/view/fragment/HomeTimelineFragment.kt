package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.OnDeleteEvent
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.Factory
import xyz.donot.twix.twitter.StreamType
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.bindToLifecycle

class HomeTimelineFragment : BaseFragment() {

  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    TwitterObservable(twitter)
      .getHomeTimelineAsync(paging)
      .bindToLifecycle(this@HomeTimelineFragment)
    .subscribe(object:
      TwitterSubscriber(){
      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
  Factory.getStreamObject(activity,twitter, StreamType.USER_STREAM).run()
    }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onEventMainThread(statusEvent: OnStatusEvent){
    data.addFirst(statusEvent.status)
    if(linerLayoutManger.findFirstCompletelyVisibleItemPosition()==0){recycler_view.smoothScrollToPosition(0)}
    mAdapter.notifyItemInserted(0)
  }


  @Subscribe
  fun onEvent(deleteEvent: OnDeleteEvent){
    data.filter { it.id==deleteEvent.component1().statusId }.mapNotNull { mAdapter.remove(it) }
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
