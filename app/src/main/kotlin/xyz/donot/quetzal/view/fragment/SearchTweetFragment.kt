package xyz.donot.quetzal.view.fragment

import twitter4j.Query
import xyz.donot.quetzal.twitter.TwitterObservable

class SearchTweetFragment(val query_txt:String):BaseFragment(){
  internal var query =Query("$query_txt -rt")
  internal var load =true
  override fun TimelineLoader() {
    if(load){
    TwitterObservable(twitter).getSearchAsync(query).subscribe {
      if(it.hasNext()){
        query=it.nextQuery()
      }
      else{
        load=false
      }
      it.tweets.forEach { mAdapter.add(it) }
    }
  }}
}


