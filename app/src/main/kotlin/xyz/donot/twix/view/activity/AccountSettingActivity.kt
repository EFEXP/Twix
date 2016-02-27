package xyz.donot.twix.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_account_setting.*

import kotlinx.android.synthetic.main.content_account_setting.*
import org.greenrobot.eventbus.EventBus
import xyz.donot.twix.R
import xyz.donot.twix.event.OnAccountChanged
import xyz.donot.twix.model.DBAccount
import xyz.donot.twix.view.adapter.MyUserAccountAdapter

class AccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { startActivity(Intent(this@AccountSettingActivity,InitialActivity::class.java))
        finish()
        }
        val ac= Realm.getDefaultInstance().where(DBAccount::class.java).findAll()
        accountList.adapter=MyUserAccountAdapter(this@AccountSettingActivity,0,ac,true)
        accountList.setOnItemClickListener { adapterView, view, i, l ->
           val item=accountList.adapter.getItem(i) as DBAccount
          Realm.getDefaultInstance().use {
            it.executeTransaction {
            it.where(DBAccount::class.java).equalTo("isMain", true).findFirst().isMain=false
            val st= it.where(DBAccount::class.java).equalTo("id", item.id).findFirst().apply {
              isMain=true
            }
              EventBus.getDefault().post(OnAccountChanged())
              Snackbar.make(coordinatorLayout,"${st.screenName}をメインに設定しました",Snackbar.LENGTH_SHORT).show()
            }
          }


        }
    }

}
