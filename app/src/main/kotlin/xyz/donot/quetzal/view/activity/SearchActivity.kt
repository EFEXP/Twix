package xyz.donot.quetzal.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.SearchEvent
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import xyz.donot.quetzal.R
import xyz.donot.quetzal.view.adapter.SearchAdapter

class SearchActivity : AppCompatActivity() {
  val searchView by lazy{toolbar.menu.findItem(R.id.menu_search).actionView as SearchView }
  val query_txt by lazy { intent.getStringExtra("query_txt") }
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_search)
      val toolbar = findViewById(R.id.toolbar) as Toolbar
      toolbar.inflateMenu(R.menu.search)
      toolbar.setNavigationOnClickListener { finish() }
      setUpViews("Hello")
      if (query_txt != null) {
        setUpViews(query_txt)
      }
      searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
        override fun onQueryTextChange(p0: String): Boolean {
          return true
        }
        override fun onQueryTextSubmit(p0: String): Boolean {
          Answers.getInstance().logSearch(SearchEvent().putQuery(p0))
          startActivity(Intent(this@SearchActivity,SearchActivity::class.java).putExtra("query_txt",p0))
          finish()
          return true
        }
      })
    }
 fun setUpViews(string: String){
   if (!string.isNullOrEmpty()) {
     search_view_pager.adapter = SearchAdapter(string, supportFragmentManager)
     search_tabs.setupWithViewPager(search_view_pager)
     searchView.clearFocus()
   }
 }

}
