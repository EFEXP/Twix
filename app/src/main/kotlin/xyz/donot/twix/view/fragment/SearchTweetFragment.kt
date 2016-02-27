package xyz.donot.twix.view.fragment

import twitter4j.Query
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance

class SearchTweetFragment(val query_txt:String):BaseFragment(){
  internal var query =Query(query_txt)
  val twitter by lazy { activity.getTwitterInstance() }
  override fun TimelineLoader() {
    TwitterObservable(twitter).getSearchAsync(query).subscribe {
      if(it.hasNext()){
        query=it.nextQuery()
      }
      it.tweets.forEach { mAdapter.add(it) }
    }
  }

}
