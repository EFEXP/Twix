package xyz.donot.quetzal.view.fragment

import xyz.donot.quetzal.util.bindToLifecycle


class FollowerFragment(val userId:Long,val mode:Mode): UsersWatcher()
{
  override fun loadMore() {
    when(mode){
      Mode.Friend->{
        twitterObservable.getFriendsAsync(userId, cursor)
          .bindToLifecycle(this@FollowerFragment)
          .subscribe {
            if(load){ it.forEach { mAdapter.add(it) }}
            if (it.hasNext()) {
              cursor = it.nextCursor
            }
            else{load=false}
          }
      }
      Mode.Follower->{
        twitterObservable.getFollowerAsync(userId, cursor)
          .bindToLifecycle(this@FollowerFragment)
          .subscribe {
            if(load){ it.forEach { mAdapter.add(it) }}
            if (it.hasNext()) {
              cursor = it.nextCursor
            }
            else{load=false}
          }

      }}
  }
  internal var cursor = -1L
  internal var load=true
  enum class Mode{
    Follower,
    Friend
  }
}


