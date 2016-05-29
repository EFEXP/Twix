package xyz.donot.quetzal.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search_setting.*
import twitter4j.Query
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.util.getSerialized

class SearchSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_setting)
        toolbar.inflateMenu(R.menu.menu_search_setting)
        toolbar.setNavigationOnClickListener{onBackPressed()}
        toolbar.setOnMenuItemClickListener {
            var querytext=search_setting_query.text.toString()
            if(!querytext.isNullOrEmpty()){
                val query=Query()
                if(!search_setting_from.text.isNullOrEmpty()) {
                    querytext +=" from:${search_setting_from.text}"
                }
                if(!search_setting_to.text.isNullOrEmpty()) {
                    querytext +=" to:${search_setting_to.text}"
                }

                if(search_setting_video.isChecked) {
                    querytext +=" filter:videos"
                }

                if(search_setting_image.isChecked) {
                    querytext +=" filter:images"
                }
                querytext +="  -rt"
                query.lang="ja"
                query.resultType=Query.MIXED

                query.query=querytext

                start<SearchActivity>(Bundle().apply {
                    putByteArray("query_bundle",query.getSerialized())
                })

                true
            }
            false
        }
    }
}
