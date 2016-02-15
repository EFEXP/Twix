package xyz.donot.twix.twitter

import org.greenrobot.eventbus.EventBus
import twitter4j.*
import xyz.donot.twix.event.OnStatusEvent

const val USER_STREAM :Int=0

class StreamManager(val twitter : Twitter, val type:Int)
{
  val eventBus by lazy { EventBus.getDefault() }
  var isConnected=false
  fun run()
  {
   val stream= TwitterStreamFactory().getInstance(twitter.authorization)
    StreamCreateUtil.addStatusListener(stream ,MyStatusAdapter())
    when(type){
      USER_STREAM->{stream.user()}
    }
   isConnected= true
  }
  inner class MyStatusAdapter: StatusAdapter() {
    override fun onStatus(status: Status) {
      when(type){
        USER_STREAM->{  eventBus.post(OnStatusEvent(status))}
      }
    }

    override fun onException(ex: Exception) {
      super.onException(ex)
      isConnected=false
    }

    override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice?) {
      super.onDeletionNotice(statusDeletionNotice)
    }
  }

}
