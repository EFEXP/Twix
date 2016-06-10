package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import twitter4j.Query
import xyz.donot.quetzal.util.getDeserialized

class SearchTweet() : TimeLineFragment() {
   private  var query :Query?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query=arguments.getByteArray("query_bundle").getDeserialized<Query>()
    }
    override fun loadMore() {
    if(load.value){
   twitterObservable.getSearchAsync(query!!).subscribe {
    if(it.tweets.isNotEmpty()){
      if(it.hasNext()){
        query=it.nextQuery()
      }
      else{
        load.onNext(false)
      }
      mAdapter.addAll(it.tweets)

    }}

  } else{
    empty.onNext(true)
        load.onNext(false)
    }

    }
}


