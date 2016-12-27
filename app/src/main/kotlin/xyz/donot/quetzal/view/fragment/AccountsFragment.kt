package xyz.donot.quetzal.view.fragment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.realm.DBAccount
import xyz.donot.quetzal.util.getDeserialized
import xyz.donot.quetzal.view.activity.AccountSettingActivity


class AccountsFragment : UserList() {
    override fun loadMore() {
        val userList = Realm.getDefaultInstance().where(DBAccount::class.java).findAll().mapNotNull { it.user?.getDeserialized<User>() }.toMutableList()
        mAdapter.addAll(userList)
        mAdapter.stopMore()
    }

    override fun setUpRecycler() {
        base_recycler_view.setLayoutManager(LinearLayoutManager(context))
        mAdapter.setMore(R.layout.item_loadmore, object : RecyclerArrayAdapter.OnMoreListener {
            override fun onMoreClick() {
            }
            override fun onMoreShow() {
               loadMore()
            }
        })
        if (activity is AccountSettingActivity) {
            mAdapter.setOnItemClickListener {
                val item = mAdapter.getItem(it)
                Realm.getDefaultInstance().use {
                    it.executeTransaction {
                        it.where(DBAccount::class.java).equalTo("isMain", true).findFirst().isMain = false
                        it.where(DBAccount::class.java).equalTo("id", item.id).findFirst().apply {
                            isMain = true
                        }
                    }
                }
                val data = Intent()
                data.putExtra("accountChanged", true);
                activity.setResult(AppCompatActivity.RESULT_OK, data);
                activity.finish()
            }
        }
    }
}