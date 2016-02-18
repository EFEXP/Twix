package xyz.donot.twix.twitter

import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.*
import xyz.donot.twix.util.basicNetworkTask
import xyz.donot.twix.util.logi

class TwitterObservable(val twitter : Twitter)
{

  fun getFavoritesAsync(userID:Long,paging: Paging): Observable<Status>
  {
    return  observable<Status> { subscriber ->
      try {
        val  statuses = twitter.getFavorites(userID, paging)
        statuses.withIndex().forEachIndexed { int, indexedValue ->
          subscriber.onNext(indexedValue.value)
          if(int==paging.count-1){
            logi("Loaded","${int}Tweets")
            subscriber.onCompleted()}
        }
      } catch (e: TwitterException) {
        logi("error",e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }

  fun getHomeTimelineAsync(paging: Paging): Observable<Status>
  {
    return  Observable.create<Status> { subscriber ->
      try {
        val  statuses = twitter.getHomeTimeline(paging)
        statuses.withIndex().forEachIndexed { int, indexedValue ->
          subscriber.onNext(indexedValue.value)
          if(int==paging.count-1){
            logi("Loaded","${int}Tweets")
            subscriber.onCompleted()}
        }
      } catch (e: TwitterException) {
        subscriber.onError(e)
      }

    }
      .basicNetworkTask()
  }

  fun getHomeTimelineListAsync(paging: Paging): Observable<ResponseList<Status>>
  {
    return  Observable.create<ResponseList<Status>> { subscriber ->
      try {
          subscriber.onNext(twitter.getHomeTimeline(paging))

      } catch (e: TwitterException) {
        logi("error",e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }


  fun getMentionsTimelineAsync(paging: Paging): Observable<Status>
  {
    return  Observable.create<Status> { subscriber ->
      try {
      val  statuses=  twitter.getMentionsTimeline(paging)
        statuses.withIndex().forEachIndexed { int, indexedValue ->
          subscriber.onNext(indexedValue.value)
          if(indexedValue.index==paging.count){subscriber.onCompleted()}
        }
      } catch (e: TwitterException) {
        logi("error",e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }

  fun getUserTimelineAsync(userID:Long?,paging: Paging): Observable<Status>
  {
    return  Observable.create<Status> { subscriber ->
      try {
        val statuses=
          if (userID == null) {
            twitter.getUserTimeline(paging)
          }
          else{
            twitter.getUserTimeline(userID,paging)
          }
        statuses.withIndex().forEachIndexed { int, indexedValue ->
          subscriber.onNext(indexedValue.value)
          if(int-1==paging.count-1){
            logi("Loaded","${int}Tweets")
            subscriber.onCompleted()}
        }
      } catch (e: TwitterException) {
        logi("error",e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }



}
