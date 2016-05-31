package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import rx.android.schedulers.AndroidSchedulers
import twitter4j.Status
import xyz.donot.quetzal.model.StreamType
import xyz.donot.quetzal.model.TwitterStream
import xyz.donot.quetzal.view.adapter.StatusAdapter
import java.util.*


abstract  class TimeLine() : PlainFragment<Status, StatusAdapter, xyz.donot.quetzal.view.adapter.StatusAdapter.ViewHolder>()
{

  abstract  override  fun loadMore()
  protected  val tsm by lazy { TwitterStream(context).run(StreamType.USER_STREAM)}
  override val data: MutableList<Status> by lazy { LinkedList<Status>() }
  override val mAdapter: StatusAdapter by lazy{ StatusAdapter(activity, data) }
  override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
    tsm.deleteSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
     data.filter {de-> de.id==it.statusId}.mapNotNull { mAdapter.remove(it) }
    }

  }

}
