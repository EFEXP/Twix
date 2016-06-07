package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import rx.android.schedulers.AndroidSchedulers
import twitter4j.Status
import xyz.donot.quetzal.model.StreamType
import xyz.donot.quetzal.model.TwitterStream
import xyz.donot.quetzal.view.adapter.StatusAdapter


abstract  class TimeLine() : PlainFragment<Status, StatusAdapter,StatusAdapter.ViewHolder>()
{

  abstract  override  fun loadMore()
  protected  val tsm by lazy { TwitterStream(context).run(StreamType.USER_STREAM)}


  override fun onDestroy() {
    super.onDestroy()
    tsm.clean()
  }

  override val mAdapter: StatusAdapter by lazy{ StatusAdapter(activity) }
  override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
    tsm.deleteSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
              mAdapter.allData.filter {de-> de.id==it.statusId}.mapNotNull { mAdapter.remove(it) }
    }

  }

}
