package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.util.logw
import xyz.donot.twix.view.adapter.UltimateStatusAdapter
import xyz.donot.twix.view.listener.OnLoadMoreListener
import java.util.*

abstract class BaseFragment : Fragment() {
    open var page : Int = 0
      get() {
        field++
        return field
      }
    open val data by lazy { LinkedList<Status>() }
    open val  mAdapter by lazy {UltimateStatusAdapter(activity, data)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
        val recycler=v.findViewById(R.id.recycler_view)as UltimateRecyclerView
        recycler.apply{

          logw("recycler",childCount.toString())
          recycler.setAdapter(mAdapter)
       layoutManager = ScrollSmoothLineaerLayoutManager(activity, LinearLayoutManager.VERTICAL, false, 300);
          recycler.setOnScrollListener(object:OnLoadMoreListener(){
            override fun onScrolledToBottom() {
              TimelineLoader()
            }


          })


        //setOnLoadMoreListener { itemsCount, maxLastVisiblePosition -> TimelineLoader(); }
          TimelineLoader()
     }
     return v}

  fun enableLoadMore(){
   val recyclerView= view?.findViewById(R.id.recycler_view)as UltimateRecyclerView
    logw("recycler",recyclerView.childCount.toString())
     // recyclerView.enableLoadmore()
  }

  abstract fun TimelineLoader()


}

