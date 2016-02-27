package xyz.donot.twix.twitter

import rx.Observable
import twitter4j.Trend
import twitter4j.Twitter
import xyz.donot.twix.util.basicNetworkTask

class TwitterTrendObservable(val twitter: Twitter){


  fun  getTrend():Observable<Array<Trend>>{
    return  Observable.create<Array<Trend>> {
      try{
        it.onNext(twitter.getPlaceTrends(23424856).trends)
        it.onCompleted()
      }
      catch(ex:Exception){
        it.onError(ex)
      }

    } .basicNetworkTask()
  }
}
