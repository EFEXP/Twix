package xyz.donot.quetzal.util

import android.content.Context
import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.TwitterException


inline fun <T> safeTry(context: Context,crossinline  body: () -> T):Observable<T> {
  val observable= observable<T> { subscriber ->
    try {
      val toNext=body()
      subscriber.onNext(toNext)
      subscriber.onCompleted()
    } catch(ex: TwitterException) {

  //    subscriber.onError(ex)
      ex.printStackTrace()
      context.twitterEx(ex)
    } catch(ex: Exception) {

      //subscriber.onError(ex)
      ex.printStackTrace()
    }
  }
    .basicNetworkTask()

  return observable

}

