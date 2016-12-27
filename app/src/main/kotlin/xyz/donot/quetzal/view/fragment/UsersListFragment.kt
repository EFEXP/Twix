package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import twitter4j.UserList
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.extrautils.Bundle
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.view.activity.SeeMyListActivity
import xyz.donot.quetzal.viewmodel.adapter.UsersListAdapter

class UsersListFragment() : BaseRecyclerFragment<UserList, UsersListAdapter>() {

    override val mAdapter: UsersListAdapter by lazy { UsersListAdapter(context) }
    internal var cursor = -1L

    override fun setUpRecycler() {
        mAdapter.setMore(R.layout.item_loadmore, object : RecyclerArrayAdapter.OnMoreListener {
            override fun onMoreClick() {
            }
            override fun onMoreShow() {
                loadMore()
            }
        })
        mAdapter.setOnItemClickListener {
            val item = mAdapter.getItem(it)
            activity.start<SeeMyListActivity>(Bundle { putLong("list_id", item.id) })
        }
        base_recycler_view.setLayoutManager(LinearLayoutManager(activity))
    }

    override fun loadMore() {
        val userId = arguments.getLong("userId")
        if (userId == 0L) {
            TwitterObservable(context, twitter).showOwnUsersList(getMyId(), cursor)
                    .subscribe {
                        result ->
                        if (load.value) {
                            mAdapter.addAll(result)
                        }
                        if (result.hasNext()) {
                            cursor = result.nextCursor
                        } else {
                            load.onNext(false)
                        }
                    }
        } else {
            TwitterObservable(context, twitter).showOwnUsersList(userId, cursor)
                    .subscribe {
                        result ->
                        if (load.value) {
                            mAdapter.addAll(result)
                        }
                        if (result.hasNext()) {
                            cursor = result.nextCursor
                        } else {
                            load.onNext(false)
                        }
                    }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_timeline_base, container, false)
    }

    override fun reload() {
        cursor = -1
        super.reload()
    }



}
