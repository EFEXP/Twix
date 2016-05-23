package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import twitter4j.Query

class SearchTweet(): TimeLine(){
  internal var query :Query?=null
  internal var load =true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val query_txt:String=arguments.getString("query_txt")
      query=  Query("$query_txt -rt")
    }


    override fun loadMore() {
    query?.lang="ja"
    query?.resultType=Query.MIXED
    if(load){
   twitterObservable.getSearchAsync(query!!).subscribe {
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


