package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import org.greenrobot.eventbus.Subscribe
import twitter4j.Status
import xyz.donot.quetzal.event.*
import xyz.donot.quetzal.view.adapter.StatusAdapter
import java.util.*


abstract  class TimeLine() : PlainFragment<Status, StatusAdapter, xyz.donot.quetzal.view.adapter.StatusAdapter.ViewHolder>()
{


  open  fun onDeserialize(){}
  open  fun onSerialize(sStatus: Status){}
  abstract  override  fun loadMore()

  override val data: MutableList<Status> by lazy { LinkedList<Status>() }
  override val mAdapter: StatusAdapter by lazy{ StatusAdapter(activity, data) }
  override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
    val hasEvent=eventBus.hasSubscriberForEvent(OnDeleteEvent::class.java)
      ||eventBus.hasSubscriberForEvent(OnStatusEvent::class.java)
      ||eventBus.hasSubscriberForEvent(OnReplyEvent::class.java)
      ||eventBus.hasSubscriberForEvent(OnCustomtabEvent::class.java)
      ||eventBus.hasSubscriberForEvent(OnRetweetEvent::class.java)
    val re=eventBus.isRegistered(this)
    if(!re&&hasEvent){
      eventBus.register(this)
    }
   onDeserialize()
  }
  override fun onDestroy() {
    super.onDestroy()
    if(eventBus.isRegistered(this)){
      eventBus.unregister(this)
    }
  }
  @Subscribe
  fun onEvent(deleteEvent: OnDeleteEvent){
    data.filter { it.id==deleteEvent.deletionNotice.statusId }.mapNotNull { mAdapter.remove(it) }
  }
}
