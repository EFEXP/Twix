package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.trello.rxlifecycle.components.support.RxAppCompatDialogFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import rx.lang.kotlin.BehaviorSubject
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.extrautils.mainThread
import xyz.donot.quetzal.util.getTwitterInstance

abstract class BaseRecyclerFragment<L, out T : RecyclerArrayAdapter<L>> : RxAppCompatDialogFragment() {
    val twitterObservable: TwitterObservable by lazy { TwitterObservable(context, twitter) }
    val twitter by lazy { getTwitterInstance() }
    val load by lazy { BehaviorSubject(true) }
    val empty by lazy { BehaviorSubject(true) }
    abstract val mAdapter: T
    abstract fun loadMore()
    abstract fun setUpRecycler()
    var page: Int = 0
        get() {
            field++
            return field
        }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tempAdapter= AlphaInAnimationAdapter(mAdapter)
        base_recycler_view.apply {
            empty.subscribe {
                if (it) {
                    showEmpty()
                }
            }
            setItemAnimator(OvershootInRightAnimator(0.3f))
            setUpRecycler()
           setAdapterWithProgress(tempAdapter)
            setRefreshListener { reload() }
        }

        load.subscribe {
            if (!it) {
                mainThread {
                    mAdapter.setNoMore(R.layout.item_stop_more)
                    mAdapter.stopMore()
                }
            }
        }
        reload()
    }

    override fun onDetach() {
        super.onDetach()
        try {
            val childFm = Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFm.isAccessible = true
            childFm.set(this, null)
        } catch(e: NoSuchMethodException) {
            throw RuntimeException(e)
        } catch(e: IllegalAccessException) {
            throw RuntimeException(e)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_timeline_base, container, false)
    }

    open fun reload() {
        page = 0
        mAdapter.clear()
        load.onNext(true)
        loadMore()
    }
}

