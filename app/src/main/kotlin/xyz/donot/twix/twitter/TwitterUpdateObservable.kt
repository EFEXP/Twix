package xyz.donot.twix.twitter

import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.Status
import twitter4j.StatusUpdate
import twitter4j.Twitter
import twitter4j.User
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
  fun updateStatusAsync(update: StatusUpdate): Observable<Status> {
    return  observable<Status> {
      try{it.onNext(twitter.updateStatus(update))
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

  fun createLikeAsync(statusId:Long): Observable<Status> {
    return  observable<Status> {
      try{it.onNext(twitter.createFavorite(statusId))
        it.onCompleted()
      }
      catch(ex:Exception){it.onError(ex)}
    }.basicNetworkTask()
  }
  fun createRetweetAsync(statusId:Long): Observable<Status> {
    return  observable<Status> {
      try{it.onNext(twitter.retweetStatus(statusId))
        it.onCompleted()
      }
      catch(ex:Exception){it.onError(ex)}
    }.basicNetworkTask()
  }
  fun deleteLikeAsync(statusId:Long):Observable<Status>{
    return observable<Status> {
      try  {
        it.onNext( twitter.destroyFavorite(statusId))
        it.onCompleted()
      }
      catch(ex:Exception){
        it.onError(ex)
      }}.basicNetworkTask()
  }

  fun updateProfileAsync(name:String,url:String,location:String,description:String): Observable<User> {
    return  observable<User> {
      try{it.onNext(twitter.updateProfile(name,url,location,description))
        it.onCompleted()
      }
      catch(ex:Exception){it.onError(ex)}
    }.basicNetworkTask()
  }
}





