package xyz.donot.quetzal.view.fragment

import android.support.v7.widget.StaggeredGridLayoutManager
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import xyz.donot.quetzal.view.listener.OnLoadMoreListener


abstract class StaggeredFragment<L, out T : RecyclerArrayAdapter<L>> : BaseRecyclerFragment<L, T>()
{
    override fun setUpRecycler() {
        base_recycler_view.setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
        base_recycler_view.setOnScrollListener(object : OnLoadMoreListener({ loadMore() }) {})
    }

    abstract override fun loadMore()
}
