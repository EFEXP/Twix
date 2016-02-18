package xyz.donot.twix.twitter

import org.greenrobot.eventbus.EventBus
import twitter4j.*
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.util.logd

const val USER_STREAM :Int=0

class StreamManager(  val twitter : Twitter, val type:Int)
{
  val eventBus by lazy { EventBus.getDefault() }
   var isConnected:Boolean
  init {isConnected =false }

  companion object Factory {
    var instance :StreamManager?=null
    fun getStreamObject(twitter : Twitter, type:Int):StreamManager{
      return instance?:StreamManager(twitter,type)
    }
  }
  fun run()
  {
    if(!this.isConnected){ this.isConnected= true
   val stream= TwitterStreamFactory().getInstance(twitter.authorization)
    StreamCreateUtil.addStatusListener(stream ,MyStatusAdapter())
    when(type){
      USER_STREAM->{stream.user()}
    }

  }
    else{

      logd("StreamManager","You Already Connected to the Stream ")
    }
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


