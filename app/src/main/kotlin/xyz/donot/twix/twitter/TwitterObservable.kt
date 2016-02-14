package xyz.donot.twix.twitter

import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.*
import xyz.donot.twix.util.basicNetworkTask

class TwitterObservable(val twitter : Twitter)
{

  fun getFavoritesAsync(screenName:String?,paging: Paging): Observable<Status>
  {
    return  observable<Status> { subscriber ->
      try {
        val  statuses = twitter.getFavorites(screenName, paging)
        statuses.forEach {
          subscriber.onNext(it)
        }
      } catch (e: TwitterException) {
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
        statuses.forEach {
          subscriber.onNext(it)
        }
      } catch (e: TwitterException) {
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }

  fun getHomeTimelineListAsync(paging: Paging): Observable<ResponseList<Status>>
  {
    return  Observable.create<ResponseList<Status>> { subscriber ->
      try {
          subscriber.onNext(twitter.getHomeTimeline(paging))

      } catch (e: TwitterException) {
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
        twitter.getMentionsTimeline(paging)
          .forEach {
            subscriber.onNext(it)
          }
      } catch (e: TwitterException) {
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }

  fun getUserTimelineAsync(screenName:String?,paging: Paging): Observable<Status>
  {
    return  Observable.create<Status> { subscriber ->
      try {
        val statuses=
          if (screenName == null) {
            twitter.getUserTimeline(paging)
          }
          else{
            twitter.getUserTimeline(screenName,paging)
          }
        statuses.forEach {
          subscriber.onNext(it)
        }
      } catch (e: TwitterException) {
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }
}
