package xyz.donot.twix.twitter

import org.greenrobot.eventbus.EventBus
import twitter4j.*
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.util.logd



class StreamManager(  val twitter : Twitter, val type:StreamType)
{
  val eventBus by lazy { EventBus.getDefault() }
   var isConnected:Boolean
  init {isConnected =false }

  fun run()
  {
    if(!this.isConnected){ this.isConnected= true
   val stream= TwitterStreamFactory().getInstance(twitter.authorization)
    StreamCreateUtil.addStatusListener(stream ,MyStatusAdapter())
    when(type){
      StreamType.USER_STREAM->{stream.user()}
      StreamType.FILTER_STREAM->{}
        StreamType.RETWEET_STREAM->{}
      StreamType.SAMPLE_STREAM->{stream.sample()}
    }

  }
    else{
      logd("StreamManager","You Have Already Connected to the Stream ")
    }
  }
  inner class MyStatusAdapter: StatusAdapter() {
    override fun onStatus(status: Status) {
      when(type){
        StreamType.USER_STREAM->{eventBus.post(OnStatusEvent(status))}
        StreamType.FILTER_STREAM->{}
        StreamType.RETWEET_STREAM->{}
        StreamType.SAMPLE_STREAM->{eventBus.post(OnStatusEvent(status))}
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
object Factory {
  var instance :StreamManager?=null
  fun getStreamObject(twitter : Twitter, type:StreamType):StreamManager{
    return instance?:StreamManager(twitter,type)
  }
}
enum  class StreamType{
  USER_STREAM,
  SAMPLE_STREAM,
  FILTER_STREAM,
  RETWEET_STREAM
}

