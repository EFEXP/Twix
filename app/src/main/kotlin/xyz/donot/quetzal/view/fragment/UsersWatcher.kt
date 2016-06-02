package xyz.donot.quetzal.view.fragment

import rx.Subscriber
import twitter4j.ResponseList
import twitter4j.User
import xyz.donot.quetzal.view.adapter.UsersAdapter

abstract class UsersWatcher : PlainFragment<User, UsersAdapter,UsersAdapter.ViewHolder>(){
  abstract override  fun loadMore()
  val userSubscriber by lazy { TwitterUserSubscriber() }
  override val mAdapter by lazy{ UsersAdapter(activity) }

  inner class TwitterUserSubscriber: Subscriber<ResponseList<User>>() {
    override fun onNext(p0: ResponseList<User>) {
     mAdapter.addAll(p0.toMutableList())
    }
    override fun onCompleted() {
    }
    override fun onError(e: Throwable) {
    }


  }
}
