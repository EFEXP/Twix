package xyz.donot.twix.view.fragment

import android.os.Bundle
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.OnDeleteEvent
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance

class UserTimelineFragment(val userId:Long) : BaseFragment() {
  override fun TimelineLoader() {
    val twitter=activity.getTwitterInstance()
    val paging = Paging(page, 30)
    TwitterObservable(twitter).getUserTimelineAsync(userId,paging).subscribe (object:
      TwitterSubscriber(){
      override fun onCompleted() {
        loadingDismiss()
      }
      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )
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
