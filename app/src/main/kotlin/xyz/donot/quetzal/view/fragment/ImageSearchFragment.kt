package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import twitter4j.Query
import twitter4j.Status
import xyz.donot.quetzal.util.extrautils.Bundle
import xyz.donot.quetzal.util.extrautils.start
import xyz.donot.quetzal.util.getDeserialized
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.view.activity.PictureActivity
import xyz.donot.quetzal.view.adapter.TwitterImageAdapter
import java.util.*

class ImageSearchFragment : StaggeredFragment<String, TwitterImageAdapter>()
{
    override val mAdapter by lazy {  TwitterImageAdapter(activity) }
    private  var query : Query?=null
    private val statusList=ArrayList<Status>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query=arguments.getByteArray("query_bundle").getDeserialized<Query>()
        query?.query= query?.query+" filter:images -rt"
        mAdapter.setOnItemClickListener {
                activity.start<PictureActivity>(Bundle {
                    putByteArray("status",statusList[it].getSerialized())
                })
        }
    }
    override fun loadMore() {
        if(load.value){
            twitterObservable.getSearchAsync(query!!).subscribe {
                if(!it.tweets.isEmpty()){
                    empty.onNext(false)
                if(it.hasNext()){
                    query=it.nextQuery()
                }
                else{
                    load.onNext(false)
                }
                it.tweets.forEach {
                    status->
                    status.mediaEntities.forEach {
                        statusList.add(status)
                        mAdapter.add(it.mediaURLHttps)
                    }
                }}
                else{
                    empty.onNext(true)
                }


            }
        }}
}
