package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import twitter4j.Query
import xyz.donot.quetzal.util.getDeserialized
import xyz.donot.quetzal.view.adapter.TwitterImageAdapter

class ImageSearchFragment :StaggeredFragment<String,TwitterImageAdapter,TwitterImageAdapter.ViewHolder>()
{
    override val mAdapter by lazy {  TwitterImageAdapter(activity) }
    private  var query : Query?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query=arguments.getByteArray("query_bundle").getDeserialized<Query>()
        query?.query= query?.query+" filter:images"
    }
    override fun loadMore() {
        if(load.value){
            twitterObservable.getSearchAsync(query!!).subscribe {
                if(it.hasNext()){
                    query=it.nextQuery()
                }
                else{
                    load.onNext(false)
                }
                it.tweets.forEach {
                    it.mediaEntities.forEach {
                        mAdapter.add(it.mediaURLHttps)
                    }

                }


            }
        }}
}
