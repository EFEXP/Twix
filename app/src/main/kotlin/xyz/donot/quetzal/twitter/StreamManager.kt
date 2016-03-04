package xyz.donot.quetzal.twitter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import io.realm.Realm
import org.greenrobot.eventbus.EventBus
import twitter4j.*
import xyz.donot.quetzal.event.OnDeleteEvent
import xyz.donot.quetzal.model.DBStatus
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.util.logd
import xyz.donot.quetzal.util.save


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
      status.save(context)
      Realm.getDefaultInstance().executeTransaction { it.createObject(DBStatus::class.java).status=status.getSerialized() }
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

