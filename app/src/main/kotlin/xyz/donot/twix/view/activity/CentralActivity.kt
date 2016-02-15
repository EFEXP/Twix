package xyz.donot.twix.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import kotlinx.android.synthetic.main.activity_central.*
import xyz.donot.twix.R
import xyz.donot.twix.util.haveToken
import xyz.donot.twix.view.adapter.TimeLinePagerAdapter


class CentralActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      if(!haveToken())
      {
        startActivity(Intent(this@CentralActivity, InitialActivity::class.java))
        finish()
      }
      else {
        setContentView(R.layout.activity_central)
        setSupportActionBar(toolbar)
        viewpager.adapter=TimeLinePagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(viewpager)
        fab.setOnClickListener({
          TweetComposer.Builder(this).show()
        })
        val v=findViewById(R.id.design_navigation_view) as NavigationView
        v.setNavigationItemSelectedListener {
          menuItem: MenuItem ->
          throw UnknownError()
          Toast.makeText(this@CentralActivity,"design_navigation_view",Toast.LENGTH_LONG).show()
          when (menuItem.itemId) {
            R.id.setting -> {
              // drawer_layout.closeDrawers()
            }
            R.id.account -> {
              startActivity(Intent(this@CentralActivity, AccountSettingActivity::class.java))
              // drawer_layout.closeDrawers()
            }

          }
          false

        }



      }



}}
