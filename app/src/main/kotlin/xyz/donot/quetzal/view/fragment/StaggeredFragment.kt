package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.trello.rxlifecycle.components.support.RxDialogFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import rx.lang.kotlin.BehaviorSubject
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.customview.EqualItemSpacingDecoration
import xyz.donot.quetzal.view.listener.OnLoadMoreListener


abstract class StaggeredFragment<L,T: RecyclerArrayAdapter<L>,X: BaseViewHolder<L>>: RxDialogFragment()
{
    val twitterObservable : TwitterObservable by lazy { TwitterObservable(context, twitter) }
    val twitter by lazy { getTwitterInstance() }
    val load by lazy { BehaviorSubject(true) }
    abstract val  mAdapter : T
    abstract fun loadMore()
    var page : Int = 0
        get() {
            field++
            return field
        }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        base_recycler_view.apply {
            showEmpty()
            addItemDecoration(EqualItemSpacingDecoration(0))
            setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
            setOnScrollListener(object : OnLoadMoreListener() {
                override fun onScrolledToBottom() {
                    loadMore()
                }
            })
            adapter = AlphaInAnimationAdapter(mAdapter)
            setRefreshListener { reload() }
        }
        load.subscribe {
            if(!it){
                mAdapter.setNoMore(R.layout.item_stop_more)
                mAdapter.stopMore()
            }
        }
        reload()
    }

    override fun onDetach() {
        super.onDetach()
        try{
            val childFm= Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFm.isAccessible=true
            childFm.set(this,null)
        }
        catch(e:NoSuchMethodException){
            throw RuntimeException(e)
        }
        catch(e:IllegalAccessException){
            throw RuntimeException(e)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
        return v}
    fun reload(){
        page=0
        mAdapter.clear()
        loadMore()
    }

}
