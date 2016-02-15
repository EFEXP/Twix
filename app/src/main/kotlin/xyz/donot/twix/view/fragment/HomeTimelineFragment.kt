package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.view.View
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import twitter4j.Paging
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.twitter.StreamManager
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.twitter.USER_STREAM
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.logd

class HomeTimelineFragment : BaseFragment() {
 val eventBus by lazy { EventBus.getDefault() }
  override fun TimelineLoader() {
    val twitter=activity.getTwitterInstance()
    val paging = Paging(page, 30)
    page++
    TwitterObservable(twitter).getHomeTimelineAsync(paging).subscribe {
      data.add(it);
      mAdapter.notifyItemChanged(mAdapter.itemCount) }
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    val twitter=activity.getTwitterInstance()
      TwitterObservable(twitter).getHomeTimelineAsync(Paging(1)).subscribe {
        data.add(it)
        mAdapter.notifyItemChanged(mAdapter.itemCount) }
        page++
   StreamManager(twitter,USER_STREAM).run()
    }

  @Subscribe
  fun onEventMainThread(statusEvent: OnStatusEvent){
    logd("Got a message", statusEvent.status.text)
    data.addFirst(statusEvent.status)
    mAdapter.
    mAdapter.notifyItemInserted(0)

    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
      eventBus.register(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    eventBus.unregister(this)
  }


}
