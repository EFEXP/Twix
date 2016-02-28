package xyz.donot.quetzal.view.fragment

import twitter4j.Paging
import twitter4j.Status
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.twitter.TwitterObservable

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