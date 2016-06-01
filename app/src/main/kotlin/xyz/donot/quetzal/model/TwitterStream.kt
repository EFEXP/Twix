package xyz.donot.quetzal.model

import android.content.Context
import rx.lang.kotlin.BehaviorSubject
import rx.subjects.BehaviorSubject
import twitter4j.*
import xyz.donot.quetzal.twitter.StreamCreateUtil
import xyz.donot.quetzal.util.extrautils.d
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.util.isIgnore


class TwitterStream(val context: Context){

        val stream by lazy { TwitterStreamFactory().getInstance(getTwitterInstance().authorization)  }

    val isConnected:BehaviorSubject<Boolean>  by lazy{val t= BehaviorSubject<Boolean>()
        t
    }
    val statusSubject :BehaviorSubject<Status> by lazy { BehaviorSubject<Status>() }
    val deleteSubject:BehaviorSubject<StatusDeletionNotice>  by lazy { BehaviorSubject<StatusDeletionNotice>() }
    fun run(streamType: StreamType):TwitterStream
    {
        if(!isConnected.hasValue()){
           stream.addConnectionLifeCycleListener(MyConnectionAdapter())
            StreamCreateUtil.addStatusListener(stream,MyStreamAdapter())
            when(streamType){
                StreamType.USER_STREAM->{
                    stream.user()}
                StreamType.FILTER_STREAM->{}
                StreamType.RETWEET_STREAM->{}
                StreamType.SAMPLE_STREAM->{ stream.sample()}
            }
        }
        else if(!isConnected.value){
            stream.addConnectionLifeCycleListener(MyConnectionAdapter())
            StreamCreateUtil.addStatusListener( stream,MyStreamAdapter())
            when(streamType){
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
       return  this
    }
    fun clean()
    {
        if(isConnected.value){
         stream.shutdown()
        }
        else{
            d("StreamManager", "You Have Already Disconnected to the Stream ")
        }
    }

    inner class MyStreamAdapter: UserStreamAdapter(){
        override fun onStatus(status: Status) {
            if(!isIgnore(status.user.id)) {
                statusSubject.onNext(status)
            }
           }

        override fun onException(ex: Exception) {
            super.onException(ex)
            ex.printStackTrace()
        }

        override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {
            super.onDeletionNotice(statusDeletionNotice)
            deleteSubject.onNext(statusDeletionNotice)

        }

        override fun onFavorite(source: User, target: User, favoritedStatus: Status) {
            super.onFavorite(source, target, favoritedStatus)

        }
    }

    inner class MyConnectionAdapter: ConnectionLifeCycleListener {
        override fun onCleanUp() {
        }

        override fun onConnect() {
            isConnected.onNext(true)
        }

        override fun onDisconnect() {
            isConnected.onNext(false)
        }

    }
}
enum  class StreamType{
    USER_STREAM,
    SAMPLE_STREAM,
    FILTER_STREAM,
    RETWEET_STREAM
}