package xyz.donot.quetzal.view.dialog

import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.TwitterException
import xyz.donot.quetzal.util.basicNetworkTask
import xyz.donot.quetzal.util.extrautils.w

import xyz.donot.quetzal.view.fragment.UsersWatcher

class RetweeterDialog() : UsersWatcher (){
  var cursor:Long =-1
  override fun loadMore() {
      if(load.value){getRetweeterIdAsync().subscribe { twitterObservable.showUser(it).subscribe(userSubscriber) }} }
  fun getRetweeterIdAsync(): Observable<LongArray>
  {
    return  observable<LongArray> { subscriber ->
      try {
         val statusId=arguments.getLong("statusId")
        val ids = twitter.getRetweeterIds(statusId, cursor)
        if (ids.hasNext()) {
          cursor=ids.nextCursor
        }
        else{
          load.onNext(false)
        }
        subscriber.onNext(ids.iDs)
      } catch (e: TwitterException) {
       w(e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }


}
