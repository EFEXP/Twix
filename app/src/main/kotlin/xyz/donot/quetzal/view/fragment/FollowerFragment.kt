package xyz.donot.quetzal.view.fragment

import xyz.donot.quetzal.util.bindToLifecycle


class FollowerFragment : UserList()
{
   private  val userId:Long by lazy { arguments.getLong("userId") }
    private   val mode:Int by lazy {arguments.getInt("mode")  }
    companion object{
       val FOLLOWER=0
        val FRIEND=1
    }

  override fun loadMore() {
    when(mode){
      FRIEND->{
        twitterObservable.getFriendsAsync(userId, cursor)
          .bindToLifecycle(this@FollowerFragment)
          .subscribe {
              if (load.value) {
                  mAdapter.addAll(it)
              }
            if (it.hasNext()) {
              cursor = it.nextCursor
            }
            else{load.onNext(false)}
          }
      }
        FOLLOWER->{
        twitterObservable.getFollowerAsync(userId, cursor)
          .bindToLifecycle(this@FollowerFragment)
          .subscribe {
              if (load.value) {
                  mAdapter.addAll(it)
              }
            if (it.hasNext()) {
              cursor = it.nextCursor
            }
            else{load.onNext(false)}
          }
      }}
  }
}


