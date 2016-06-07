package xyz.donot.quetzal.view.activity

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_search_setting.*
import twitter4j.Query
import xyz.donot.quetzal.R
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.view.fragment.DatePickFragment

class SearchSettingActivity : RxAppCompatActivity() {
   fun dateSet(year: Int, monthOfYear: Int, dayOfMonth: Int,isFrom:Boolean) {
       if(isFrom){
           day_from.text = "${year.toString()}年${monthOfYear.toString()}月${dayOfMonth.toString()}日～"
           day_from.tag=" since:${year.toString()}-${monthOfYear.toString()}-${dayOfMonth.toString()}"
       }
       else {
           day_to.text = "～${year.toString()}年${monthOfYear.toString()}月${dayOfMonth.toString()}日"
           day_to.tag=" until:${year.toString()}-${monthOfYear.toString()}-${dayOfMonth.toString()}"
       }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_setting)
        day_from.setOnClickListener {DatePickFragment()
                .apply { arguments= Bundle().apply { putBoolean("isFrom",true) } }
                .show(supportFragmentManager,"") }
        day_to.setOnClickListener {DatePickFragment()
                .apply { arguments= Bundle().apply { putBoolean("isFrom",false) } }
                .show(supportFragmentManager,"") }
        toolbar.inflateMenu(R.menu.menu_search_setting)
        toolbar.setNavigationOnClickListener{onBackPressed()}
        toolbar.setOnMenuItemClickListener {
            var querytext=search_setting_query.text.toString()

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
                if(day_from.tag!=null&&day_from.tag is String){
                    querytext +=day_from.tag
                }
            if(day_to.tag!=null&&day_to.tag is String){
                querytext +=day_to.tag
            }
                querytext +=" -rt"
              query.resultType=Query.MIXED

                query.query=querytext


                start<SearchActivity>(Bundle().apply {
                    putByteArray("query_bundle",query.getSerialized())
                })
            FirebaseAnalytics.getInstance(applicationContext).apply {
                logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Bundle().apply {
                  putString(FirebaseAnalytics.Param.ITEM_NAME,querytext)
                })
            }





                true


        }
    }
}
