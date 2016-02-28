package xyz.donot.quetzal.view.fragment

import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.bindToLifecycle


class FollowerFragment(val userId:Long,val mode:Mode):BaseUserFragment()
{
  internal var cursor = -1L
  internal var load=true
  override fun TimelineLoader() {
    when(mode){

        Mode.Friend->{
          TwitterObservable(twitter).getFriendsAsync(userId, cursor)
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
          TwitterObservable(twitter).getFollowerAsync(userId, cursor)
            .bindToLifecycle(this@FollowerFragment)
            .subscribe {
              if(load){ it.forEach { mAdapter.add(it) }}
              if (it.hasNext()) {
                cursor = it.nextCursor
              }
              else{load=false}
            }

      }
      }

  }
  enum class Mode{
    Follower,
    Friend
  }
}


