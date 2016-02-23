package xyz.donot.twix.event

import rx.Subscriber
import twitter4j.Status
import xyz.donot.twix.util.logi


abstract  class TwitterSubscriber : Subscriber<Status>() {
    override fun onCompleted() {
      onLoaded()
    }

    override fun onError(e: Throwable) {
      logi(e.cause.toString(),e.message.toString())
    }

    override fun onNext(status: Status) {
      onStatus(status)
    }
 abstract  fun onStatus(status: Status)
  open fun onLoaded(){}
}

