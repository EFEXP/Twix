package xyz.donot.twix.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import xyz.donot.twix.R
import xyz.donot.twix.util.haveToken


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
    }

}}
