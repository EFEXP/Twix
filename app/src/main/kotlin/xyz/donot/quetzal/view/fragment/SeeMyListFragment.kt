package xyz.donot.quetzal.view.fragment

import twitter4j.Paging
import twitter4j.Status
import xyz.donot.quetzal.event.TwitterSubscriber
import xyz.donot.quetzal.twitter.TwitterObservable

class SeeMyListFragment(val listId:Long): BaseFragment() {
  override fun TimelineLoader() {
    val paging = Paging(page, 30)
    TwitterObservable(twitter).getMyListAsync(listId,paging).subscribe (object:
      TwitterSubscriber(activity){
      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )
  }
}
