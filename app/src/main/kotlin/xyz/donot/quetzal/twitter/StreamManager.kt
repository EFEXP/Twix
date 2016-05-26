package xyz.donot.quetzal.twitter

import android.content.Context
import org.greenrobot.eventbus.EventBus
import twitter4j.*
import xyz.donot.quetzal.event.OnDeleteEvent
import xyz.donot.quetzal.event.OnFavoritedEvent
import xyz.donot.quetzal.util.extrautils.d
import xyz.donot.quetzal.util.extrautils.toast
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
  private val stream by lazy { TwitterStreamFactory().getInstance(twitter.authorization) }
  init {isConnected =false }

  fun run()
  {
    if(!this.isConnected){
      stream.addConnectionLifeCycleListener(MyConnectionAdapter())
    StreamCreateUtil.addStatusListener(stream,MyNotificationAdapter())
    when(type){
      StreamType.USER_STREAM->{
        stream.user()}
      StreamType.FILTER_STREAM->{}
      StreamType.RETWEET_STREAM->{}
      StreamType.SAMPLE_STREAM->{stream.sample()}
    }
  }
    else{
      d("StreamManager", "You Have Already Connected to the Stream ")
    }
  }


  inner class MyConnectionAdapter:ConnectionLifeCycleListener{
    override fun onCleanUp() {
    }

    override fun onConnect() {
      context.toast("ストリームに接続しました")
     isConnected=true
    }

    override fun onDisconnect() {
      context.toast("ストリームから切断されました")
      isConnected=false
    }

  }

  inner class MyNotificationAdapter:UserStreamAdapter(){
    override fun onStatus(status: Status) {
      status.save(context)
     // Realm.getDefaultInstance().executeTransaction { it.createObject(DBStatus::class.java).status=status.getSerialized() }
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
      eventBus.post(OnFavoritedEvent(source,favoritedStatus))
    }
  }
  }

enum  class StreamType{
  USER_STREAM,
  SAMPLE_STREAM,
  FILTER_STREAM,
  RETWEET_STREAM
}

