package xyz.donot.quetzal.event

import android.content.Context
import android.widget.Toast
import rx.Subscriber
import twitter4j.Status
import twitter4j.User

open class StatusSubscriber(val context: Context, val body: (status: Status) -> Unit) : Subscriber<Status>() {
  override fun onCompleted() {
    onLoaded()
  }

  override fun onError(ex: Throwable) {
    Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()

  }

  override fun onNext(status: Status) {
    body(status)
  }

  open fun onLoaded() {
  }
}


open class TwitterSubscriber(val context: Context) : Subscriber<Status>() {
    override fun onCompleted() {
      onLoaded()
    }

  override fun onError(ex: Throwable) {
    Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()

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
    Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()

  }

  override fun onNext(user: User) {
    onUser(user)
  }
  open fun onUser(user:User){}
  open fun onLoaded(){}
}



