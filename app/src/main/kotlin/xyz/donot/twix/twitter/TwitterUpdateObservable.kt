package xyz.donot.twix.twitter

import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.Status
import twitter4j.Twitter
import xyz.donot.twix.util.basicNetworkTask

class TwitterUpdateObservable(val twitter: Twitter){

 fun updateStatusAsync(string: String): Observable<Status> {
  return  observable<Status> {
     try{it.onNext(twitter.updateStatus(string))
     it.onCompleted()
     }
     catch(ex:Exception){it.onError(ex)}
   }.basicNetworkTask()
 }
  fun deleteStatusAsync(statusId:Long):Observable<Status>{
  return observable<Status> {
     try  {
       it.onNext( twitter.destroyStatus(statusId))
       it.onCompleted()
     }
     catch(ex:Exception){
       it.onError(ex)
     }}.basicNetworkTask()
   }
  }



