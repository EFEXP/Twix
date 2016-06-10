package xyz.donot.quetzal.twitter

import android.content.Context
import rx.Observable
import twitter4j.*
import xyz.donot.quetzal.util.safeTry
import java.io.File

class TwitterUpdateObservable(val context: Context,val twitter: Twitter){
 fun updateStatusAsync(string: String): Observable<Status> {
  return  safeTry(context){twitter.updateStatus(string)}
 }
  fun updateStatusAsync(update: StatusUpdate): Observable<Status> {
   return  safeTry(context){twitter.updateStatus(update)}
  }
  fun deleteStatusAsync(statusId:Long):Observable<Status>{
    return  safeTry(context){ twitter.destroyStatus(statusId)}
   }

  fun createLikeAsync(statusId:Long): Observable<Status> {
    return  safeTry(context){twitter.createFavorite(statusId)}

  }
  fun createRetweetAsync(statusId:Long): Observable<Status> {
    return  safeTry(context){twitter.retweetStatus(statusId)}
  }
  fun deleteLikeAsync(statusId:Long):Observable<Status>{
    return  safeTry(context){twitter.destroyFavorite(statusId)}
  }

  fun updateProfileAsync(name:String,url:String,location:String,description:String): Observable<User> {
    return  safeTry(context){twitter.updateProfile(name,url,location,description)}
  }
  fun profileImageUpdateAsync(file: File): Observable<User> {
    return  safeTry(context){twitter.updateProfileImage(file)}
  }

    fun profileImageBannerUpdateAsync(file: File): Observable<Unit> {
        return safeTry(context) { twitter.updateProfileBanner(file) }
    }

  fun createFriendShipAsync(long: Long): Observable<User> {
    return  safeTry(context){twitter.createFriendship(long)}
  }
  fun destroyFriendShipAsync(long: Long): Observable<User> {
    return  safeTry(context){twitter.destroyFriendship(long)}
  }
  fun createList(name: String,isPublic:Boolean,description: String): Observable<UserList> {
    return  safeTry(context){twitter.createUserList(name,isPublic,description)}
  }
}





