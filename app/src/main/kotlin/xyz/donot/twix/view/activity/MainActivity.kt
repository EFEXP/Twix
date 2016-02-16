package xyz.donot.twix.view.activity


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


import xyz.donot.twix.R
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.haveToken
import xyz.donot.twix.util.updateUserProfile
import xyz.donot.twix.view.adapter.TimeLinePagerAdapter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      if(!haveToken())
      {
        startActivity(Intent(this@MainActivity, InitialActivity::class.java))
        finish()
      }
      else {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        viewpager.adapter=TimeLinePagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(viewpager)
        design_navigation_view.setNavigationItemSelectedListener({
          when (it.itemId) {
            R.id.setting -> {
              startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
              drawer_layout.closeDrawers()
            }
            R.id.account -> {
              startActivity(Intent(this@MainActivity, AccountSettingActivity::class.java))
              drawer_layout.closeDrawers()
                }
          }
          true
        })
        updateUserProfile(this@MainActivity.getTwitterInstance())
      }



}}
