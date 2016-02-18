package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.view.adapter.UltimateStatusAdapter
import java.util.*

abstract class BaseFragment : Fragment() {


    open var page : Int = 2
    open val data by lazy { LinkedList<Status>() }
    open val  mAdapter by lazy { UltimateStatusAdapter(activity, data) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
        val recycler_view=v.findViewById(R.id.recycler_view)as UltimateRecyclerView

        recycler_view.apply {
          setAdapter(mAdapter)
          setOnScrollChangeListener({ p0, p1, p2, p3, p4 -> enableLoadmore() })
       layoutManager = LinearLayoutManager(activity)
         setOnLoadMoreListener { itemsCount, maxLastVisiblePosition ->
           TimelineLoader();
           page++

         }

     }


     return v}



  abstract fun TimelineLoader()

}
