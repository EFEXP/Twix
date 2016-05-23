package xyz.donot.quetzal.view.fragment

import xyz.donot.quetzal.util.bindToLifecycle


class FollowerFragment: UsersWatcher()
{
    private var cursor :Long
    internal var load=true
    val userId:Long by lazy { arguments.getLong("userId") }
    val mode:Int by lazy { arguments.getInt("mode") }
    companion object{
       val FOLLOWER=0
        val FRIEND=1
    }
    init {
        cursor=-1L
    }
  override fun loadMore() {
    when(mode){
      FRIEND->{
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
        FOLLOWER->{
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
}


