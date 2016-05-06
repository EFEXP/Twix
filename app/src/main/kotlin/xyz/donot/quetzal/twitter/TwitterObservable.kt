package xyz.donot.quetzal.twitter

import android.content.Context
import rx.Observable
import twitter4j.*
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.util.safeTry

class TwitterObservable(val context: Context,val twitter : Twitter)
{

  fun getFavoritesAsync(userID:Long,paging: Paging): Observable<ResponseList<Status>>
  {
    return safeTry(context){twitter.getFavorites(userID, paging)}
  }

    fun getRateLimit(vararg string: String): Observable<Map<String ,RateLimitStatus>>
    {
        return safeTry(context){twitter.getRateLimitStatus(*string)}
    }

  fun getHomeTimelineAsync(paging: Paging): Observable<ResponseList<Status>>
  {
    return safeTry(context){ twitter.getHomeTimeline(paging)}
  }
  fun getMentionsTimelineAsync(paging: Paging): Observable<ResponseList<Status>>
  {
    return safeTry(context){twitter.getMentionsTimeline(paging)}
  }

  fun getUserTimelineAsync(userID:Long?,paging: Paging): Observable<ResponseList<Status>>
  {
    return safeTry(context){
      if (userID == null) {
        twitter.getUserTimeline(paging)
      }
      else{
        twitter.getUserTimeline(userID,paging)
      }
    }
  }

  fun getMyListAsync(listid:Long,page:Paging): Observable<ResponseList<Status>>
  {
    return safeTry(context){twitter.getUserListStatuses(listid,page)}
  }

 fun getFriendsAsync(userid:Long,cursor:Long): Observable<PagableResponseList<User>>{
   return safeTry(context){twitter.getFriendsList(userid,cursor)}
 }
  fun getFollowerAsync(userid:Long,cursor:Long): Observable<PagableResponseList<User>>{
    return safeTry(context){twitter.getFollowersList(userid,cursor)}
  }
  fun getSearchAsync(query:Query): Observable<QueryResult>
  {
    return safeTry<QueryResult>(context){twitter.search(query)}
  }
  fun getUserSearchAsync(query:String,page:Int): Observable<ResponseList<User>>
  {
    return safeTry(context){ twitter.searchUsers(query,page)}

  }

  fun showStatusAsync(statusId:Long) :Observable<Status>{
    return safeTry<Status>(context){twitter.showStatus(statusId)}
  }


 fun showUser(long: Long) :Observable<User>{
   return safeTry<User>(context){twitter.showUser(long)}
 }
  fun showUser(longArray: LongArray) : Observable<ResponseList<User>>{
    return safeTry(context){twitter.users().lookupUsers(*longArray)}
  }

  fun showUser(string: String) :Observable<User>{
    return safeTry<User>(context){twitter.showUser(string)}
  }
   fun showFriendShip(long: Long) :Observable<Relationship>{
     return safeTry<Relationship>(context){twitter.showFriendship(getMyId(),long)}
  }
  fun showUsersList(long: Long) :Observable<ResponseList<UserList>>{
    return safeTry(context){twitter.getUserLists(long)}
  }

  fun showOwnUsersList(userid: Long,cursor: Long) :Observable<PagableResponseList<UserList>>{
    return safeTry(context){twitter.getUserListsOwnerships(userid,cursor)}
  }

}
