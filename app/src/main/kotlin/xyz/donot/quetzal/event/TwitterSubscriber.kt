package xyz.donot.quetzal.event

import android.content.Context
import android.widget.Toast
import rx.Subscriber
import twitter4j.PagableResponseList
import twitter4j.Status
import twitter4j.User

import xyz.donot.quetzal.util.extrautils.e


open class TwitterSubscriber(val context: Context) : Subscriber<Status>() {
    override fun onCompleted() {
      onLoaded()
    }

  override fun onError(ex: Throwable) {
      Toast.makeText(context,"エラーが発生しました",Toast.LENGTH_LONG).show()
    e(ex.message.toString())
    }

  override fun onNext(status: Status) {
    onStatus(status)
    }
  open fun onStatus(status: Status){}
  open fun onLoaded(){}
}

open class TwitterUserSubscriber(val context: Context) : Subscriber<User>() {
  override fun onCompleted() {
    onLoaded()
  }

  override fun onError(ex: Throwable) {
    Toast.makeText(context,"エラーが発生しました",Toast.LENGTH_LONG).show()
   e(ex.message.toString())
  }

  override fun onNext(user: User) {
    onUser(user)
  }
  open fun onUser(user:User){}
  open fun onLoaded(){}
}

open class TwitterUsersSubscriber (val context: Context): Subscriber<PagableResponseList<User>>() {
  override fun onCompleted() {
    onLoaded()
  }

  override fun onError(ex: Throwable) {
   e(ex.message.toString())
  }

  override fun onNext(user: PagableResponseList<User>) {
    onUser(user)
  }
  open fun onUser(user:PagableResponseList<User>){}
  open fun onLoaded(){}




}
