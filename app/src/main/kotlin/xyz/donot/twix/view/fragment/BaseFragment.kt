package xyz.donot.twix.view.fragment


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malinskiy.superrecyclerview.SuperRecyclerView
import com.trello.rxlifecycle.components.support.RxFragment
import twitter4j.Status
import xyz.donot.twix.R
import xyz.donot.twix.view.adapter.StatusAdapter
import xyz.donot.twix.view.dialog.TweetTapDialog
import xyz.donot.twix.view.listener.OnRecyclerListener
import java.util.*

abstract class BaseFragment : RxFragment() {
    open var page : Int = 2
    open val data by lazy {  ArrayList<Status>() }
    open val  mAdapter by lazy { StatusAdapter(activity, data,OnRecyclerListener { view, i ->
      TweetTapDialog().show(activity.supportFragmentManager,"TweetTapDialog")
    }) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_timeline_base, container, false)
        val recycler_view=v.findViewById(R.id.recycler_view)as SuperRecyclerView

     recycler_view.apply {
       setLayoutManager(LinearLayoutManager(activity))
       adapter = mAdapter
        setupMoreListener( { numberOfItems,  numberBeforeMore,  currentItemPos ->
          TimelineLoader();
          page++ }
          , 10)
     }

     return v}



  abstract fun TimelineLoader()

}
