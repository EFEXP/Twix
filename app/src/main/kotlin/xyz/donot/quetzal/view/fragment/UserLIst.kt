package xyz.donot.quetzal.view.fragment

import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.extrautils.Bundle
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.view.activity.UserActivity
import xyz.donot.quetzal.view.adapter.UsersAdapter

abstract class UserList : BaseRecyclerFragment<User, UsersAdapter>() {
  abstract override  fun loadMore()
  protected var cursor: Long = -1L
  override val mAdapter by lazy{ UsersAdapter(activity) }
  override fun setUpRecycler() {
    base_recycler_view.setLayoutManager(LinearLayoutManager(context))
    mAdapter.setMore(R.layout.item_loadmore, { loadMore() })
    mAdapter.setOnItemClickListener {
      val item = mAdapter.getItem(it)
      activity.start<UserActivity>(Bundle { putLong("user_id", item.id) })

    }
  }

  override fun reload() {
    cursor = -1
    super.reload()
  }
}
