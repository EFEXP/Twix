package xyz.donot.twix.view.fragment

import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterObservable

class LikeTimelineFragment(val userId:Long): BaseFragment() {
  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    TwitterObservable(twitter).getFavoritesAsync(userId,paging).subscribe (object:
      TwitterSubscriber(){
      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )
  }
}
