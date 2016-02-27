package xyz.donot.twix.view.fragment

import twitter4j.Query
import xyz.donot.twix.twitter.TwitterObservable

class SearchTweetFragment(val query_txt:String):BaseFragment(){
  internal var query =Query("$query_txt -rt")
  override fun TimelineLoader() {
    TwitterObservable(twitter).getSearchAsync(query).subscribe {
      if(it.hasNext()){
        query=it.nextQuery()
      }
      it.tweets.forEach { mAdapter.add(it) }
    }
  }

}
