package xyz.donot.twix.view.fragment


import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance


class MentionFragment() : BaseFragment() {
  val twitter by lazy { activity.getTwitterInstance() }
  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    TwitterObservable(twitter).getMentionsTimelineAsync(paging).subscribe (object:
      TwitterSubscriber(){
      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )
  }
}
