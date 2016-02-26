package xyz.donot.twix.twitter

import android.content.Context
import org.greenrobot.eventbus.EventBus
import twitter4j.*
import xyz.donot.twix.event.OnDeleteEvent
import xyz.donot.twix.event.OnStatusEvent
import xyz.donot.twix.notification.NewMentionNotification
import xyz.donot.twix.util.isIgnore
import xyz.donot.twix.util.isMentionToMe
import xyz.donot.twix.util.logd



 class StreamManager( val context: Context, val twitter : Twitter, val type:StreamType)
{
  val eventBus by lazy { EventBus.getDefault() }
   var isConnected:Boolean
  init {isConnected =false }

  fun run()
  {
    if(!this.isConnected){
      this.isConnected= true
   val stream= TwitterStreamFactory().getInstance(twitter.authorization)
    StreamCreateUtil.addStatusListener(stream,MyNotificationAdapter())
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


  inner class MyNotificationAdapter:UserStreamAdapter(){
    override fun onStatus(status: Status) {
      if(!isIgnore(status.user.id)){
        if(isMentionToMe(status)){  NewMentionNotification.notify(context,status.text,0)}
        when(type){
          StreamType.USER_STREAM->{eventBus.post(OnStatusEvent(status))}
          StreamType.FILTER_STREAM->{}
          StreamType.RETWEET_STREAM->{}
          StreamType.SAMPLE_STREAM->{eventBus.post(OnStatusEvent(status))}
        }}
    }

    override fun onException(ex: Exception) {
      super.onException(ex)
      isConnected=false
    }

    override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {
      super.onDeletionNotice(statusDeletionNotice)
      eventBus.post(OnDeleteEvent(statusDeletionNotice))
    }

    override fun onFavorite(source: User, target: User, favoritedStatus: Status) {
      super.onFavorite(source, target, favoritedStatus)

    }
  }



  }
object Factory {
  var instance :StreamManager?=null
  fun getStreamObject(context: Context,twitter : Twitter, type:StreamType):StreamManager{
    if (instance == null) {
      instance=StreamManager(context,twitter,type)
    }
    return instance?:throw IllegalStateException()
  }
}
enum  class StreamType{
  USER_STREAM,
  SAMPLE_STREAM,
  FILTER_STREAM,
  RETWEET_STREAM
}

