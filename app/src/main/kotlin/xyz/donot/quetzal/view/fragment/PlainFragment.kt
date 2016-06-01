package xyz.donot.quetzal.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxDialogFragment
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator
import kotlinx.android.synthetic.main.fragment_timeline_base.*
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.getTwitterInstance
import xyz.donot.quetzal.view.listener.OnLoadMoreListener

abstract class PlainFragment<L,T:RecyclerView.Adapter<X>,X:RecyclerView.ViewHolder>:RxDialogFragment()
{
  val twitterObservable : TwitterObservable by lazy { TwitterObservable(context,twitter) }
  val twitter by lazy {getTwitterInstance()}
  abstract val data:MutableList<L>
  abstract val  mAdapter : T
  abstract fun loadMore()
  var page : Int = 0
    get() {
      field++
      return field
    }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    base_recycler_view.apply{
      showProgress()
      setItemAnimator(OvershootInRightAnimator(0.1f))
      setLayoutManager( LinearLayoutManager(activity))
      adapter = AlphaInAnimationAdapter(mAdapter)
      setOnScrollListener(object: OnLoadMoreListener(){
        override fun onScrolledToBottom() {
          loadMore()
        }
      })
      setRefreshListener { reload()}

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
    data.clear()
    mAdapter.notifyDataSetChanged()
    loadMore()

  }

}
