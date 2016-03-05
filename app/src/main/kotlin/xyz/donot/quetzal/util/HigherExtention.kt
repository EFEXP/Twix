package xyz.donot.quetzal.util

import android.content.Context
import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.TwitterException


fun <T> safeTry(context: Context,body: () -> T):Observable<T> {
  return observable<T> { subscriber ->
    try {
      val toNext=body()
      subscriber.onNext(toNext)
      subscriber.onCompleted()
    } catch(ex: TwitterException) {
      loge(ex.cause.toString(),ex.stackTrace.toString())
      subscriber.onError(ex)
     context.twitterEx(ex)
    } catch(ex: Exception) {
      loge(ex.cause.toString(),ex.stackTrace.toString())
      subscriber.onError(ex)
    }
  }  .basicNetworkTask() }

