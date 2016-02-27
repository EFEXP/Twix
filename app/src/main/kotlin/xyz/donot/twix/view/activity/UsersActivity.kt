package xyz.donot.twix.view.activity

import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.content_users.*
import xyz.donot.twix.R
import xyz.donot.twix.view.adapter.FFAdapter


class UsersActivity() : RxAppCompatActivity() {
    val userId by lazy { intent.getLongExtra("user_id",0) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
      toolbar.setNavigationOnClickListener { finish() }
      viewpager.adapter= FFAdapter(userId, supportFragmentManager)
      users_tabs.setupWithViewPager(viewpager)

    }





}


