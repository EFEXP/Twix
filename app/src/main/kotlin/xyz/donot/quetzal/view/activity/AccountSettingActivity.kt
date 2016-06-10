package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.extrautils.start

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

    }

}
