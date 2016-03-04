package xyz.donot.quetzal.view.fragment

import twitter4j.Query

class SearchTweet(val query_txt:String): TimeLine(){
  internal var query =Query("$query_txt -rt")
  internal var load =true
  override fun loadMore() {
    query.lang="ja"
    query.resultType=Query.MIXED
    if(load){
   twitterObservable.getSearchAsync(query).subscribe {
      if(it.hasNext()){
        query=it.nextQuery()
      }
      else{
        load=false
      }
       mAdapter.addAll(it.tweets)
    }
  }}
}


