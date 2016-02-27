package xyz.donot.twix.view.fragment

import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.bindToLifecycle


class FollowerFragment(val userId:Long,val mode:Mode):BaseUserFragment()
{
  internal var cursor = -1L
  override fun TimelineLoader() {
    when(mode){
      Mode.Friend->{
        TwitterObservable(twitter).getFriendsAsync(userId,cursor)
          .bindToLifecycle(this@FollowerFragment)
          .subscribe {
            if(it.hasNext()){cursor=it.nextCursor}
            it.forEach {  mAdapter.add(it) }
          }}
      Mode.Follower->{
        TwitterObservable(twitter).getFollowerAsync(userId,cursor)
          .bindToLifecycle(this@FollowerFragment)
          .subscribe {
            if(it.hasNext()){cursor=it.nextCursor}
            it.forEach {  mAdapter.add(it) }
          }}
      }

  }
  enum class Mode{
    Follower,
    Friend
  }
}


