package xyz.donot.twix.view.fragment

import android.os.Bundle
import android.view.View
import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance

class LikeTimelineFragment(val userId:Long): BaseFragment() {
  val twitter by lazy { activity.getTwitterInstance() }
  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    TwitterObservable(twitter).getFavoritesAsync(userId,paging).subscribe (object:
      TwitterSubscriber(){
      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )
  }
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

  }



}
