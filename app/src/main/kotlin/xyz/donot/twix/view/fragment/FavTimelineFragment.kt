package xyz.donot.twix.view.fragment

import android.os.Bundle
import android.view.View
import twitter4j.Paging
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance

class FavTimelineFragment(val userName:String): BaseFragment() {
  override fun TimelineLoader() {
    val twitter=activity.getTwitterInstance()
    val paging = Paging(page, 30)
    page++
    TwitterObservable(twitter).getFavoritesAsync(userName,paging).subscribe {
      data.add(it);
      mAdapter.notifyItemChanged(mAdapter.itemCount) }
  }
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    val twitter=activity.getTwitterInstance()
    TwitterObservable(twitter).getFavoritesAsync(userName,Paging(1)).subscribe {
      data.add(it);
      mAdapter.notifyItemChanged(mAdapter.itemCount) }
    page++
  }



}
