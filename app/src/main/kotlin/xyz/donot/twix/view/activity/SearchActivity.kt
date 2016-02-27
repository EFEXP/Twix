package xyz.donot.twix.view.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.content_search.*
import xyz.donot.twix.R
import xyz.donot.twix.view.adapter.SearchAdapter

class SearchActivity : AppCompatActivity() {
    val query_txt by lazy { intent.getStringExtra("query_txt") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
      search_view_pager.adapter=SearchAdapter(query_txt,supportFragmentManager)
      search_tabs.setupWithViewPager(search_view_pager)


        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }

}
