package xyz.donot.twix.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import xyz.donot.twix.R
import xyz.donot.twix.twitter.TwitterObservable
import xyz.donot.twix.util.getTwitterInstance
import xyz.donot.twix.util.haveToken
import xyz.donot.twix.view.adapter.StatusAdapter
import kotlinx.android.synthetic.main.content_central.*
import twitter4j.Paging
import twitter4j.Status
import java.util.*


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
        //  ここから通常の処理
        recycler_view.layoutManager = LinearLayoutManager(this)
        val data= ArrayList<Status>()
        val twitter =getTwitterInstance()
        val  mAdapter = StatusAdapter(this@CentralActivity, data, null)
        TwitterObservable(twitter).getHomeTimelineAsync(Paging()).subscribe { data.add(it) ; mAdapter.notifyItemChanged(mAdapter.itemCount)}
        recycler_view.adapter=mAdapter
    }

}}
