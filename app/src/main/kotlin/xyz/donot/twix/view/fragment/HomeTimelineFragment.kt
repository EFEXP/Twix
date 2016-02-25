package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.view.View
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.OnDeleteEvent
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.Factory
import xyz.donot.twix.twitter.StreamType
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.bindToLifecycle
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.logd

class HomeTimelineFragment : BaseFragment() {


  val eventBus by lazy { EventBus.getDefault() }
  val twitter by lazy { activity.getTwitterInstance() }
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

  @Subscribe
  fun onEventMainThread(statusEvent: OnStatusEvent){
    logd("You got a message.", statusEvent.status.text)

    mAdapter.insert(statusEvent.status) }


  @Subscribe
  fun onEvent(deleteEvent: OnDeleteEvent){
    val statusData= data.filter { it.id==deleteEvent.component1().statusId }.first()
 mAdapter.remove(statusData)
  }
  override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
    eventBus.register(this)
  }
  override fun onDestroy() {
    super.onDestroy()
    eventBus.unregister(this)
  }
  override fun onPause() {
    super.onPause()
  }

  override fun onResume() {
    super.onResume()

  }
}
