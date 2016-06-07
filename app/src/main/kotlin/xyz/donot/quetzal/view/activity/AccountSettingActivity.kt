package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import io.realm.Realm
import kotlinx.android.synthetic.main.content_account_setting.*
import twitter4j.User
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.DBAccount
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.util.getDeserialized
import xyz.donot.quetzal.view.adapter.MyAccountsAdapter

class AccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationOnClickListener { finish() }
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { start<TwitterOauthActivity>()
        finish()
        }
       val mAdapter= MyAccountsAdapter(context = this@AccountSettingActivity)
      accountList.apply {
        adapter= mAdapter
        layoutManager=LinearLayoutManager(this@AccountSettingActivity)
      }
        mAdapter.addAll(Realm.getDefaultInstance().where(DBAccount::class.java).findAll().mapNotNull { it.user?.getDeserialized<User>() }.toMutableList())

    }

}
