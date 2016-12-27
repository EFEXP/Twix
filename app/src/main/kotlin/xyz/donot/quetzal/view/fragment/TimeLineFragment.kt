package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import rx.android.schedulers.AndroidSchedulers
import twitter4j.Status
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.StreamType
import xyz.donot.quetzal.model.TwitterStream
import xyz.donot.quetzal.viewmodel.adapter.StatusAdapter


abstract class TimeLineFragment : BaseRecyclerFragment<Status, StatusAdapter>() {
    override fun setUpRecycler() {
        mAdapter.setMore(R.layout.item_loadmore, object : RecyclerArrayAdapter.OnMoreListener {
            override fun onMoreClick() {
            }
            override fun onMoreShow() {
                loadMore()
            }
        })
        base_recycler_view.setLayoutManager(LinearLayoutManager(context))
    }

    abstract override fun loadMore()
    protected val tsm by lazy { TwitterStream().run(StreamType.USER_STREAM) }
    override fun onDestroy() {
        super.onDestroy()
        tsm.clean()
    }

    override val mAdapter: StatusAdapter by lazy { StatusAdapter(activity) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tsm.deleteSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mAdapter.allData.filter { de -> de.id == it.statusId }.mapNotNull { mAdapter.remove(it) } }
    }

}