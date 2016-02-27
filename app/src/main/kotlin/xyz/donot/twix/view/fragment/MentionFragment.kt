package xyz.donot.twix.view.fragment


import android.os.Bundle
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterObservable


class MentionFragment() : BaseFragment() {
  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    TwitterObservable(twitter).getMentionsTimelineAsync(paging).subscribe (object:
      TwitterSubscriber(){
      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )
  }
  @Subscribe
  fun onEventMainThread(statusEvent: OnStatusEvent){
    mAdapter.insert(statusEvent.status) }

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
