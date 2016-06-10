package xyz.donot.quetzal.view.fragment


import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.TwitterException
import xyz.donot.quetzal.util.basicNetworkTask

class RetweeterDialog : UserList() {
  override fun loadMore() {
    if (load.value) {
      getRetweeterIdAsync().subscribe { twitterObservable.showUser(it).subscribe { mAdapter.addAll(it) } }
    }
  }
  fun getRetweeterIdAsync(): Observable<LongArray>
  {
    return  observable<LongArray> { subscriber ->
      try {
        val statusId = arguments.getLong("statusId")
        val ids = twitter.getRetweeterIds(statusId, cursor)
        if (ids.hasNext()) {
          cursor = ids.nextCursor
        } else {
          load.onNext(false)
        }
        subscriber.onNext(ids.iDs)
      } catch (e: TwitterException) {
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }


}
