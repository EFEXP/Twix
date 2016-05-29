package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import twitter4j.Query
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.getDeserialized
import xyz.donot.quetzal.view.adapter.SearchAdapter

class SearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      val query_txt :String?= intent.getStringExtra("query_txt")
      val query_bundle :Query?=intent.getByteArrayExtra("query_bundle").getDeserialized<Query>()
      setContentView(R.layout.activity_search)
      toolbar.setNavigationOnClickListener { onBackPressed() }
      if(query_bundle!=null)
      {
        setUpViews(query_bundle)
      }
        else if (query_txt != null) {
        setUpViews(Query(query_txt))
      }


    }
 fun setUpViews(tweetQuery: Query){
     search_view_pager.adapter = SearchAdapter(tweetQuery, supportFragmentManager)
     search_tabs.setupWithViewPager(search_view_pager)

 }

}
