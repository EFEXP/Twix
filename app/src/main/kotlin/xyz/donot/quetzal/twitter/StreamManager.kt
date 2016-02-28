package xyz.donot.quetzal.twitter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import twitter4j.*
import xyz.donot.quetzal.event.OnDeleteEvent
import xyz.donot.quetzal.event.OnReplyEvent
import xyz.donot.quetzal.event.OnStatusEvent
import xyz.donot.quetzal.notification.NewMentionNotification
import xyz.donot.quetzal.util.isIgnore
import xyz.donot.quetzal.util.isMentionToMe
import xyz.donot.quetzal.util.logd



 class StreamManager( val context: Context, val twitter : Twitter, val type:StreamType)
{
  object Factory {
    var instance :StreamManager?=null
    fun getStreamObject(context: Context,twitter : Twitter, type:StreamType):StreamManager{
      if (instance == null) {
        instance=StreamManager(context,twitter,type)
      }
      return instance?:throw IllegalStateException()
    }
  }
  val eventBus by lazy { EventBus.getDefault() }
   var isConnected:Boolean
  val stream by lazy { TwitterStreamFactory().getInstance(twitter.authorization) }
  init {isConnected =false }

  fun run()
  {
    if(!this.isConnected){
      this.isConnected= true
    StreamCreateUtil.addStatusListener(stream,MyNotificationAdapter())
    when(type){
      StreamType.USER_STREAM->{stream.user()}
      StreamType.FILTER_STREAM->{}
      StreamType.RETWEET_STREAM->{}
      StreamType.SAMPLE_STREAM->{stream.sample()}
    }
      Handler(Looper.getMainLooper()).post {Toast.makeText(context,"ストリームに接続しました",Toast.LENGTH_LONG).show()}

  }
    else{
      logd("StreamManager","You Have Already Connected to the Stream ")
    }
  }


  inner class MyNotificationAdapter:UserStreamAdapter(){
    override fun onStatus(status: Status) {
      if(!isIgnore(status.user.id)){
        if(isMentionToMe(status)){
          eventBus.post(OnReplyEvent(status))
          if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notifications",false)){
          NewMentionNotification.notify(context,status.text,0)}}
        when(type){
          StreamType.USER_STREAM->{eventBus.post(OnStatusEvent(status))}
          StreamType.FILTER_STREAM->{}
          StreamType.RETWEET_STREAM->{}
          StreamType.SAMPLE_STREAM->{eventBus.post(OnStatusEvent(status))}
        }}
    }

    override fun onException(ex: Exception) {
      super.onException(ex)
      ex.printStackTrace()
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

enum  class StreamType{
  USER_STREAM,
  SAMPLE_STREAM,
  FILTER_STREAM,
  RETWEET_STREAM
}

