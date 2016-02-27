package xyz.donot.twix.view.fragment

import android.os.Bundle
import android.view.View
import twitter4j.Paging
import twitter4j.Status
import xyz.donot.twix.event.TwitterSubscriber
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance

class UserTimelineFragment(val userId:Long) : BaseFragment() {
  override fun TimelineLoader() {
    val twitter=activity.getTwitterInstance()
    val paging = Paging(page, 30)
    TwitterObservable(twitter).getUserTimelineAsync(userId,paging).subscribe (object:
      TwitterSubscriber(){
      override fun onStatus(status: Status) {
        mAdapter.add(status)
      }
    } )
    }


  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {


    }





}
