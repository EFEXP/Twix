package xyz.donot.quetzal.view.fragment


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import twitter4j.Paging
import xyz.donot.quetzal.event.OnReplyEvent
import xyz.donot.quetzal.model.DBNotification
import xyz.donot.quetzal.util.getDeserialized


class MentionTimeLine() : TimeLine() {
 /* override fun onDeserialize() {
    super.onDeserialize()
    val t=   Realm.getDefaultInstance().where(DBNotification::class.java).equalTo("type",0).findAll()
    t?.forEach { mAdapter.add(it.status?.getStatusDeserialized()!!) }
  }

  override fun onSerialize(sStatus: Status) {
    super.onSerialize(sStatus)
    Realm.getDefaultInstance().executeTransaction {
      val t=   it.createObject(DBNotification::class.java)
      t.status=sStatus.getSerialized()
      t.type=0
    }
  }*/

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val t=  Realm.getDefaultInstance().where(DBNotification::class.java).equalTo("type",0).findAll()
    t?.forEach { mAdapter.add(it.status?.getDeserialized()!!) }
  }
  override fun loadMore() {
  twitterObservable.getMentionsTimelineAsync(Paging(page)).subscribe({mAdapter.addAll(it)})
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onEventMainThread(statusEvent: OnReplyEvent){
    data.add(0,statusEvent.status)
    val t=base_recycler_view.layoutManager as LinearLayoutManager
    if(t.findFirstCompletelyVisibleItemPosition()==0){base_recycler_view.scrollToPosition(0)}
    mAdapter.notifyItemInserted(0)
  }
}
