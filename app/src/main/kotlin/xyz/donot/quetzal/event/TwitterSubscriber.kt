package xyz.donot.quetzal.event

import rx.Subscriber
import twitter4j.PagableResponseList
import twitter4j.Status
import twitter4j.User
import xyz.donot.quetzal.util.loge


open class TwitterSubscriber : Subscriber<Status>() {
    override fun onCompleted() {
      onLoaded()
    }

  override fun onError(e: Throwable) {
      loge(e.cause.toString(),e.message.toString())
    }

  override fun onNext(status: Status) {
    onStatus(status)
    }
  open fun onStatus(status: Status){}
  open fun onLoaded(){}
}

open class TwitterUserSubscriber : Subscriber<User>() {
  override fun onCompleted() {
    onLoaded()
  }

  override fun onError(e: Throwable) {
    loge(e.cause.toString(),e.message.toString())
  }

  override fun onNext(user: User) {
    onUser(user)
  }
  open fun onUser(user:User){}
  open fun onLoaded(){}
}

open class TwitterUsersSubscriber : Subscriber<PagableResponseList<User>>() {
  override fun onCompleted() {
    onLoaded()
  }

  override fun onError(e: Throwable) {
    loge(e.cause.toString(),e.message.toString())
  }

  override fun onNext(user: PagableResponseList<User>) {
    onUser(user)
  }
  open fun onUser(user:PagableResponseList<User>){}
  open fun onLoaded(){}




}
