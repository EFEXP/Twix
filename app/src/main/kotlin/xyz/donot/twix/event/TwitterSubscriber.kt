package xyz.donot.twix.event

import rx.Subscriber
import twitter4j.Status
import xyz.donot.twix.util.loge


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

