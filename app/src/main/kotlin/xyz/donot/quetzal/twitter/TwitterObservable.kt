package xyz.donot.quetzal.twitter

import rx.Observable
import rx.lang.kotlin.observable
import twitter4j.*
import xyz.donot.quetzal.util.basicNetworkTask
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.util.logi

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
            subscriber.onCompleted()}
        }
      } catch (e: TwitterException) {
        subscriber.onError(e)
      }

    }
      .basicNetworkTask()
  }

 fun getFriendsAsync(userid:Long,cursor:Long): Observable<PagableResponseList<User>>{
   return Observable.create<PagableResponseList<User>> {subscriber->
  try{
       subscriber.onNext(twitter.getFriendsList(userid,cursor))
     }
  catch(ex:Exception){subscriber.onError(ex)}
     subscriber.onCompleted()
   }
     .basicNetworkTask()
 }
  fun getFollowerAsync(userid:Long,cursor:Long): Observable<PagableResponseList<User>>{
    return Observable.create<PagableResponseList<User>> {subscriber->
      try{
        subscriber.onNext(twitter.getFollowersList(userid,cursor))
      }
      catch(ex:Exception){subscriber.onError(ex)}
      subscriber.onCompleted()
    }.basicNetworkTask()

  }
  fun getMentionsTimelineAsync(paging: Paging): Observable<Status>
  {
    return  Observable.create<Status> { subscriber ->
      try {
      val  statuses=  twitter.getMentionsTimeline(paging)
        statuses.withIndex().forEachIndexed { int, indexedValue ->
          subscriber.onNext(indexedValue.value)
          if(indexedValue.index==paging.count-1){subscriber.onCompleted()}
        }
      } catch (e: TwitterException) {
        logi("error",e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }
  fun getSearchAsync(query:Query): Observable<QueryResult>
  {
    return  Observable.create<QueryResult> { subscriber ->
      try {
          subscriber.onNext(twitter.search(query))
          subscriber.onCompleted()

      } catch (e: TwitterException) {
        logi("error",e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }
  fun getUserSearchAsync(query:String,page:Int): Observable<User>
  {
    return  Observable.create<User> { subscriber ->
      try {
        twitter.searchUsers(query,page).forEach {
          subscriber.onNext(it)
        }

        subscriber.onCompleted()

      } catch (e: TwitterException) {
        logi("error",e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }
  fun getMyListAsync(listid:Long,page:Paging): Observable<Status>
  {
    return  Observable.create<Status> { subscriber ->
      try {
        val statuses=twitter.getUserListStatuses(listid,page)
        statuses.withIndex().forEachIndexed { int, indexedValue ->
          subscriber.onNext(indexedValue.value)
          if(indexedValue.index==page.count-1){subscriber.onCompleted()}
        }
      } catch (e: TwitterException) {
        logi("error",e.errorMessage)
        subscriber.onError(e)
      }
      subscriber.onCompleted()
    }
      .basicNetworkTask()
  }




  fun showStatusAsync(statusId:Long) :Observable<Status>{
    return observable <Status>{
        try{it.onNext(twitter.showStatus(statusId))
        it.onCompleted()
        }
        catch(ex:Exception){
          it.onError(ex)
        }
    }  .basicNetworkTask()
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

 fun showUser(long: Long) :Observable<User>{
  return  Observable.create<User> {
     try{
       it.onNext(twitter.showUser(long))
       it.onCompleted()
     }
     catch(ex:Exception){
       it.onError(ex)
     }

   } .basicNetworkTask()
 }

  fun showUser(string: String) :Observable<User>{
    return  Observable.create<User> {
      try{
        it.onNext(twitter.showUser(string))
        it.onCompleted()
      }
      catch(ex:Exception){
        it.onError(ex)
      }

    } .basicNetworkTask()
  }
   fun showFriendShip(long: Long) :Observable<Relationship>{
    return  Observable.create<Relationship> {
      try{
        it.onNext(twitter.showFriendship(getMyId(),long))
        it.onCompleted()
      }
      catch(ex:Exception){
        it.onError(ex)
      }

    } .basicNetworkTask()
  }
  fun showUsersList(long: Long) :Observable<ResponseList<UserList>>{
    return  Observable.create<ResponseList<UserList>> {
      try{
        it.onNext(twitter.list().getUserLists(long))
        it.onCompleted()
      }
      catch(ex:Exception){
        it.onError(ex)
      }

    } .basicNetworkTask()
  }

  fun showOwnUsersList(userid: Long,cursor: Long) :Observable<PagableResponseList<UserList>>{
    return  Observable.create<PagableResponseList<UserList>> {
      try{
        it.onNext(twitter.list().getUserListsOwnerships(userid,cursor))
        it.onCompleted()
      }
      catch(ex:Exception){
        it.onError(ex)
      }

    } .basicNetworkTask()
  }

}
