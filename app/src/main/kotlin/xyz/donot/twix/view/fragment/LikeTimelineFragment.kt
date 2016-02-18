package xyz.donot.twix.view.fragment

import android.os.Bundle
import android.view.View
import twitter4j.Paging
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance

class LikeTimelineFragment(val userId:Long): BaseFragment() {
  val twitter by lazy { activity.getTwitterInstance() }
  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    TwitterObservable(twitter).getFavoritesAsync(userId,paging).subscribe {
      mAdapter.add(it); }
  }
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

  }



}
