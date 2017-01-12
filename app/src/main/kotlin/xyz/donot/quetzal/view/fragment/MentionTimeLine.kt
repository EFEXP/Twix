package xyz.donot.quetzal.view.fragment


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import rx.android.schedulers.AndroidSchedulers
import twitter4j.Paging
import xyz.donot.quetzal.model.realm.DBNotification
import xyz.donot.quetzal.util.getDeserialized
import xyz.donot.quetzal.util.isMentionToMe


class MentionTimeLine : TimeLineFragment() {
 /* override fun onDeserialize() {
    super.onDeserialize()
    val tInstance=   Realm.getDefaultInstance().where(DBNotification::class.java).equalTo("type",0).findAll()
    tInstance?.forEach { mAdapter.add(it.status?.getStatusDeserialized()!!) }
  }

  override fun onSerialize(sStatus: Status) {
    super.onSerialize(sStatus)
    Realm.getDefaultInstance().executeTransaction {
      val tInstance=   it.createObject(DBNotification::class.java)
      tInstance.status=sStatus.getSerialized()
      tInstance.type=0
    }
  }*/

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tsm.statusSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
      if(isMentionToMe(it)) {
        mAdapter.insert(it,0)
        val t=base_recycler_view.recyclerView.layoutManager as LinearLayoutManager
        if(t.findFirstCompletelyVisibleItemPosition()==0){base_recycler_view.scrollToPosition(0)}
      }
    }
    val t=  Realm.getDefaultInstance().where(DBNotification::class.java).equalTo("type",0).findAll()
    t?.forEach { mAdapter.add(it.status?.getDeserialized()!!)}
  }
  override fun loadMore() {
  twitterObservable.getMentionsTimelineAsync(Paging(page)).subscribe{mAdapter.addAll(it)}
  }


}
