package xyz.donot.quetzal.twitter

import rx.Observable
import twitter4j.PagableResponseList
import twitter4j.ResponseList
import twitter4j.Twitter
import twitter4j.User
import xyz.donot.quetzal.util.basicNetworkTask

class UsersObservable (val twitter: Twitter){
 fun getBlockList(): Observable<ResponseList<User>> {
   return  Observable.create<ResponseList<User>> {
     try{
       it.onNext(twitter.blocksList)
       it.onCompleted()
     }
     catch(ex:Exception){
       it.onError(ex)
     }

   } .basicNetworkTask()
 }

  fun getMuteList(page:Long):Observable<PagableResponseList<User>>{
    return  Observable.create<PagableResponseList<User>> {
      try{
        it.onNext(twitter.users().getMutesList(page))
        it.onCompleted()
      }
      catch(ex:Exception){
        it.onError(ex)
      }

    } .basicNetworkTask()
  }
  fun getMyUserInstance():Observable<User>{
    return  Observable.create<User> {
      try{
        it.onNext(twitter.verifyCredentials())
        it.onCompleted()
      }
      catch(ex:Exception){
        it.onError(ex)
      }

    } .basicNetworkTask()
  }

}
