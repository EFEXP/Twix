package xyz.donot.quetzal.view.activity

import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.content_users.*
import xyz.donot.quetzal.R
import xyz.donot.quetzal.viewmodel.adapter.FFAdapter


class UsersActivity : RxAppCompatActivity() {
    val userId by lazy { intent.getLongExtra("user_id",0L) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
      toolbar.setNavigationOnClickListener { finish() }
      if(userId!=0L) {
        viewpager.adapter = FFAdapter(userId, supportFragmentManager)
        users_tabs.setupWithViewPager(viewpager)
      }
    }





}


