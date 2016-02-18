package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.StreamManager
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.twitter.USER_STREAM
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.logd

class HomeTimelineFragment : BaseFragment() {
 val eventBus by lazy { EventBus.getDefault() }
  val twitter by lazy { activity.getTwitterInstance() }
  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    val observableTweet=TwitterObservable(twitter).getHomeTimelineAsync(paging)
    observableTweet.subscribe(object :TwitterSubscriber(){
      override fun onLoaded() {

        enableLoadMore()
      }
      override fun onStatus(status: Status) { mAdapter.add(status) } })
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
   StreamManager.getStreamObject(twitter,USER_STREAM).run()
    }

  @Subscribe
  fun onEventMainThread(statusEvent: OnStatusEvent){
    logd("You got a message.", statusEvent.status.text)
  Handler(Looper.getMainLooper()).post {
    mAdapter.insert(statusEvent.status) }
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
